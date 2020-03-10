package backup;

import java.io.*;
import javax.swing.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.*;

public class Back {
  
  private static final String DATE_FORMAT_NOW = "yyyy-MM-ddHH.mm.ss";
  public static int sChunk = 8192;
  byte[] buffer = new byte[sChunk];
  /**backup restoring method*/
  public void restore(String filename, Statement stmt){
 	try{
		File f = new File(filename);
		if(f.exists()){
			BufferedReader buff = new BufferedReader(new FileReader(f));
			String line = buff.readLine();
			String ss = "";
			while(line !=null){
				if(line.indexOf("/*") == -1 && line.indexOf("--") == -1 && line.indexOf("#") == -1 ) ss += line +"";
				line = buff.readLine(); 
			}
			//System.out.println(ss);
			StringTokenizer st = new StringTokenizer(ss,";"); 
			String tt = "";
			while((tt = st.nextToken())!=null){
				stmt.executeUpdate(tt);
			} 
		}else{
			System.out.println("Restore File not found !!!");
			JOptionPane.showMessageDialog(null,"Restore file not found, check restore file in selected path","File Not Found",JOptionPane.ERROR_MESSAGE);
		}
	}
	 catch(SQLException er){
		System.out.println("SQL ERROR : "+er);
	}catch(java.util.NoSuchElementException er){
		//this occuring in restore, but im ignoring it. bcz, restoration is success with or without this error
	} catch(Exception er){ 
		System.out.println("ERROR : "+er.toString());
		//er.printStackTrace();
	 } 
  }

  /**backup method*/
    public String backup(String path,String filename, String desc ,String ext, String db, int stat, Connection con){
	String retname = null;
	try{
		doit(filename,con,con.createStatement(),db);
	if(stat==0){ //create backup tmp file and copy it to real backup file
		File inputFile = new File(filename);
		String dateget = now();
		String newfilename = path+File.separator+desc+dateget+ext;

		File outputFile = new File(newfilename);
	    FileInputStream in = new FileInputStream(inputFile);
	    FileOutputStream out = new FileOutputStream(outputFile);
		BufferedOutputStream buffo = new BufferedOutputStream(out);
			int length;
			while ((length = in.read(buffer, 0, sChunk)) != -1)
				buffo.write(buffer, 0, length);
		buffo.flush();
	    in.close();
	    buffo.close();
		retname = newfilename;
	}
	else if(stat==1){ //create backup tmp file only
		retname= null;
	}
	}
	catch(Exception er){
		er.printStackTrace();
		JOptionPane.showMessageDialog(null,"Error occured on backup creating the data base \n Error can be : \n 1. Currupted Media \n 2. Unaccessible media\n\nCreate backup on another storage location","Error",JOptionPane.ERROR_MESSAGE);
		return null;
	}
	return retname;
  }
 
	public void doit(String filename,Connection con, Statement stmt,String db){ //temp path+filename, connection, statement
	String sss = "\n-- Backup data file for databse : "+db+" ||\n\n";
		try {
			DatabaseMetaData dbmd = con.getMetaData();
			String[] types = {"TABLE"};
			ResultSet resultSet = dbmd.getTables(null, null, "%", types);
			while (resultSet.next()) {
				String tableName = resultSet.getString(3);
				sss += "\n-- table structure for table `"+tableName+"` \n\n";
				sss += "DROP TABLE IF EXISTS `"+tableName+"`;\nCREATE TABLE `"+tableName+"` (";
				String temp = "select * from `"+tableName+"`";
				ResultSet rr = stmt.executeQuery(temp);
				ResultSetMetaData md = rr.getMetaData( );
				int count = md.getColumnCount( );
				for (int i=1; i<=count; i++) {
					String colName = md.getColumnName(i);						String nullo = ((md.isNullable(i))==0)?"NOT NULL":"";
					String dtype = DTypes.getType(md.getColumnType(i));			String com = (i==count)?"":",";
					String size = (md.getColumnType(i)==8 || md.getColumnType(i)==7 || md.getColumnType(i)==91|| md.getColumnType(i)==92 || md.getColumnType(i)==93)?"":"("+String.valueOf(md.getColumnDisplaySize(i))+")";
					String autoinc = (md.isAutoIncrement(i))?"AUTO_INCREMENT, PRIMARY KEY (`"+colName+"`)":"";
					//String signed = (md.isSigned(i))?"":"";
					sss += "`"+colName+"` "+dtype+""+size+" "+nullo+" "+autoinc+""+com;
				}
				sss += ");";
			if(rr.next()){
					sss+= "\nINSERT INTO `"+tableName+"` VALUES ";
				do {
					sss+="(";
					for (int i=1; i<=count; i++) {
						String val1 = "0000-00-00 00:00:00";
						String com = (i==count)?"":",";
						String dc = DTypes.getCom(md.getColumnType(i));
						//System.out.println(md.isSigned(i));
						try{
							val1 = rr.getString(i);
							val1 = val1.replaceAll("'","''");
							sss+=dc+val1+dc+com;}
						catch(Exception er){
							sss+=dc+val1+dc+com;
							continue;
						}
					}
					sss+=")";
					String co = (rr.isLast())?"":",\r";
					sss+=co;
				}while (rr.next( ));
					sss+=";";
				}else{ //result set empty
				}
			} 
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	try{
		FileWriter file = new FileWriter(filename,false); //existing data will be over written or erased and the new data will be added to the file
		BufferedWriter buff = new BufferedWriter(file);
		buff.write(sss);
		buff.flush();
		buff.close();
		file.close(); 
	}  	
	catch(Exception er){
		System.out.print(er);
	}			
	} 

	
    public String backup(String path,String filename, String desc ,String ext, String db, int stat, Connection con,String host, String port, String user, String pass){
	String retname = null;
	try{
	
		String ppp= "";
		if(pass.equals("")|| pass==null)
			ppp="";
		else
			ppp = " -p"+pass ;
		
		String ecmd= "mysqldump -h "+host+" -P "+port+" -u "+user+ppp+" "+db+" -r "+filename;
		Process pr = Runtime.getRuntime().exec(ecmd);
		int prc = pr.waitFor();
		if(prc==0){
			//ok
		}
		else{
			JOptionPane.showMessageDialog(null,"Error occured on backup creating the data base \n Error can be : \n 1. Currupted Media \n 2. Unaccessible media\n\nCreate backup on another storage location","Error",JOptionPane.ERROR_MESSAGE);
		}	
		//doit(filename,con,con.createStatement(),db);
	if(stat==0){ //create backup tmp file and copy it to real backup file
		File inputFile = new File(filename);
		String dateget = now();
		String newfilename = path+"\\"+desc+dateget+ext;

		File outputFile = new File(newfilename);
	    FileInputStream in = new FileInputStream(inputFile);
	    FileOutputStream out = new FileOutputStream(outputFile);
		BufferedOutputStream buffo = new BufferedOutputStream(out);
			int length;
			while ((length = in.read(buffer, 0, sChunk)) != -1)
				buffo.write(buffer, 0, length);
		buffo.flush();
	    in.close();
	    buffo.close();
		retname = newfilename;
	}
	else if(stat==1){ //create backup tmp file only
		retname= null;
	}
	}
	catch(Exception er){
		er.printStackTrace();
		JOptionPane.showMessageDialog(null,"Error occured on backup creating the data base \n Error can be : \n 1. Currupted Media \n 2. Unaccessible media\n\nCreate backup on another storage location","Error",JOptionPane.ERROR_MESSAGE);
		return null;
	}
	return retname;
  }
 	
	

  /**Reading current date and time*/
  public String now(){
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    return sdf.format(cal.getTime());
  }
  
}//end of class