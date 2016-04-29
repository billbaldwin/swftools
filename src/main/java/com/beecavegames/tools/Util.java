package com.beecavegames.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

public class Util {

    public static DefaultMutableTreeNode buildInheritanceTree(Map<String,String> superclassByClass ) {
    	
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    	
    	Map<String, DefaultMutableTreeNode> classToTreeNode = new HashMap<>();
    	for ( Map.Entry<String, String> entry : superclassByClass.entrySet() ) {
    		String className = entry.getKey();
    		String superclassName = entry.getValue();
    		
    		if ( classToTreeNode.containsKey(className) ) {
    			// we've already encountered this class.
    			continue;
    		}
    		DefaultMutableTreeNode node = new DefaultMutableTreeNode(className);
    		classToTreeNode.put(className, node);
    		if ( classToTreeNode.containsKey(superclassName) ) {
    			// Parent already in tree.
    			DefaultMutableTreeNode parent = classToTreeNode.get(superclassName);
//    			checkExisting(parent.children(), className);
    			parent.add(node);
    		}
    		else {
    			// Parent not in tree.
    			DefaultMutableTreeNode parent = null;
    			List<String> chain = new ArrayList<>();
    			do {
        			className = superclassName;
        			superclassName = superclassByClass.get(superclassName);
        			chain.add(className);
    				
        			parent = classToTreeNode.get(superclassName);
    			} while ( superclassName != null && (parent == null) );

    			if ( parent == null ) {
    				parent = root;
    			}
    			for ( int c = chain.size()-1; c >= 0; c-- ) {
    				String cls = chain.get(c);
        			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(cls);
        			classToTreeNode.put(cls, newNode);
//        			checkExisting(parent.children(), cls);
        			parent.add(newNode);
        			parent = newNode;
    			}
    			parent.add(node);
    		}
    	}
    	return root;
    }
	
}
