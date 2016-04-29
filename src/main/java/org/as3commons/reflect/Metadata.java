package org.as3commons.reflect;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Metadata {
	public String name;
	public List<MetadataArgument> arguments;
	
	public void addArgument(MetadataArgument arg) {
		if ( arguments == null ) {
			arguments = new ArrayList<MetadataArgument>();
		}
		arguments.add(arg);
	}
	
}
