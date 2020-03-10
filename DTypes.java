package backup;

import java.util.*;

public class DTypes{

static HashMap<Integer,String> hm = new HashMap<Integer,String>();
static {
	hm.put(-7,"tinyint"); //also for bit
	hm.put(-5,"bigint");
	hm.put(-4,"blob");
	hm.put(-3,"tinyblob"); //also for medium blob , long blob
	hm.put(-2,"geometry"); // also for point,line string,polygon multi point,multi line string,multi polygon, geometry collection , binary
	hm.put(-1,"text"); //also for tinytext, medium text , long text
	hm.put(1,"char");
	hm.put(3,"decimal");
	hm.put(4,"int"); //also for medium int
	hm.put(5,"smallint");
	hm.put(7,"float");
	hm.put(8,"double");
	hm.put(12,"varchar");
	hm.put(91,"date"); //also for year
	hm.put(92,"time");
	hm.put(93,"datetime "); //also for current time stamp
}
public static String getType(int i){

	String ii = String.valueOf(i);
	return String.valueOf(hm.get(i));
}

public static String getCom(int i){
	switch(i){
		case -7:
			return "";
			
		case -5:
			return "";
			
		case -1:
			return "'";
			
		case 3:
			return "";
			
		case 4:
			return "";
			
		case 5:
			return "";
			
		case 7:
			return "";
			
		case 8:
			return "";
			
		case 12:
			return "'";
			
		case 91:
			return "'";
			
		case 92:
			return "'";
			
		case 93:
			return "'";
				
		default :
			return "'";
	}
}

}