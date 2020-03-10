/**
 * @(#)Prop.java
 *
 *
 * @author Kasun Bandara
 * @version 1.00 2010/3/7
 */
package backup;

import java.util.*;

public class Prop {

    public static Object set(String s, String s1){
    	HashMap hm = new Read().getMap();
    	Object o = hm.put(s,s1);
    	new Read().setMap(hm);
    	return o;
    }
    
    public static Object get(String s){
    	HashMap hm =  new Read().getMap();
    	return hm.get(s);
    }
        
    public static HashMap readAll(){
    	return new Read().getMap();
    }
    
    public static HashMap clearAll(){
    	HashMap hm = new Read().getMap();
    	HashMap old = new HashMap();
    	if(hm.size()<=0){
    		return null;
    	}
    	else{
    		old = hm;
    		hm.clear();
    		new Read().setMap(hm);
    	}
    	return old;
    }
    
    public static Object remove(String s){
    	HashMap hm = new Read().getMap();
    	Object o = hm.remove(s);
    	new Read().setMap(hm);
    	return o;    	
    }
    
    
}