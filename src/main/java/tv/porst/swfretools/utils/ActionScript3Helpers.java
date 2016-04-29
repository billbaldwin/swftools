package tv.porst.swfretools.utils;

import org.apache.commons.lang3.ArrayUtils;

import tv.porst.splib.strings.StringHelpers;
import tv.porst.swfretools.parser.structures.ConstantPool;
import tv.porst.swfretools.parser.structures.GenericVector;
import tv.porst.swfretools.parser.structures.MultinameInfo;
import tv.porst.swfretools.parser.structures.MultinameInfoList;
import tv.porst.swfretools.parser.structures.NamespaceInfo;
import tv.porst.swfretools.parser.structures.NamespaceInfoList;
import tv.porst.swfretools.parser.structures.QName;
import tv.porst.swfretools.parser.structures.StringInfoList;

/**
 * Contains helper functions for working with ActionScript 3 code.
 */
public final class ActionScript3Helpers {

	/**
	 * Looks up a namespace in a namespace list.
	 * 
	 * @param namespaceIndex Index of the namespace.
	 * @param namespaceList Provides all available namespaces.
	 * 
	 * @return The resolved namespace or null if the namespaceIndex was 0.
	 * 
	 * @throws ResolverException Thrown if the namespace could not be resolved.
	 */
	private static NamespaceInfo resolveNamespace(final int namespaceIndex, final NamespaceInfoList namespaceList) throws ResolverException {

		if (namespaceIndex == 0) {
			return null;
		}
		else if (namespaceIndex > namespaceList.size()) {
			throw new ResolverException("Invalid namespace index");
		}
		else {
			return namespaceList.get(namespaceIndex - 1);
		}
	}

	/**
	 * Looks up a string in a string info list
	 * 
	 * @param stringIndex The index of the string to look up.
	 * @param constantPool Provides the strings.
	 * 
	 * @return The looked up string or null if the string index was 0.
	 * 
	 * @throws ResolverException Thrown if the string could not be looked up.
	 */
	private static String resolveString(final int stringIndex, final StringInfoList stringList) throws ResolverException {

		if (stringIndex == 0) {
			return null;
		}
		else if (stringIndex > stringList.size()) {
			throw new ResolverException("Invalid string index");
		}
		else {
			return stringList.get(stringIndex - 1).getName().value();
		}
	}

	/**
	 * Flattens a multi-part namespace string into a single string.
	 * 
	 * @param namespaceParts The parts of the namespace.
	 * 
	 * @return The flattened namespace string.
	 */
	public static String flattenNamespaceName(final String[] namespaceParts) {

		if (namespaceParts == null) {
			throw new IllegalArgumentException("Namespace parts argument must not be null.");
		}

		return StringHelpers.join(namespaceParts, ":");
	}

	/**
	 * Returns the individual components of a multiname namespace in string form.
	 * 
	 * @param multinameInfo The multiname namespace to resolve.
	 * @param constantPool Provides the constant strings for the resolved namespace.
	 * @param namespaceList Provides the available namespace.
	 * 
	 * @return The individual components of the resolved multiname namespace.
	 * 
	 * @throws ResolverException Thrown if the namespace could not be resolved.
	 */
	public static String[] resolveMultiname(final MultinameInfo multinameInfo, final ConstantPool constantPool) throws ResolverException {

		if (multinameInfo == null) {
			throw new IllegalArgumentException("MultinameInfo argument must not be null.");
		}

		if (constantPool == null) {
			throw new IllegalArgumentException("ConstantPool argument must not be null.");
		}

		final NamespaceInfoList namespaceList = constantPool.getNamespaces();
		
		if (namespaceList == null) {
			throw new IllegalArgumentException("NamespaceList argument must not be null.");
		}

		if (multinameInfo.getKind().value() == 7) {
			final QName qname = (QName) multinameInfo.getData();

			final int ns = qname.getNs().value();
			final int nameIndex = qname.getName().value();

			final NamespaceInfo namespace = resolveNamespace(ns, namespaceList);

			if (namespace == null) {
				return new String[0];
			}

			final int namespaceNameIndex = namespace.getName().value();
			final String namespaceName = resolveString(namespaceNameIndex, constantPool.getStrings());

			final String name = resolveString(nameIndex, constantPool.getStrings());

			return new String[] { namespaceName, name };
		}
		else if ( multinameInfo.getKind().value() == 29) {
			final GenericVector vec = (GenericVector) multinameInfo.getData();
			
			final MultinameInfoList multinames = constantPool.getMultinames();
			MultinameInfo type = multinames.get(vec.getTypeDefinition().value()-1);
			MultinameInfo param = multinames.get(vec.getParams().get(0).value()-1);
			
			String[] outer = resolveMultiname(type, constantPool);
			String[] inner = resolveMultiname(param, constantPool);
			
			// inner could be more nested Vectors, so concat full arrays
			return ArrayUtils.addAll(outer, inner);
		}
		else {
			return new String[0];
		}
	}

}
