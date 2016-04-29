package com.beecavegames.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.as3commons.reflect.Accessor;
import org.as3commons.reflect.AccessorAccess;
import org.as3commons.reflect.Field;
import org.as3commons.reflect.Metadata;
import org.as3commons.reflect.MetadataArgument;
import org.as3commons.reflect.Type;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.Messages;

import com.beecavegames.dataModel.ClassReflectionData;
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
import tv.porst.swfretools.parser.structures.MultinameInfo;
import tv.porst.swfretools.parser.structures.NamespaceInfo;
import tv.porst.swfretools.parser.structures.QName;
import tv.porst.swfretools.parser.structures.SWFFile;
import tv.porst.swfretools.parser.structures.TagList;
import tv.porst.swfretools.parser.structures.TraitClass;
import tv.porst.swfretools.parser.structures.TraitGetter;
import tv.porst.swfretools.parser.structures.TraitMethod;
import tv.porst.swfretools.parser.structures.TraitSetter;
import tv.porst.swfretools.parser.structures.TraitSlot;
import tv.porst.swfretools.parser.structures.TraitsInfo;
import tv.porst.swfretools.parser.tags.DoABCTag;
import tv.porst.swfretools.parser.tags.Tag;
import tv.porst.swfretools.utils.ResolverException;

public class ReflectTool {
	
	public static final AccessorAccess access_readonly = new AccessorAccess(AccessorAccess.READ_ONLY);
	public static final AccessorAccess access_writeonly = new AccessorAccess(AccessorAccess.WRITE_ONLY);
	public static final AccessorAccess access_readwrite = new AccessorAccess(AccessorAccess.READ_WRITE);
	
	@Option(name="-swf", usage="Name of SWF file to process.", metaVar="[SWF name]", required=true)
	public String swfName;

	@Option(name="-json", usage="Name of JSON output file.", metaVar="[JSON file name]", required=false)
	public String jsonFilename;
	
	@Option(name="-amf", usage="Name of AMF output file.", metaVar="[AMF file name]", required=false)
	public String amfFilename;

	@Option(name="-classes", usage="Text file containing names of classes to reflect.", metaVar="[class list file name]", required=true)
	public String classListFilename;

	@Option(name="-quiet", aliases={"-q"}, required=false)
	public boolean quiet;
	
    public static void main( String[] args ) throws IOException, SWFParserException, ResolverException {
    	ReflectTool it = new ReflectTool();
    	it.doMain(args);
    }

    private List<String> classList;
    private List<Pattern> patternList;
    
    private void doMain(String[] args) throws IOException, SWFParserException, ResolverException {
    	
    	CmdLineParser parser = new CmdLineParser(this);

    	try {
    		parser.parseArgument(args);
    		if ( swfName == null ) {
    			throw new CmdLineException(parser, Messages.valueOf("No SWF file specified."));
    		}
    		if ( classListFilename == null ) {
    			throw new CmdLineException(parser, Messages.valueOf("No class list file specified."));
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
    	
    	classList = Files.readAllLines(Paths.get(classListFilename));
    	
    	// Any entries surrounded by () are treated as regular expressions.
    	patternList = new ArrayList<>();
    	Iterator<String> it = classList.iterator();
    	while ( it.hasNext() ) {
    		String c = it.next();
    		if ( c.startsWith("(") && c.endsWith(")") ) {
    			// remove from class list, add to pattern list
    			it.remove();
    			c = c.substring(1, c.length()-1);
    			Pattern pattern = Pattern.compile(c);
    			patternList.add(pattern);
    		}
    	}
    	
    	ClassReflectionData reflectionData = computeReflectionData(tags);
    	
    	boolean printOutput = (amfFilename == null && jsonFilename == null);
    	
    	if ( (jsonFilename != null) || printOutput ) {
	    	Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	    	String json = gson.toJson(reflectionData);
	    	if ( printOutput ) {
	    		System.out.println(json);
	    	}
	    	else {
	    		logInfo("Saving "+jsonFilename);
	    		Path jsonPath = Paths.get(jsonFilename);
	    		Files.write(jsonPath, json.getBytes());
	    	}
    	}
    	
    	if ( amfFilename != null ) {
	    	// Convert to AMF
	    	ByteArrayOutputStream out = new ByteArrayOutputStream();
	    	try ( AMF3Serializer amfOut = new AMF3Serializer(out) ) {
	    		amfOut.writeObject(reflectionData);    		
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
    
    private ClassReflectionData computeReflectionData(TagList tags) throws ResolverException {
    	
    	ClassReflectionData reflectionData = new ClassReflectionData();
    	reflectionData.types = new HashMap<>();
    	
		Map<String, String> superclassByClass = new HashMap<>();
		Map<String, Pair<AS3Data,InstanceInfo>> instanceInfoByClass = new HashMap<>();

    	for ( Tag tag : tags ) {
    		if ( tag instanceof DoABCTag ) {
    			DoABCTag abc = (DoABCTag) tag;
    			AS3Data abcData = abc.getAbcData();

    			for ( int i = 0; i < abcData.getClassCount().value(); i++ )  {
    				InstanceInfo instanceInfo = abcData.getInstances().get(i);
    				String className = ABCUtil.resolveMultiname(abcData, instanceInfo.getName().value(), true);
    				
    				instanceInfoByClass.put(className, new ImmutablePair<AS3Data,InstanceInfo>(abcData,instanceInfo));
    				
    				// Record superclass name for every class.
    				if ( instanceInfo.getSuperName().value() != 0 ) {
    					String superclassName = ABCUtil.resolveMultiname(abcData, instanceInfo.getSuperName().value(), true);
    					if ( !superclassName.equals("Object") ) {
    						superclassByClass.put(className, superclassName);
    					}
    				}
    				
    				
    				boolean doClass = classList.contains(className);
    				if ( !doClass ) {
    					for ( Pattern p : patternList ) {
    						if ( p.matcher(className).matches() ) {
    							doClass = true;
    							break;
    						}
    					}
    				}

    				if ( doClass ) {
    					logInfo("  "+className);
						Type type = reflectClass(abcData, className, instanceInfo);
						reflectionData.types.put(className, type);
    				}
    			}
    		}
    	}
    	
    	// Need to reflect any superclasses that weren't in our list.
    	Map<String,Type> newTypes = new HashMap<>();
    	for ( String className : reflectionData.types.keySet() ) {
    		while ( superclassByClass.containsKey(className) ) {
    			className = superclassByClass.get(className);
        		if ( !reflectionData.types.containsKey(className) && instanceInfoByClass.containsKey(className) ) {
        			AS3Data abcData = instanceInfoByClass.get(className).getLeft();
        			InstanceInfo instanceInfo = instanceInfoByClass.get(className).getRight();
    				Type type = reflectClass(abcData, className, instanceInfo);
    				newTypes.put(className, type);
        		}
    		}
    	}

    	for ( String className : newTypes.keySet() ) {
    		reflectionData.types.put(className, newTypes.get(className));
    	}
    	
    	// Now walk inheritance tree, adding fields from super- to sub-classes.
    	DefaultMutableTreeNode root = Util.buildInheritanceTree(superclassByClass);

		@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> traverse = root.breadthFirstEnumeration();
		while ( traverse.hasMoreElements() ) {
			DefaultMutableTreeNode node = traverse.nextElement();
			String className = (String) node.getUserObject();
			if ( reflectionData.types.containsKey(className) ) {
				Type classType = reflectionData.types.get(className);
				
				@SuppressWarnings("unchecked")
				Enumeration<DefaultMutableTreeNode> children = node.children();
				while ( children.hasMoreElements() ) {
					DefaultMutableTreeNode child = children.nextElement();
					String subclassName = (String) child.getUserObject();
					
					// ALL classes are in the tree, but we only care about the classes in classList plus their
					// superclasses.
					if ( reflectionData.types.containsKey(subclassName) ) {
						Type subclassType = reflectionData.types.get(subclassName);
			
						// Add superclass fields to subclass, skipping fields that already exist
						// (by name) in subclass.
						for ( Field superField : classType.fields ) {
							if ( !subclassType.fields.stream().anyMatch(f -> f.name.equals(superField.name)) ) {
								subclassType.fields.add(superField);
							}
						}
						for ( Accessor superAcc : classType.accessors ) {
							if ( !subclassType.accessors.stream().anyMatch(a -> a.name.equals(superAcc.name)) ) {
								subclassType.accessors.add(superAcc);
							}
						}
					}
				}
			}
		}
		
		reflectionData.fields = new ArrayList<>();
		reflectionData.accessors = new ArrayList<>();
		
		Map<Field,Integer> uniqueFields = new HashMap<>();
		Map<Accessor,Integer> uniqueAccessors = new HashMap<>();
		for ( Type type : reflectionData.types.values() ) {
			for ( Field field : type.fields ) {
				if ( !uniqueFields.containsKey(field) ) {
					uniqueFields.put(field, reflectionData.fields.size());
					reflectionData.fields.add(field);
				}
			}
			for ( Accessor acc : type.accessors ) {
				if ( !uniqueAccessors.containsKey(acc) ) {
					uniqueAccessors.put(acc, reflectionData.accessors.size());
					reflectionData.accessors.add(acc);
				}
			}
		}

		for ( Type type : reflectionData.types.values() ) {
			type.fieldIndices = new ArrayList<>();
			for ( Field field : type.fields ) {
				type.fieldIndices.add(reflectionData.fields.indexOf(field));
			}
			type.accessorIndices = new ArrayList<>();
			for ( Accessor acc : type.accessors ) {
				type.accessorIndices.add(reflectionData.accessors.indexOf(acc));
			}
			if ( type.fieldIndices.size() == 0 ) {
				type.fieldIndices = null;
			}
			if ( type.accessorIndices.size() == 0 ) {
				type.accessorIndices = null;
			}
			type.fields = null;
			type.accessors = null;
		}
    	return reflectionData;
    }

    private Type reflectClass(AS3Data abcData, String className, InstanceInfo instanceInfo) throws ResolverException {
    	Type result = new Type();
    	
    	result.fullName = className;
    	result.name = className.substring(className.lastIndexOf(':')+1);
    	result.isDynamic = (instanceInfo.getFlags().value() & 0x1) == 0;	// isDynamic = sealed flag not set
    	result.isInterface = (instanceInfo.getFlags().value() & 0x4) != 0;	// isInterface = interface flag set
    	result.fields = new ArrayList<>();

    	Map<String,TraitsInfo> getters = new HashMap<>();
    	Map<String,TraitsInfo> setters = new HashMap<>();

    	for ( int t = 0; t < instanceInfo.getTraitCount().value(); t++ ) {
			TraitsInfo trait = instanceInfo.getTraits().get(t);
			
			MultinameInfo multiname = abcData.getConstantPool().getMultinames().get(trait.getName().value()-1);
			int multinameKind = multiname.getKind().value();			
			if ( multinameKind == 7 ) {	// QName
				QName qname = (QName)multiname.getData();
				Pair<String,Integer> ns = resolveNamespace(abcData, qname.getNs().value());
				
//				System.out.println("    "+resolveString(abcData, qname.getName().value()) + " " + ns.getLeft()+" "+ns.getRight());
				if ( ns.getValue() != 0x16 ) {
					// private or protected, skip
					continue;
				}
			}
			
			Class<?> tclass = trait.getData().getClass();

			if ( tclass.equals(TraitSlot.class) ) {
				TraitSlot slot = (TraitSlot) trait.getData();
				
				Field field = new Field();
				field.name = ABCUtil.resolveMultiname(abcData, trait.getName().value(), false);
				field.typeName = ABCUtil.resolveMultiname(abcData, slot.getTypeName().value(), true);
				
				field.metadata = getMetadata(abcData, trait);
				
				result.fields.add(field);
			}
			else if ( tclass.equals(TraitClass.class) ) {
				result.metadata = getMetadata(abcData, trait);
			}
			else if ( tclass.equals(TraitGetter.class) || tclass.equals(TraitSetter.class) ) {
				TraitMethod method = (TraitMethod) trait.getData();
				MethodInfo methodInfo = abcData.getMethodInfos().get(method.getMethod().value());
				String accessorName = resolveString(abcData, methodInfo.getName().value());

				if ( tclass.equals(TraitGetter.class) ) {
					getters.put(accessorName, trait);
				}
				else {
					setters.put(accessorName, trait);
				}
			}
		}
    	
    	//
    	// Need to create Accessors for read-only or write-only properties.
    	//
    	List<TraitsInfo> readonly = new ArrayList<>();
    	List<TraitsInfo> writeonly = new ArrayList<>();
    	List<TraitsInfo> readwrite = new ArrayList<>();
    	
    	// TODO: set operations...
    	for ( String acc : setters.keySet() ) {
    		if ( getters.containsKey(acc) ) {
    			// Metadata can be on getter or setter, but not both. Need
    			// to choose the right one in order to get metadata.
    			TraitsInfo t = setters.get(acc);
    			List<Metadata> gm = getMetadata(abcData, getters.get(acc));
    			if ( gm != null ) {
    				t = getters.get(acc);
    			}    			
    			readwrite.add(t);
    		}
    		else {
    			writeonly.add(setters.get(acc));
    		}
    	}
    	for ( String acc : getters.keySet() ) {
    		if ( !setters.containsKey(acc) ) {
    			readonly.add(getters.get(acc));
    		}
    	}
    	
    	result.accessors = createAccessors(abcData, readonly, access_readonly);
    	result.accessors.addAll(createAccessors(abcData, writeonly, access_writeonly));
    	result.accessors.addAll(createAccessors(abcData, readwrite, access_readwrite));
    	
    	if ( instanceInfo.getInterfaceCount().value() > 0 ) {
    		result.interfaces = new ArrayList<>();
    		for ( EncodedU30 iface : instanceInfo.getInterfaces() ) {
    			result.interfaces.add( ABCUtil.resolveMultiname(abcData, iface.value(), true) );
    		}
    	}
    	
		return result;
    }

    private List<Accessor> createAccessors(AS3Data abcData, List<TraitsInfo> traits, AccessorAccess access ) throws ResolverException {
    	List<Accessor> result = new ArrayList<>();

    	for ( TraitsInfo trait : traits ) {
			Accessor acc = new Accessor();
			
			TraitMethod method = (TraitMethod) trait.getData();
			MethodInfo methodInfo = abcData.getMethodInfos().get(method.getMethod().value());
			int methodNameIndex = methodInfo.getName().value();

			int typeNameIndex;
			if ( access.equals(access_readonly) || trait.getData().getClass().equals(TraitGetter.class) ) {
				// Getter, look at return type.
				typeNameIndex = methodInfo.getReturnType().value();
			}
			else {
				// Setter, so look at type of first parameter.
				typeNameIndex = methodInfo.getParamTypes().get(0).value();
			}
	
			acc.typeName = ABCUtil.resolveMultiname(abcData, typeNameIndex, true);
			acc.name = resolveString(abcData, methodNameIndex);
			acc.metadata = getMetadata(abcData, trait);
			acc.access = access;
			
			result.add(acc);
    	}
    	return result;
    }
    
    private List<Metadata> getMetadata(AS3Data abcData, TraitsInfo trait) {
    	List<Metadata> result = null;
    	if ( trait.getMetaDataCount() != null ) {
			result = new ArrayList<>();
			for ( EncodedU30 md : trait.getMetaData() ) {
				MetaData metaData = abcData.getMetaData().get(md.value());
				String mdname = resolveString(abcData, metaData.getName().value());
				if ( mdname.equals("__go_to_definition_help") ) {
					continue;
				}
				Metadata fieldMetaData = new Metadata();
				fieldMetaData.name = mdname;
				for ( ItemInfo itemInfo : metaData.getItems() ) {
					int k = itemInfo.getKey().value();
					String key = null, value = null;
					if ( k != 0 ) {
						key = resolveString(abcData, k);
					}
					int v = itemInfo.getValue().value();
					value = resolveString(abcData, v);
					fieldMetaData.addArgument(new MetadataArgument(key, value));
				}
				result.add(fieldMetaData);
			}
			if ( result.size() == 0 ) {
				result = null;
			}
    	}
		return result;
	}
    
    private String resolveString(AS3Data abcData, int index) {
    	String result = null;
    	if ( index != 0 ) {
    		result = abcData.getConstantPool().getStrings().get(index-1).getName().value();
    	}
    	return result;
    }

    private Pair<String,Integer> resolveNamespace(AS3Data abcData, int index) {
		NamespaceInfo namespace = abcData.getConstantPool().getNamespaces().get(index-1);
		String name = resolveString(abcData, namespace.getName().value());
    	int kind = namespace.getKind().value();
		return new ImmutablePair<String, Integer>(name, kind); 
    }
}
