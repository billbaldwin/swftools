package org.as3commons.reflect;

import java.util.List;

public class Type {
	public String			name;
	public String			fullName;
	public String			clazz;
	public boolean			isDynamic;
	public boolean			isInterface;
	public List<String>		interfaces;
	public List<Field>		fields;
	public List<Integer>	fieldIndices;
	public List<Accessor>	accessors;
	public List<Integer>	accessorIndices;
	public List<Metadata>	metadata;
}
