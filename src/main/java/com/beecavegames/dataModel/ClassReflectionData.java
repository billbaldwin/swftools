package com.beecavegames.dataModel;

import java.util.List;
import java.util.Map;

import org.as3commons.reflect.Accessor;
import org.as3commons.reflect.Field;
import org.as3commons.reflect.Type;

public class ClassReflectionData {
	public Map<String,Type> types;
	public List<Field>		fields;
	public List<Accessor>	accessors;
}
