/**
 * @(#)Read.java
 *
 *
 * @author 
 * @version 1.00 2010/3/7
 */
package backup;

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class Read {

	private HashMap hm = readMap();
	public static String path = System.getProperty("user.dir")+File.separator+"bin"+File.separator+"backup.prop";
	
    HashMap getMap(){
    	hm = readMap();
    	return hm;
    }
    
    Map setMap(Map m){
    	if(m != null && m instanceof HashMap){
    		HashMap old = hm;
    		hm = (HashMap)m;
    		saveMap();
    		return old;
    	}
    	return null;
    }
    
    private void saveMap(){
    	try{
    	  	ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
        	os.writeObject(hm);
        	os.flush();
        	os.close();  
    	}  	
        catch(Exception er){
        	System.out.print(er);
        }
    }
    
    private HashMap readMap(){
    	try{
    		checkFile();
	        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
	     	Object n = is.readObject();
	     //	System.out.println(n);
	     	if(n != null && n instanceof HashMap){
	        	hm = (HashMap)n;
	        //	System.out.println("not null and hashmap");
	     	}
	        else{
	        	hm = null;
	        }
	        is.close( );
    	}catch(Exception er){
        	System.out.print(er);
        }
       // System.out.println("read map "+hm);
        return hm;
    }
    
    private void checkFile() throws Exception{
    	File f = new File(path);
    	if(!f.exists()){
			JOptionPane.showMessageDialog(null,"Error in Program initializing settings...(default loaded)\nsettings will be change to defaul values... but System may not be function Properly..!!!!\n\nContact System Administrator \nRef.Code: backup.prop0013","Error in initializing settings...",JOptionPane.ERROR_MESSAGE);		
    		new FileWriter(path);
			HashMap hm1 = new HashMap();
			hm1.put("sql_bakup_exe","bin\\mysqldump.exe");
			hm1.put("sql_backup_path","");
			hm1.put("sql_db_port","3306");
			hm1.put("sql_db_root","localhost");
			hm1.put("sql_db_name","database");
			hm1.put("sql_db_user","root");
			hm1.put("sql_db_pass","");
			hm1.put("sql_db_auto","false");
			hm1.put("sql_db_auto_method","1");
    		setMap(hm1);
    	}
    		
    }
	
	public void checkInit(){
		try{
			checkFile();
		}
		catch(Exception er){
		
		}
	}
    
    
}