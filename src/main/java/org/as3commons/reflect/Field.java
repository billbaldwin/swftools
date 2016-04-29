package org.as3commons.reflect;

import java.util.List;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Field {

	public boolean			isStatic;
	public String			name;
	public String			typeName;
	public List<Metadata>	metadata;
}
