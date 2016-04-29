package org.as3commons.reflect;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class AccessorAccess {

	public static final String READ_ONLY = "readonly";
	public static final String WRITE_ONLY = "writeonly";
	public static final String READ_WRITE = "readwrite";
	
	public String name;
	
	public AccessorAccess(String n) {
		name = n;
	}
}
