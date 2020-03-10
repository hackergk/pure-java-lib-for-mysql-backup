package backup;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.plaf.metal.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.net.*;
import java.io.*;
import java.sql.*;


 public class Backup extends JInternalFrame implements ActionListener,Runnable{

	private String path="";
	private String s="";
 	private int clicked =0;
	private int error = 0;
 
	JPanel c1,c2,c3;
	JTabbedPane tp;
	JButton btnOk = new JButton("Ok");
	
	JLabel pro = new JLabel("Wait Until Finish....");
 
	JLabel lblBSetPath = new JLabel("Set The Path To Create Backup ||");
	JTextField txtBSetPath = new JTextField("");
	JButton btnBBrows = new JButton("Browse");
	JButton btnBBackup = new JButton("Backup");
	JProgressBar btnBBar = new JProgressBar();
	JProgressBar btnBBar2 = new JProgressBar();
	JLabel btnBTask = new JLabel("Task take some time depend on computer performence");
	JLabel btnBLabel = new JLabel("");
 
	JLabel lblRSetPath = new JLabel("Set The Backup File To ReStore ||");
	JTextField txtRSetPath = new JTextField("");
	JButton btnRBrows = new JButton("Browse");
	JButton btnRRestore = new JButton("Restore");
	JProgressBar btnRBar = new JProgressBar();
	JProgressBar btnRBar2 = new JProgressBar();
	JLabel lblRTask = new JLabel("Task take some time depend on computer performence");
	JLabel lblRLabel = new JLabel("");
	
	private Back back = new Back();
	
	KGlassPane glass = new KGlassPane();
 
	private Thread thread1;
	private Thread thread2;

	String DEFAULT_DIR = "";
	String FILE = "back.tmp";
	String PATH = "files"+File.separator;
	String BACKUP_EXT = ".bku";
	String DESC = "Backup";
	String NEW_PATH="";
	Statement STMT;
	Connection CONN;
	boolean database_editable = true;
	String name = null;
	
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu tasks = new JMenu("Tasks");
	JMenu about = new JMenu("About");
	JMenuItem close = new JMenuItem("Close");
	JMenuItem backup1 = new JMenuItem("Backup System");
	JMenuItem restore1 = new JMenuItem("Restore System");
	JMenuItem options = new JMenuItem("Reverse back to last state");
	JMenuItem opt = new JMenuItem("Options");
	JMenuItem about1 = new JMenuItem("About Backup Tool");
	
	/**Constructer method*/
	public Backup(){

	super("Backup ||",false,false,false,true);	
	tp = new JTabbedPane();
	
	close.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			dispose();
		}
	}
	);
	backup1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			tp.setSelectedIndex(0);
		}
	}
	);
	restore1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			tp.setSelectedIndex(1);
		}
	}
	);	
	about1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			URL imgl = getClass().getClassLoader().getResource ("backup/backup.jpg");
			JOptionPane.showMessageDialog(null,"<html>Backup Tool for SQL Servers V2.6<br>Designed by Kasun Bandara<br><br><a href=mailto:kasuninjava@gmail.com>kasuninjava@gmail.com</a>\n<html>","About",1,new ImageIcon(imgl));
		}
	}
	);		
	file.add(close);
	tasks.add(backup1);
	tasks.add(restore1);
	tasks.add(options);
	tasks.add(opt);
	
	about.add(about1);
	
	menuBar.add(file);
	menuBar.add(tasks);
	menuBar.add(about);
	
	setMenuBar(menuBar);

	c1 = (JPanel)getContentPane();
	c1.setLayout(null);
	c1.setBackground(new Color(238,246,255));

	c2 = new JPanel();
	c2.setLayout(null);
	c2.setBackground(new Color(238,246,255));
	c2.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(new Color(11,46,231), new Color(11,46,231)), ""));

	c3 = new JPanel();
	c3.setLayout(null);
	c3.setBackground(new Color(238,246,255));
	c3.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(new Color(11,46,231), new Color(11,46,231)), ""));

	glass.setLayout(null);
	
	btnBBar.setMaximum(1000);
	btnBBar.setMinimum(0);
	btnBBar.setForeground(new Color(102,204,0));
	btnBBar.setStringPainted(true);
	btnBBar.setBorder(null);
	
	btnBBar2.setMaximum(1000);
	btnBBar2.setMinimum(0);
	btnBBar2.setForeground(new Color(102,204,0));
	btnBBar2.setStringPainted(true);
	btnBBar2.setBorder(null);	

	btnRBar.setMaximum(1000);
	btnRBar.setMinimum(0);
	btnRBar.setForeground(new Color(102,204,0));
	btnRBar.setStringPainted(true);
	btnRBar.setBorder(null);

	btnRBar2.setMaximum(1000);
	btnRBar2.setMinimum(0);
	btnRBar2.setForeground(new Color(102,204,0));
	btnRBar2.setStringPainted(true);
	btnRBar2.setBorder(null);	

	c2.add(lblBSetPath);		c2.add(btnBBrows);
	c2.add(txtBSetPath);		c2.add(btnBBackup);
	c2.add(btnBBar);	c2.add(btnBTask);
	c2.add(btnBLabel);

	c3.add(lblRSetPath);		c3.add(lblRTask);
	c3.add(txtRSetPath); 	c3.add(btnRBrows);
	c3.add(btnRRestore);		c3.add(btnRBar);
	c3.add(lblRLabel);

	glass.add(btnBBar2);	glass.add(btnRBar2);	
	glass.add(pro);
	
	c1.add(btnOk);

	txtBSetPath.setText(checkPath());
	txtRSetPath.setText(checkPath());

	tp.setTabPlacement(JTabbedPane.TOP);

	tp.addTab("Backup Data ||",c2);
	tp.addTab("ReStore Data ||",c3);

	c1.add(tp);

	btnOk.addActionListener(this);
	btnBBrows.addActionListener(this);
	btnBBackup.addActionListener(this);
	btnRBrows.addActionListener(this);
	btnRRestore.addActionListener(this);
	options.addActionListener(this);
	opt.addActionListener(this);

	setbounds();

	setSize(400,370);
	setResizable(false);

	}
	/**Settings of bounds*/
	public void setbounds(){

	tp.setBounds(0,0,390,270);

	pro.setBounds(new Rectangle(new Point(120,70),pro.getPreferredSize()));
	
	btnRBar.setBounds(new Rectangle(new Point(20, 140), btnRBar.getPreferredSize()));
	btnBBar.setBounds(new Rectangle(new Point(20, 140), btnRBar.getPreferredSize()));

	btnRBar2.setBounds(new Rectangle(new Point(120, 100), btnRBar.getPreferredSize()));
	btnBBar2.setBounds(new Rectangle(new Point(120, 100), btnRBar.getPreferredSize()));	
	
	lblRSetPath.setBounds(20,20,300,30);
	txtRSetPath.setBounds(20,60,200,30);

	btnRBrows.setBounds(new Rectangle(new Point(230, 60), btnRBrows.getPreferredSize()));
	btnRRestore.setBounds(new Rectangle(new Point(230, 105), btnRRestore.getPreferredSize()));

	lblRTask.setBounds(20,170,350,30);
	lblRLabel.setBounds(20,210,350,30);	
	btnOk.setBounds(new Rectangle(new Point(220, 280), btnRRestore.getPreferredSize()));

	lblBSetPath.setBounds(20,20,300,30);
	txtBSetPath.setBounds(20,60,200,30);

	btnBBrows.setBounds(new Rectangle(new Point(230, 60), btnBBrows.getPreferredSize()));
	btnBBackup.setBounds(new Rectangle(new Point(230, 100), btnBBackup.getPreferredSize()));
	btnBTask.setBounds(new Rectangle(new Point(20, 170), btnBTask.getPreferredSize()));
	btnBLabel.setBounds(20, 210,350,30);
	}

	public void createAutoBackup() throws Exception{
		String name = back.backup(String.valueOf(Prop.get("sql_backup_path")),System.getProperty("user.dir")+ System.getProperty("file.separator") + PATH + FILE,DESC,BACKUP_EXT,String.valueOf(Prop.get("sql_db_name")),0,getConnection(String.valueOf(Prop.get("sql_db_root")));
		if(name!=null)
			new GZip(new File[]{new File(name)},name);
		File ff = new File(name);
		if(ff.exists())
			ff.delete();

	}

	public int testPath(){
		String p = String.valueOf(Prop.get("sql_backup_path"));
		File ff = new File(p);
		if(ff.exists() && ff.isDirectory() && ff.canWrite()){
			return 1;
		}
		else{
			JOptionPane.showMessageDialog(this,"Error default backup folder. error can be, \n1.Folder Not exists\n2.Given path isnt folder\n3.Folder haven't required writing permissions\n\ncontact system administrator","Error !",JOptionPane.ERROR_MESSAGE);
			return 0;
		}
	
	}
	
	public String checkPath(){
		String p = String.valueOf(Prop.get("sql_backup_path"));
		File ff = new File(p);
		if(ff.exists() && ff.isDirectory() && ff.canWrite()){
			txtBSetPath.setText(p);
			txtRSetPath.setText(p);
			return p;
		}
		else{
			//JOptionPane.showMessageDialog(this,"Error default backup folder. error can be, \n1.Folder Not exists\n2.Given path isnt folder\n3.Folder haven't required writing permissions\n\ncontact system administrator","Error !",JOptionPane.ERROR_MESSAGE);
			return "";
		}
	
	}
	
	public void setConn(String root,String db,String user, String pass, String port, boolean bool /*true=use default config, no= use this parameters to connection*/){
		try{
			database_editable = bool;
			if(bool){
				this.CONN = getConnection(String.valueOf(Prop.get("sql_db_root")),String.valueOf(Prop.get("sql_db_port")),String.valueOf(Prop.get("sql_db_name")),String.valueOf(Prop.get("sql_db_user")),String.valueOf(Prop.get("sql_db_pass")));
				STMT = CONN.createStatement();				
			}
			else{
				Prop.set("sql_db_name",db);
				Prop.set("sql_db_user",user);
				Prop.set("sql_db_pass",pass);
				Prop.set("sql_db_root",root);
				Prop.set("sql_db_port",port);
				this.CONN = getConnection(root,port,db,user,pass);
				STMT = CONN.createStatement();				
			}
		}catch(java.sql.SQLException er){
			System.out.println("SQL ERROR : "+er.toString());
		}
	}
	
	public void setDefaultDir(String s){
		DEFAULT_DIR = s;
		Prop.set("sql_backup_path",s);
	}
	
	public void setBackupExt(String s){
		BACKUP_EXT = s;
	}
	
	public void setDesc(String s){
		DESC = s;
	}
	
	public void actionPerformed(ActionEvent e){
	if(e.getSource()==btnOk){
		dispose();
	}
	else if(e.getSource()==btnBBrows){
		btnBTask.setForeground(new Color(1,28,172));
		btnBTask.setText("Task take some time depend on computer performence");
		btnBBar.setValue(0);
		btnBLabel.setForeground(new Color(1,28,172));
		window("backup");
	}
	else if(e.getSource()==btnRBrows){
		lblRTask.setForeground(new Color(1,28,172));
		lblRTask.setText("Task take some time depend on computer performence");
		btnRBar.setValue(0);
		lblRLabel.setForeground(new Color(1,28,172));
		window("");
	}
	else if(e.getSource()==btnBBackup){
		error= check(txtBSetPath);
		if(error==1){
			error = 0;
		}
		else{
			s = System.getProperty("user.dir")+ System.getProperty("file.separator") + PATH + FILE;
			thread1 = new Thread(this);
			clicked = 1;
			thread1.start();
		}
	}
	else if(e.getSource()==btnRRestore){
	error = check(txtRSetPath);
		if(error==1){
			error = 0;
		}
		else{
			s = System.getProperty("user.dir")+ System.getProperty("file.separator")+PATH;
			NEW_PATH = txtRSetPath.getText();
			thread2 = new Thread(this);
			clicked = 2;
			thread2.start();
		}
	}
	else if(e.getSource()==options){
	File file = new File(System.getProperty("user.dir")+ System.getProperty("file.separator")+PATH+"reverse.rev");
	if(file.exists()){
		int response = JOptionPane.showConfirmDialog(this,"Are you sure you want Reverse Backup \nto last state???","Reverse Backup",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(response==JOptionPane.NO_OPTION){	}
		else{
			s = System.getProperty("user.dir")+ System.getProperty("file.separator")+PATH;
			NEW_PATH = System.getProperty("user.dir")+ System.getProperty("file.separator")+PATH+"reverse.rev";
			thread2 = new Thread(this);
			clicked = 3;
			thread2.start();	
		}
	}
	else{
		JOptionPane.showMessageDialog(this,"system cannot access previous file. please restore \nsystem at least one time to reverse back","reverse",0);
	}
	}	
	else if(e.getSource()==opt){
		new Options(this);
	}		
	} 
	/**Method to desplay file chooser windows under selection*/
	public void window(String s){

	if(s.equals("backup")){
		try{
		JFileChooser fchooser = new JFileChooser(String.valueOf(Prop.get("sql_backup_path")));
		fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fchooser.showSaveDialog(this);

		File fi = fchooser.getSelectedFile();
		String fileroot = fi.getAbsolutePath();	

		if(fileroot != null){
				if(!fi.canWrite()){
				JOptionPane.showMessageDialog(this,"This path is write protected. \nPleace select another place to backup.","Access Denied",JOptionPane.ERROR_MESSAGE);
				}
				else{
					String s1 = "";
					s1 = System.getProperty("user.dir")+ System.getProperty("file.separator") + PATH + FILE;
					File file = new File(s1);
					btnBLabel.setText("Free Space : "+String.valueOf((fi.getFreeSpace()/1000))+" KB ");
					txtBSetPath.setText(fileroot);
				}
		}
		else{
			JOptionPane.showMessageDialog(this,"Wrong name to file","Enter correct file name",JOptionPane.ERROR_MESSAGE);
		}

		}
		catch(NullPointerException er){
			JOptionPane.showMessageDialog(this,"Select folder to backup");
		}
		catch(Exception er){
			JOptionPane.showMessageDialog(this,er.toString());
		}
	}
	else{
		try{

		JFileChooser fchooser = new JFileChooser(String.valueOf(Prop.get("sql_backup_path")));
		fchooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fchooser.setAcceptAllFileFilterUsed(false);
		fchooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(BACKUP_EXT)
			|| f.isDirectory();
		}	
		 public String getDescription() {
			return BACKUP_EXT+" \"backup files\"";
		}
		});
		
		fchooser.showOpenDialog(this);
		File fi = fchooser.getSelectedFile();
		String fileroot = fi.getAbsolutePath();

		if(fileroot != null){
			if(!fi.canRead()){
			JOptionPane.showMessageDialog(this,"This file is corrupted. \nPleace select good healthy file to backup.","EError in file",JOptionPane.ERROR_MESSAGE);
			}
			else{
				if(fi.length()==0||fi.length()<0){
				JOptionPane.showMessageDialog(this,"This file is Empty. \nPleace select good healthy file to backup.","Error in file",JOptionPane.ERROR_MESSAGE);
				}
				else{
					txtRSetPath.setText(fileroot);
					}
			}
		}
		else{
			JOptionPane.showMessageDialog(this,"Wrong name to file","Enter correct file name",JOptionPane.ERROR_MESSAGE);
		}
		}
		catch(NullPointerException er){
			JOptionPane.showMessageDialog(this,"Select backup file to backup");
		}
		catch(Exception er){
			JOptionPane.showMessageDialog(this,er.toString());
		}
	}

	}

	public int check(JTextField t){
	if(t.getText().trim().equals("")||t.getText().trim().length()<3){
		JOptionPane.showMessageDialog(this,"Path cannot be empty","Value",JOptionPane.ERROR_MESSAGE);
		error=1;
	}
	return error;

	}

	public void initwin(boolean bool){

	if(!bool){
		txtBSetPath.setText(checkPath());
		txtRSetPath.setText(checkPath());	
		btnOk.setEnabled(false);
		btnBBrows.setEnabled(false);
		btnBBackup.setEnabled(false);
		btnRBrows.setEnabled(false);
		btnRRestore.setEnabled(false);
	}
	else{
		txtBSetPath.setText(checkPath());
		txtRSetPath.setText(checkPath());
		btnOk.setEnabled(true);
		btnBBrows.setEnabled(true);
		btnBBackup.setEnabled(true);
		btnRBrows.setEnabled(true);
		btnRRestore.setEnabled(true);
		btnBLabel.setText("");
		lblRLabel.setText("");	
	getGlassPane().setVisible(false);		
	}

	}

	public String getProp(String s){
		return String.valueOf(Prop.get(s));
	}
	
	public Connection getConnection(String root, String port, String db , String user, String pass){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://"+root+":"+port+"/"+db,user, pass);
			return conn;
		}
		catch(ClassNotFoundException er){
			JOptionPane.showMessageDialog(null,"Error found ...\nDataBase Driver error (Invalid Drivers)\nUsers Cant login to system without database\n\nContact System Administrator","Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
		catch(Exception er){
			JOptionPane.showMessageDialog(null,"Error found ...\nDataBase Access error (Invalid Authentication)\nOr\nDataBase not found. Details are not be loaded \n\nUsers Cant login to system without database\n\nContact System Administrator","Error",JOptionPane.ERROR_MESSAGE); return null; 
		} 
	}	
	/**Backup method*/
	public void run(){
		String name = null;
		setGlassPane(glass);
		getGlassPane().setVisible(true);
		if(clicked==1){ //clicked backup
			btnBBar2.setVisible(true);
			btnRBar2.setVisible(false);
			for(int q = 0; q<=btnBBar.getMaximum();q+=5){
				try{
					btnBBar.setValue(q);
					btnBBar2.setValue(q);
					thread1.sleep(10);
					initwin(false);
					if(q<=300){btnBTask.setText("Initialising details...");}
					else if(q<=550){btnBTask.setText("collecting Information...");
						int response = JOptionPane.showConfirmDialog(this,"Are you sure you want Create Backup ???","Create Backup",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(response==JOptionPane.NO_OPTION){
							q = btnBBar.getMaximum();
							btnBBar.setValue(q);
							q = btnBBar2.getMaximum();
							btnBBar2.setValue(q);						
							initwin(true);
							btnBTask.setForeground(Color.red);
							btnBTask.setText("Backup Canceled !!!");
						}
						else{
							q = 555;
							}	
					}
					else if(q<=850){btnBTask.setText("finalizing Backup...");}
					else if(q==855){
						this.CONN = getConnection(String.valueOf(Prop.get("sql_db_root")),String.valueOf(Prop.get("sql_db_port")),String.valueOf(Prop.get("sql_db_name")),String.valueOf(Prop.get("sql_db_user")),String.valueOf(Prop.get("sql_db_pass")));
						STMT = CONN.createStatement();						
						name = back.backup(txtBSetPath.getText(),s,DESC,BACKUP_EXT,String.valueOf(Prop.get("sql_db_name")),0,CONN);
					}
					else if(q==1000){btnBTask.setText("finished");
					//System.out.println(name);
 					int response = JOptionPane.showConfirmDialog(this,"Are you want to compress Backup ???","Compress Backup",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(response==JOptionPane.NO_OPTION){		}
					else{
						if(name!=null)
							new GZip(new File[]{new File(name)},name);
						else
							JOptionPane.showMessageDialog(this,"Backup file not found !!!","Error !",JOptionPane.ERROR_MESSAGE);
						}
						initwin(true);
					}
					clicked =0;
				}
				catch(Exception er){
					JOptionPane.showMessageDialog(this,er.toString());
				}	
			}
		}
		else if(clicked==2){ //clicked restore
		btnRBar2.setVisible(true);
		btnBBar2.setVisible(false);
			for(int q = 0; q<=btnRBar.getMaximum();q+=5){
				try{
					btnRBar.setValue(q);
					btnRBar2.setValue(q);
					thread2.sleep(10);
					initwin(false);
					if(q<=300){lblRTask.setText("Initialising details...");}
					else if(q<=550){lblRTask.setText("collecting Information");
						int response = JOptionPane.showConfirmDialog(this,"Are you sure you want Re store Backup ???","Re store Backup",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(response==JOptionPane.NO_OPTION){
							q = btnRBar.getMaximum();
							btnRBar.setValue(q);
							q = btnRBar2.getMaximum();
							btnRBar2.setValue(q);						
							initwin(true);
							lblRTask.setForeground(Color.red);
							lblRTask.setText("Re storing Canceled !!!");
						}
						else{
							q=555;
						}
					}
					else if(q<=850){lblRTask.setText("finalizing Restoring...");}
					else if(q==855){
						this.CONN = getConnection(String.valueOf(Prop.get("sql_db_root")),String.valueOf(Prop.get("sql_db_port")),String.valueOf(Prop.get("sql_db_name")),String.valueOf(Prop.get("sql_db_user")),String.valueOf(Prop.get("sql_db_pass")));
						STMT = CONN.createStatement();						
						back.backup(null,null,null,null,String.valueOf(Prop.get("sql_db_name")),1,CONN);
						//back.backup(null,null,null,null,CONN,null,1);
						File fi = new File(s+FILE);
						File f2 = new File(s+"reverse.rev");
						if(f2.exists()){
							f2.delete();
						}
						fi.renameTo(new File(s+"reverse.rev"));
						//back.restore(s,txtRSetPath.getText(),FILE);
						back.restore(NEW_PATH,STMT); //file name to restore , stament to execute queries
					}
					else if(q==1000){lblRTask.setText("finished");	
					initwin(true);
					}
					clicked=0;
				}
				catch(Exception er){
					JOptionPane.showMessageDialog(this,er.toString());
				}
			}
		}
		else{
		btnRBar2.setVisible(true);
		btnBBar2.setVisible(false);
			for(int q = 0; q<=btnRBar.getMaximum();q+=5){
				try{
					btnRBar.setValue(q);
					btnRBar2.setValue(q);
					thread2.sleep(10);
					initwin(false);
					if(q<=300){lblRTask.setText("Initialising details...");}
					else if(q<=550){lblRTask.setText("collecting Information");
						int response = JOptionPane.showConfirmDialog(this,"Are you sure!!! \nDo you want Reverse Last Backup ???","Reverse Backup",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if(response==JOptionPane.NO_OPTION){
							q = btnRBar.getMaximum();
							btnRBar.setValue(q);
							q = btnRBar2.getMaximum();
							btnRBar2.setValue(q);						
							initwin(true);
							lblRTask.setForeground(Color.red);
							lblRTask.setText("Reverse Canceled !!!");
						}
						else{
							q=555;
						}
					}
					else if(q<=850){lblRTask.setText("finalizing Reversing...");}
					else if(q==855){
						this.CONN = getConnection(String.valueOf(Prop.get("sql_db_root")),String.valueOf(Prop.get("sql_db_port")),String.valueOf(Prop.get("sql_db_name")),String.valueOf(Prop.get("sql_db_user")),String.valueOf(Prop.get("sql_db_pass")));
						STMT = CONN.createStatement();						
						back.backup(null,null,null,null,String.valueOf(Prop.get("sql_db_name")),1,CONN);
					//	back.backup(null,null,null,null,null,null,1);
						File fi = new File(s+FILE);
						File f2 = new File(s+"reverse.rev");
						back.restore(NEW_PATH,STMT);
						if(f2.exists()){
							f2.delete();
						}
						fi.renameTo(new File(s+"reverse.rev"));
					}
					else if(q==1000){lblRTask.setText("finished");	
					initwin(true);
					}
					clicked=0;
				}
				catch(Exception er){
					JOptionPane.showMessageDialog(this,er.toString());
				}
			}
		}
	}

	public void windowDeactivated(WindowEvent w){}
	public void windowActivated(WindowEvent w){}
	public void windowDeiconified(WindowEvent w){}
	public void windowIconified(WindowEvent w){}
	public void windowClosed(WindowEvent w){} 
	public void windowOpened(WindowEvent w){}
	public void windowClosing(WindowEvent w){
  }


 }//end of class