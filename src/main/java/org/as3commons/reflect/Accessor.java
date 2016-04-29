package org.as3commons.reflect;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class Accessor extends Field {
	public AccessorAccess access;
}
