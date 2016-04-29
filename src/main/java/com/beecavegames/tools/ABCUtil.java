package com.beecavegames.tools;

import tv.porst.swfretools.parser.structures.AS3Data;
import tv.porst.swfretools.parser.structures.MultinameInfo;
import tv.porst.swfretools.utils.ActionScript3Helpers;
import tv.porst.swfretools.utils.ResolverException;

public class ABCUtil {

    public static String resolveMultiname(AS3Data abc, int index, boolean full) throws ResolverException {
    	String result;
    	if ( index == 0 ) {
    		result = "*";
    	}
    	else {
    		index--;
			MultinameInfo multinameInfo = abc.getConstantPool().getMultinames().get(index);
	    	String[] array = ActionScript3Helpers.resolveMultiname(multinameInfo, abc.getConstantPool());
	    	if ( array.length > 0 ) {
	    		if ( full ) {
	    			int len = array.length;
	    			result = resolveTypeName(array[len-2], array[len-1]);
	    			len -= 2;
	    			// This loop is to handle nested Vectors
    				while ( len > 0 ) {
    					String outer = resolveTypeName(array[len-2], array[len-1]);
	    				len -= 2;
	    				result = String.format("%s.<%s>", outer, result);
	    			}
	    		}
	    		else {
	    			result = array[array.length-1];
	    		}
	    	}
	    	else {
	    		result = "";
	    	}
    	}
    	// REMOVE "__AS3__.vec::" namespace on Vector.<> 
    	result = result.replace("__AS3__.vec::", "");
    	return result;
    }

    public static String resolveTypeName(String packageName, String typeName) {
    	String result;
    	if ( packageName.length() != 0 ) {
    		result = packageName + "::" + typeName;
    	}
    	else {
    		result = typeName;
    	}
    	return result;
    }
}
