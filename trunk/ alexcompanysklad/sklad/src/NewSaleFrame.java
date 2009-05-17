import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;





class NewSaleFrame extends JPanel
{
	private JLabel okrLabel;
	private JComboBox okrCombo;
	private JComboBox clientCombo;
	private JComboBox skladCombo;
	private NewClientDialog newClient=null;
	public NewSaleFrame()
	{
//		setTitle("Ввод накладной");
//		setMaximizable(true);
//		setClosable(true);
//		setResizable(true);
//		setExtendedState(Frame.MAXIMIZED_BOTH);
/*		Connection cn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn = DriverManager.getConnection("jdbc:oracle:thin:@91.210.177.35:1521:XE", "sklad", "sklad");
		}
		catch (Exception e) { e.printStackTrace();}
		Statement st = null;
		ResultSet rs = null;
*/
		setLayout(null);
		JButton saveButton = new JButton("Сохранить");
		JButton cancelButton = new JButton("Отмена");
		JButton barcodeButton = new JButton("Штрих-код(F1,+)");
		JButton listButton = new JButton("Выбрать из списка");
		JButton printButton = new JButton("Печать");
		JLabel skladLabel = new JLabel("Склад:");
		JLabel clientLabel = new JLabel("Клиент:");
		okrLabel = new JLabel("Округление:");
		okrCombo = new JComboBox();
		okrCombo.addItem("Без округления");
		okrCombo.addItem("До 1");
		okrCombo.addItem("До 0,1");
		okrCombo.addItem("До 0,01");
		JCheckBox editableCheck = new JCheckBox("Редактировать в ячейке");
		skladCombo = new JComboBox();
		clientCombo = new JComboBox();
		clientCombo.setEditable(true);
		ResultSet rs = DataSet.QueryExec("select name from sklad order by name");
		try{
			rs.next();
			while (!rs.isAfterLast()){
				skladCombo.addItem(rs.getString("name"));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		rs = DataSet.QueryExec("select rtrim(name) from client where type in (1,2) order by name");
		try { 
			rs.next();
			while (!rs.isAfterLast()){
				clientCombo.addItem(rs.getString("rtrim(name)"));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}

		rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'");
		try {
			rs.next();
			if (rs.getInt(1)==1){
				okrLabel.setVisible(false);
				okrCombo.setVisible(false);
			}
		}
		catch (Exception e) { e.printStackTrace();}

		realTableModel model = new realTableModel((String)clientCombo.getSelectedItem(),(String)skladCombo.getSelectedItem(),0);
		JTable naklTable=new JTable(model);
		naklTable.getColumnModel().getColumn(0).setMaxWidth(30);
		naklTable.getColumnModel().getColumn(1).setMaxWidth(455);
		naklTable.getColumnModel().getColumn(2).setMaxWidth(50);
		naklTable.getColumnModel().getColumn(3).setMaxWidth(71);
		naklTable.getColumnModel().getColumn(4).setMaxWidth(96);
		naklTable.getColumnModel().getColumn(5).setMaxWidth(58);
		JScrollPane ScrollTable=new JScrollPane(naklTable);

		saveButton.setBounds(34, 495, 104, 22);
		cancelButton.setBounds(159, 495, 104, 22);
		barcodeButton.setBounds(285, 495, 150, 22);
		listButton.setBounds(455, 495, 163, 22);
		printButton.setBounds(638, 495, 104, 22);
		skladLabel.setBounds(10, 28, 58, 22);
		skladCombo.setBounds(79, 28, 207, 22);
		clientLabel.setBounds(10, 58, 61, 22);
		clientCombo.setBounds(79, 58, 207, 22);
		ScrollTable.setBounds(6, 89, 769, 335);
		okrLabel.setBounds(457, 28, 86, 22);
		okrCombo.setBounds(555, 28, 207, 22);
		editableCheck.setBounds(555, 58, 207, 22);
//Задаем слушателей
		clientCombo.addActionListener(new ClientChoose());
		
//Добавляем элементы на форму
		add(saveButton);
		add(cancelButton);
		add(barcodeButton);
		add(listButton);
		add(printButton);
		add(skladLabel);
		add(skladCombo);
		add(clientLabel);
		add(clientCombo);
		add(ScrollTable);
		add(okrLabel);
		add(okrCombo);
		add(editableCheck);
		
	}
	private class ClientChoose implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if (checkClient()){
			ResultSet rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'");
			try {
				rs.next();
				if (rs.getInt(1)==1){
					okrLabel.setVisible(false);
					okrCombo.setVisible(false);
				}else{
					okrLabel.setVisible(true);
					okrCombo.setVisible(true);
				}
					
			}
			catch (Exception e) { e.printStackTrace();}}
			else{
				ResultSet rs;
				if (newClient==null)
					newClient=new NewClientDialog();
				newClient.setClient(((String)clientCombo.getSelectedItem()).trim());
				if (newClient.showDialog(NewSaleFrame.this, "Ввод нового клиента")){
					clientCombo.removeAllItems();
					rs = DataSet.QueryExec("select rtrim(name) from client where type in (1,2) order by name");
					try { 
						rs.next();
						while (!rs.isAfterLast()){
							clientCombo.addItem(rs.getString("rtrim(name)"));
							rs.next();
						}
					}
					catch (Exception e) { e.printStackTrace();}
					clientCombo.setSelectedItem(newClient.getClient());
					rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'");
					try {
						rs.next();
						if (rs.getInt(1)==1){
							okrLabel.setVisible(false);
							okrCombo.setVisible(false);
						}
					}
					catch (Exception e) { e.printStackTrace();}
					
				}else{
					clientCombo.setSelectedIndex(0);
					rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'");
					try {
						rs.next();
						if (rs.getInt(1)==1){
							okrLabel.setVisible(false);
							okrCombo.setVisible(false);
						}
					}
					catch (Exception e) { e.printStackTrace();}

				}
			}
			
		}
		private boolean checkClient(){
			boolean ret=false;
			ResultSet rs=DataSet.QueryExec("Select count(*) from client where name like '%"+(String)clientCombo.getSelectedItem()+"%'");
			try { 
				rs.next();
				if (rs.getInt(1)>0){
					ret=true;
				}
				rs.close();
			}
			catch (Exception e) { }
			return ret;

		}
	}
	
}

	
