package com.beecavegames.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.Messages;
import org.swiftsuspenders.injectionpoints.InjectionPoint;
import org.swiftsuspenders.injectionpoints.PostConstructInjectionPoint;
import org.swiftsuspenders.injectionpoints.PropertyInjectionPoint;

import com.beecavegames.dataModel.ClassInjectionPointData;
import com.exadel.flamingo.flex.messaging.amf.io.AMF3Serializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tv.porst.swfretools.parser.SWFParser;
import tv.porst.swfretools.parser.SWFParserException;
import tv.porst.swfretools.parser.structures.AS3Data;
import tv.porst.swfretools.parser.structures.EncodedU30;
import tv.porst.swfretools.parser.structures.InstanceInfo;
import tv.porst.swfretools.parser.structures.ItemInfo;
import tv.porst.swfretools.parser.structures.MetaData;
import tv.porst.swfretools.parser.structures.MethodInfo;
import tv.porst.swfretools.parser.structures.SWFFile;
import tv.porst.swfretools.parser.structures.TagList;
import tv.porst.swfretools.parser.structures.TraitMethod;
import tv.porst.swfretools.parser.structures.TraitSetter;
import tv.porst.swfretools.parser.structures.TraitSlot;
import tv.porst.swfretools.parser.structures.TraitsInfo;
import tv.porst.swfretools.parser.tags.DoABCTag;
import tv.porst.swfretools.parser.tags.Tag;
import tv.porst.swfretools.utils.ResolverException;

public class InjectTool
{
	@Option(name="-swf", usage="Name of SWF file to process.", metaVar="[SWF name]", required=true)
	public String swfName;

	@Option(name="-json", usage="Name of JSON output file.", metaVar="[JSON file name]", required=false)
	public String jsonFilename;
	
	@Option(name="-amf", usage="Name of AMF output file.", metaVar="[AMF file name]", required=false)
	public String amfFilename;

	@Option(name="-quiet", aliases={"-q"}, required=false)
	public boolean quiet;
	
    public static void main( String[] args ) throws IOException, SWFParserException, ResolverException {
    	InjectTool it = new InjectTool();
    	it.doMain(args);
    }
    
    private void doMain(String[] args) throws IOException, SWFParserException, ResolverException {
    	
    	CmdLineParser parser = new CmdLineParser(this);

    	try {
    		parser.parseArgument(args);
    		if ( swfName == null ) {
    			throw new CmdLineException(parser, Messages.valueOf("No SWF file specified."));
    		}
    		if ( jsonFilename == null && amfFilename == null ) {
    			System.out.println("No JSON or AMF output files specified, will print JSON to stdout.");
    		}
    	}
    	catch ( CmdLineException e ) {
    		System.err.println(e.getMessage());
    		parser.printUsage(System.err);
    		return;
    	}

   		logInfo("Parsing "+swfName);
   		
    	File file = new File(swfName);
    	SWFFile swfFile = SWFParser.parse(file);
    	TagList tags = swfFile.getTags();
    	
    	ClassInjectionPointData output = computeInjectionPoints(tags);
    	
    	logInfo("Found "+output.injectionPoints.size()+" unique injection points in "+output.classInjectionPoints.size()+" classes.");

    	boolean printOutput = (amfFilename == null && jsonFilename == null);
    	
    	if ( (jsonFilename != null) || printOutput ) {
	    	Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	    	String json = gson.toJson(output);
	    	if ( printOutput ) {
	    		System.out.println(json);
	    	}
	    	else {
	    		System.out.println("Saving "+jsonFilename);
	    		Path jsonPath = Paths.get(jsonFilename);
	    		Files.write(jsonPath, json.getBytes());
	    	}
    	}
    	
    	if ( amfFilename != null ) {
	    	// Convert to AMF
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	try ( AMF3Serializer amfOut = new AMF3Serializer(out) ) {
	    		amfOut.writeObject(output);    		
	    	}
	    	byte[] amf = out.toByteArray();
	    	System.out.println("Saving "+amfFilename);
	    	Path path = Paths.get(amfFilename);
	    	Files.write(path, amf);
    	}
    }
    
    public void logInfo(String msg) {
    	if ( !quiet ) {
    		System.out.println(msg);
    	}
    }
    
    public static ClassInjectionPointData computeInjectionPoints(TagList tags) throws ResolverException {

		Map<String, List<InjectionPoint>> injections = new HashMap<>();
		Map<String, String> superclasses = new HashMap<>();

    	for ( Tag tag : tags ) {
    		if ( tag instanceof DoABCTag ) {
    			DoABCTag abc = (DoABCTag) tag;
    			AS3Data abcData = abc.getAbcData();
    			
    			for ( int i = 0; i < abcData.getClassCount().value(); i++ )  {
    				InstanceInfo instanceInfo = abcData.getInstances().get(i);

    				String className = ABCUtil.resolveMultiname(abcData, instanceInfo.getName().value(), true);
    				if ( instanceInfo.getSuperName().value() != 0 ) {
    					String superclassName = ABCUtil.resolveMultiname(abcData, instanceInfo.getSuperName().value(), true);
    					if ( !superclassName.equals("Object") ) {
    						superclasses.put(className, superclassName);
    					}
    				}
    				
    				List<InjectionPoint> classInjections = new ArrayList<>();
    				
    				for ( int t = 0; t < instanceInfo.getTraitCount().value(); t++ ) {
    					TraitsInfo trait = instanceInfo.getTraits().get(t);
    					
    					if ( trait.getMetaDataCount() != null ) {
    						for ( EncodedU30 md : trait.getMetaData() ) {
    							MetaData metadata = abcData.getMetaData().get(md.value());
    							String mdName = abcData.getConstantPool().getStrings().get(metadata.getName().value() - 1).getName().value();
    							if ( mdName.equals("Inject") ) {
    								String injectionName = findMetadataValue(abcData, metadata, "name");
    								PropertyInjectionPoint pip = createPropertyInjectionPoint(abcData, trait, injectionName);
    								classInjections.add(pip);
    								break;
    							}
    							else if ( mdName.equals("PostConstruct") ) {
    								String order = findMetadataValue(abcData, metadata, "order");
    								PostConstructInjectionPoint pcip = createPostConstructInjectionPoint(abcData, trait, order);
    								classInjections.add(pcip);
    							}
    						}
    					}
    				}
    				if ( !classInjections.isEmpty() ) {
    					injections.put(className, classInjections);
    				}
    			}
    		}
    	}

    	// SWFParser unfortunately does not resolve inheritance, so our list of injections
    	// for each class does not include injections from superclasses. We have to do
    	// that manually.
    	
    	DefaultMutableTreeNode root = Util.buildInheritanceTree(superclasses);
		
    	// We now have a full inheritance tree. For each injected class, walk up to root, adding
    	// any injection points from superclasses. (Classes with no super- or subclasses are *not*
		// in inheritance tree.
		
		// Traverse tree from root, adding injection points from parent to child.
    	@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> traverse = root.breadthFirstEnumeration();
		while ( traverse.hasMoreElements() ) {
			DefaultMutableTreeNode node = traverse.nextElement();
			String className = (String) node.getUserObject();
			if ( injections.containsKey(className) ) {
				@SuppressWarnings("unchecked")
				Enumeration<DefaultMutableTreeNode> children = node.children();
				while ( children.hasMoreElements() ) {
					DefaultMutableTreeNode child = children.nextElement();
					String subclassName = (String) child.getUserObject();
					if ( !injections.containsKey(subclassName) ) {
						injections.put(subclassName, new ArrayList<InjectionPoint>());
					}
					List<InjectionPoint> subclassInjections = injections.get(subclassName);
					
					// Add parent PostConstruct points *only* if there isn't an equivalent one in subclass.
					// Otherwise, same function will get called 2x.
					for ( InjectionPoint ip : injections.get(className) ) {
						if ( !(ip instanceof PostConstructInjectionPoint) ) {
							subclassInjections.add(ip);
						}
						else if ( !subclassInjections.contains(ip) ) { 
							subclassInjections.add(ip);
						}
					}
				}
			}
		}
    	
    	// We now have complete injection points for all classes. Because
    	// so many of our injection points are duplicated across classes,
    	// build a set of unique injection points and refer to those
    	// instead of storing duplicates.
		
    	Map<InjectionPoint, Integer> uniqueInjectionPoints = new HashMap<>();
    	for ( List<InjectionPoint> points : injections.values() ) {
    		for ( InjectionPoint ip : points ) {
    			if ( !uniqueInjectionPoints.containsKey(ip) ) {
    				uniqueInjectionPoints.put(ip, 0);
    			}
    		}
    	}
    	
    	ClassInjectionPointData output = new ClassInjectionPointData();
    	
    	// Build master list of injection points, and store index by point so we
    	// can look that up.
    	
    	int idx = 0;
    	output.injectionPoints = new ArrayList<>();
    	output.classInjectionPoints = new HashMap<>();
    	for ( InjectionPoint ip : uniqueInjectionPoints.keySet() ) {
    		output.injectionPoints.add(ip);
    		uniqueInjectionPoints.put(ip, idx++);
    	}

    	// Build list of injectionPoint indices per injected class.
    	
    	for ( Map.Entry<String, List<InjectionPoint>> classInjection : injections.entrySet() ) {
    		List<InjectionPoint> points = classInjection.getValue();
    		List<Integer> pointIndices = new ArrayList<>();
    		for ( InjectionPoint point : points ) {
    			int pointIndex = uniqueInjectionPoints.get(point);
    			pointIndices.add(pointIndex);
    		}
    		
    		// PostConstruct need to go at the end, and sorted by order if any.
    		sortInjectionPoints(output.injectionPoints, pointIndices);
    		
    		output.classInjectionPoints.put(classInjection.getKey(), pointIndices);
    	}

    	return output;
    }
    
    public static void checkExisting(Enumeration<DefaultMutableTreeNode> list, String key) {
    	if ( list.hasMoreElements() ) {
	    	for ( DefaultMutableTreeNode node = list.nextElement(); list.hasMoreElements(); node = list.nextElement() ) {
	    		if ( node.getUserObject().equals(key) ) {
	    			System.out.println("ouch");
	    		}
	    	}
    	}
    }

    public static void sortInjectionPoints(List<InjectionPoint> injectionPoints, List<Integer> pointIndices) {
    	pointIndices.sort(new Comparator<Integer>() {
    		@Override
    		public int compare(Integer p1, Integer p2) {
    			InjectionPoint point1 = injectionPoints.get(p1.intValue());
    			InjectionPoint point2 = injectionPoints.get(p2.intValue());
    			if ( point1 instanceof PostConstructInjectionPoint ) {
    				if ( point2 instanceof PostConstructInjectionPoint ) {
    					PostConstructInjectionPoint pcip1 = (PostConstructInjectionPoint) point1;
    					PostConstructInjectionPoint pcip2 = (PostConstructInjectionPoint) point2;
    					if ( pcip1.order < pcip2.order ) {
    						return -1;
    					}
    					else if ( pcip1.order >  pcip2.order ) {
    						return 1;
    					}
    				}
    				else {
    					return 1;
    				}
    			}
    			else if ( point2 instanceof PostConstructInjectionPoint ) {
    				return -1;
    			}
   				return 0;
    		}
    	});
    }

    
    public static PropertyInjectionPoint createPropertyInjectionPoint( AS3Data abcData, TraitsInfo trait, String injectionName ) throws ResolverException {
    	PropertyInjectionPoint result = new PropertyInjectionPoint();
    	
		Class<?> tclass = trait.getData().getClass();
		if ( tclass == TraitSlot.class ) {
			TraitSlot slot = (TraitSlot) trait.getData();
			result.propertyName = ABCUtil.resolveMultiname(abcData, trait.getName().value(), false);
			result.propertyType = ABCUtil.resolveMultiname(abcData, slot.getTypeName().value(), true);
		}
		else if ( tclass == TraitMethod.class || tclass == TraitSetter.class ) {
			TraitMethod method = (TraitMethod) trait.getData();
			MethodInfo methodInfo = abcData.getMethodInfos().get(method.getMethod().value());
			int methodNameIndex = methodInfo.getName().value();
			result.propertyName = abcData.getConstantPool().getStrings().get(methodNameIndex-1).getName().value();
			
			// Setter, so look at 0th parameter type.
			int typeNameIndex = methodInfo.getParamTypes().get(0).value();
			result.propertyType = ABCUtil.resolveMultiname(abcData, typeNameIndex, true);
		}
		result.injectionName = ( injectionName == null ) ? "" : injectionName;

    	return result;
    }
    
    public static PostConstructInjectionPoint createPostConstructInjectionPoint( AS3Data abcData, TraitsInfo trait, String order ) {
    	PostConstructInjectionPoint result = new PostConstructInjectionPoint();

		TraitMethod method = (TraitMethod) trait.getData();
		MethodInfo methodInfo = abcData.getMethodInfos().get(method.getMethod().value());
		int methodNameIndex = methodInfo.getName().value();
		result.methodName = abcData.getConstantPool().getStrings().get(methodNameIndex-1).getName().value();
    	if ( order != null ) {
    		result.order = Integer.valueOf(order);
    	}
    	
    	return result;
    }
    
    public static String findMetadataValue(AS3Data abcData, MetaData metadata, String key) {
    	String result = null;
		for ( ItemInfo itemInfo : metadata.getItems() ) {
			int k = itemInfo.getKey().value();
			String ks = abcData.getConstantPool().getStrings().get(k-1).getName().value();
			if ( ks.equals(key) ) {
				int v = itemInfo.getValue().value();
				result = abcData.getConstantPool().getStrings().get(v-1).getName().value();
				break;
			}
		}
    	return result;
    }
}
