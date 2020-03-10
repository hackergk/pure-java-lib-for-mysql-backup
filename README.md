# pure-java-lib-for-mysql-backup
Backup library for MySQL using pure java.

This project initiated as experiment to backup MySQL databases without external tools but core java. 

To use library, import backup.* to program.
this values are must be set

	public void setConn(root, db,user name, password, port, default flag : true= use default (can occur error on wrong configurations), no=use this parameters to create connection){
	}


ex: for auto backup at system startup or close

				Backup ba = new Backup();
				if(ba.testPath()==1){
					if((ba.getProp("sql_db_auto").equals("true"))){ //auto backup enabled
						if(Integer.parseInt(String.valueOf(ba.getProp("sql_db_auto_method")))==1 
            ||Integer.parseInt(String.valueOf(ba.getProp("sql_db_auto_method")))==3) 
            //1 = enabled auto backup at start of program, 2 = enabled auto backup at end of program
            {
							try{
								ba.createAutoBackup(); //run auto backup
							}
							catch(Exception er){
								System.out.println("Automatic backup is found error in backup settings");
							}
						}
					}
         }
		
this library developped with different capabilities. i will update step by step. let me know if you need any additional details.
