package backup;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;

/**
 * @author hackerGK
 */
public class Options extends JDialog {

	boolean database_editable = true;

	public Options(Frame owner) {
		super(owner);
		initComponents();
	}

	public Options(Dialog owner) {
		super(owner);
		initComponents();
		setVisible(true);
	}

	public Options(Backup b) {
		initComponents();
		setVisible(true);
		database_editable = b.database_editable;
		System.out.println(database_editable);
		textField3.setEnabled(database_editable);
	}	
	
	private void closeActionPerformed(ActionEvent e) {
		dispose();
	}

	private void saveActionPerformed(ActionEvent e) {
		setVlas(textField1.getText(),textField2.getText(),textField3.getText(),textField4.getText(),textField5.getText(),textField7.getText());
		JOptionPane.showMessageDialog(this,"Settings saved successfully\n\n(for advaced configurations, contact system administrator)","Saved !",JOptionPane.INFORMATION_MESSAGE);
	}

	private void setdActionPerformed(ActionEvent e) {
		textField1.setText("localhost");
		textField2.setText("3306");
		textField3.setText("database");
		textField4.setText("root");
		textField5.setText("");
		textField7.setText("");	
		JOptionPane.showMessageDialog(this,"Default Settings loaded !\nSaving this settings can occur error in backup system if they not corrected properly\nif you not familier with this settings, please close this window without saving settings\n\n(for advaced configurations, contact system administrator)","Saved !",JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void setVlas(String root, String port, String name, String user, String pass, String path){
		Prop.set("sql_db_root",root);
		Prop.set("sql_db_port",port);
		Prop.set("sql_db_name",name);
		Prop.set("sql_db_user",user);
		Prop.set("sql_db_pass",pass);
		Prop.set("sql_backup_path",path);
			
		if(enAutomatic.isSelected()==true){
			Prop.set("sql_db_auto","true");
			if(rb1.isSelected())
				Prop.set("sql_db_auto_method","1");
			else if(rb2.isSelected())
				Prop.set("sql_db_auto_method","2");
			else if(rb3.isSelected())
				Prop.set("sql_db_auto_method","3");				
		}
		else{
			Prop.set("sql_db_auto","false");
		}
	}

	private void enAutomaticItemStateChanged(ItemEvent e) {
		if(enAutomatic.isSelected()==true){
			rb1.setEnabled(true);
			rb2.setEnabled(true);
			rb3.setEnabled(true);
		}
		else{
			rb1.setEnabled(false);
			rb2.setEnabled(false);
			rb3.setEnabled(false);
		}
	}	
	
	private void b2ActionPerformed(ActionEvent e) {
		try{
		JFileChooser fchooser = new JFileChooser(String.valueOf(Prop.get("sql_backup_path")));
		fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fchooser.showSaveDialog(this);

		File fi = fchooser.getSelectedFile();
		String fileroot = fi.getAbsolutePath();	

		if(fileroot != null){
				if(!fi.canWrite()){
				JOptionPane.showMessageDialog(this,"This path is write protected. \nPleace select another place to backup.","Error in path",JOptionPane.ERROR_MESSAGE);
				}
				else{
					textField7.setText(fileroot);
				}
		}
		else{
			JOptionPane.showMessageDialog(this,"Select correct folder","Enter correct folder name",JOptionPane.ERROR_MESSAGE);
		}

		}
		catch(NullPointerException er){
			JOptionPane.showMessageDialog(this,"Select folder to backup");
		}
		catch(Exception er){
			JOptionPane.showMessageDialog(this,er.toString());
		}
	}

	public String showCommenFileDialog( javax.swing.filechooser.FileFilter filter/*file filter for filter files*/, int type){
	String filepath = null;
			try{
				JFileChooser fchooser = new JFileChooser();
				if(type < 0 || type > 3)
					throw new IllegalArgumentException("Enter Correct File Selection Type");
				else
					fchooser.setFileSelectionMode(type);
				fchooser.setAcceptAllFileFilterUsed(false);
				if(filter != null)
					fchooser.setFileFilter(filter);
				fchooser.showOpenDialog(null);
				File fi = fchooser.getSelectedFile();
				String fileroot = fi.getAbsolutePath();
				if(fileroot != null){
					if(!fi.canRead()){
						JOptionPane.showMessageDialog(null,"This file is corrupted. \nPleace select good file","Error in file",JOptionPane.ERROR_MESSAGE);
						return null;
					}
					else{
						if(fi.length()==0||fi.length()<0){
						JOptionPane.showMessageDialog(null,"This file is Empty. \nPleace select good healthy file","Error in file",JOptionPane.ERROR_MESSAGE);
						return null;
						}
						else{
							filepath = fileroot;
						}
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"Wrong name to file","Enter correct file name",JOptionPane.ERROR_MESSAGE);
					return null;
				}
			}
			catch(NullPointerException er){
				JOptionPane.showMessageDialog(null,"Please select good healthy file");
				return null;
			}
			catch(Exception er){
				JOptionPane.showMessageDialog(null,er.toString());
				return null;
			}
			return filepath;
	}
	
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		
		String root1 = String.valueOf(Prop.get("sql_db_root"));
		String port1 = String.valueOf(Prop.get("sql_db_port"));
		String name1 = String.valueOf(Prop.get("sql_db_name"));
		String user1 = String.valueOf(Prop.get("sql_db_user"));
		String pass1 = String.valueOf(Prop.get("sql_db_pass"));
		String auto = String.valueOf(Prop.get("sql_db_auto"));
		String meth1 = String.valueOf(Prop.get("sql_db_auto_method"));
		
		if(root1==null)
			root1 = "";
		if(port1==null)
			port1 = "";
		if(name1==null)
			name1 = "";
		if(user1==null)
			user1 = "";
		if(pass1==null)
			pass1 = "";
		if(auto==null)
			auto = "false";
			
		textField1 = new JTextField(root1);
		textField2 = new JTextField(port1);
		textField3 = new JTextField(name1);
		textField4 = new JTextField(user1);
		textField5 = new JPasswordField(pass1);
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();
		panel4 = new JPanel();
		rb1 = new JRadioButton();
		rb2 = new JRadioButton();
		rb3 = new JRadioButton();
		enAutomatic = new JCheckBox();
		panel3 = new JPanel();		close = new JButton();
		save = new JButton();
		setd = new JButton();
		textField7 = new JTextField();
		panel2 = new JPanel();
		label7 = new JLabel();
		textField7 = new JTextField(String.valueOf(Prop.get("sql_backup_path")));
		b2 = new JButton();

		//======== this ========
		setTitle("Backup Settings ||");
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//======== panel1 ========
		{
			panel1.setBorder(new TitledBorder(null, "Settings", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
			panel1.setLayout(null);
			panel1.add(textField1);
			textField1.setBounds(190, 20, 205, 20);
			panel1.add(textField2);
			textField2.setBounds(190, 45, 205, textField2.getPreferredSize().height);
			panel1.add(textField3);
			textField3.setBounds(190, 70, 205, 20);
			panel1.add(textField4);
			textField4.setBounds(190, 95, 205, 20);
			panel1.add(textField5);
			textField5.setBounds(190, 120, 205, 20);

			//---- label1 ----
			label1.setText("Database Root :");
			panel1.add(label1);
			label1.setBounds(15, 20, 165, label1.getPreferredSize().height);

			//---- label2 ----
			label2.setText("Database Port :");
			panel1.add(label2);
			label2.setBounds(15, 45, 165, label2.getPreferredSize().height);

			//---- label3 ----
			label3.setText("Database Name :");
			panel1.add(label3);
			label3.setBounds(15, 70, 165, label3.getPreferredSize().height);

			//---- label4 ----
			label4.setText("Database User :");
			panel1.add(label4);
			label4.setBounds(15, 95, 165, label4.getPreferredSize().height);

			//---- label5 ----
			label5.setText("Database Password :");
			panel1.add(label5);
			label5.setBounds(15, 120, 165, label5.getPreferredSize().height);


			//======== panel2 ========
			{
				panel2.setBorder(new TitledBorder(null, "Advanced Settings", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
				panel2.setLayout(null);

				//---- label7 ----
				label7.setText("Backup Default path :");
				panel2.add(label7);
				label7.setBounds(10, 15, 110, 17);

				//---- textField7 ----
				textField7.setEnabled(false);
				panel2.add(textField7);
				textField7.setBounds(125, 15, 200, 20);

				//---- b2 ----
				b2.setText("text");
				b2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						b2ActionPerformed(e);
					}
				});
				panel2.add(b2);
				b2.setBounds(330, 15, 20, 23);

				//======== panel4 ========
				{
					panel4.setBorder(new TitledBorder("Auto Backup Settings ||"));
					panel4.setLayout(null);

					//---- rb1 ----
					rb1.setText("Backup when starting system");
					rb1.setSelected(true);
					rb1.setEnabled(false);
					panel4.add(rb1);
					rb1.setBounds(new Rectangle(new Point(45, 40), rb1.getPreferredSize()));

					//---- rb2 ----
					rb2.setText("Backup when closing system");
					rb2.setEnabled(false);
					panel4.add(rb2);
					rb2.setBounds(new Rectangle(new Point(45, 60), rb2.getPreferredSize()));

					//---- rb3 ----
					rb3.setText("Backup when starting and closing system");
					rb3.setEnabled(false);
					panel4.add(rb3);
					rb3.setBounds(new Rectangle(new Point(45, 80), rb3.getPreferredSize()));

					//---- enAutomatic ----
					enAutomatic.setText("Enable Automatic Backup System");
					enAutomatic.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent e) {
							enAutomaticItemStateChanged(e);
						}
					});
					panel4.add(enAutomatic);
					enAutomatic.setBounds(new Rectangle(new Point(15, 20), enAutomatic.getPreferredSize()));

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < panel4.getComponentCount(); i++) {
							Rectangle bounds = panel4.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = panel4.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						panel4.setMinimumSize(preferredSize);
						panel4.setPreferredSize(preferredSize);
					}
				}
				panel2.add(panel4);
				panel4.setBounds(10, 40, 365, 110);
				{ // compute preferred size
					Dimension preferredSize = new Dimension();
					for(int i = 0; i < panel2.getComponentCount(); i++) {
						Rectangle bounds = panel2.getComponent(i).getBounds();
						preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
						preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
					}
					Insets insets = panel2.getInsets();
					preferredSize.width += insets.right;
					preferredSize.height += insets.bottom;
					panel2.setMinimumSize(preferredSize);
					panel2.setPreferredSize(preferredSize);
				}
			}
			panel1.add(panel2);
			panel2.setBounds(10, 145, 385, 155);

			//======== panel3 ========
			{
				panel3.setBorder(new TitledBorder("Actions ||"));
				panel3.setLayout(null);

				//---- close ----
				close.setText("Close");
				close.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						closeActionPerformed(e);
					}
				});
				panel3.add(close);
				close.setBounds(280, 15, 95, close.getPreferredSize().height);

				//---- save ----
				save.setText("Save");
				save.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveActionPerformed(e);
					}
				});
				panel3.add(save);
				save.setBounds(185, 15, 90, save.getPreferredSize().height);

				//---- setd ----
				setd.setText("Set Default Settings");
				setd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setdActionPerformed(e);
					}
				});
				panel3.add(setd);
				setd.setBounds(10, 15, 135, setd.getPreferredSize().height);

				{ // compute preferred size
					Dimension preferredSize = new Dimension();
					for(int i = 0; i < panel3.getComponentCount(); i++) {
						Rectangle bounds = panel3.getComponent(i).getBounds();
						preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
						preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
					}
					Insets insets = panel3.getInsets();
					preferredSize.width += insets.right;
					preferredSize.height += insets.bottom;
					panel3.setMinimumSize(preferredSize);
					panel3.setPreferredSize(preferredSize);
				}
			}
			panel1.add(panel3);
			panel3.setBounds(10, 300, 385, 45);
			{ // compute preferred size
				Dimension preferredSize = new Dimension();
				for(int i = 0; i < panel1.getComponentCount(); i++) {
					Rectangle bounds = panel1.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
				}
				Insets insets = panel1.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				panel1.setMinimumSize(preferredSize);
				panel1.setPreferredSize(preferredSize);
			}
		}
		contentPane.add(panel1);
		panel1.setBounds(5, 0, 400, 350);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = contentPane.getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			contentPane.setMinimumSize(preferredSize);
			contentPane.setPreferredSize(preferredSize);
		}
		pack();
		setLocationRelativeTo(getOwner());

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(rb1);
		buttonGroup1.add(rb2);
		buttonGroup1.add(rb3);
		if(auto.equals("true")){
			enAutomatic.setSelected(true);
			rb1.setEnabled(true);
			rb2.setEnabled(true);
			rb3.setEnabled(true);
		}
		else{
			enAutomatic.setSelected(false);
		}
		
		try{
			int meth = Integer.parseInt(meth1);
			if(meth==3)
				rb3.setSelected(true);
			else if(meth==2)
				rb2.setSelected(true);
			else
				rb1.setSelected(true);			
		}catch(NumberFormatException er){
			rb1.setSelected(true);
			//System.out.println(er.toString());
		}		
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	private JPasswordField textField5;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JButton close;
	private JButton save;
	private JButton setd;
	private JPanel panel2;
	private JLabel label7;
	private JTextField textField7;
	private JButton b2;
	private JPanel panel4;
	private JRadioButton rb1;
	private JRadioButton rb2;
	private JRadioButton rb3;
	private JCheckBox enAutomatic;
	private JPanel panel3;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
