import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.*;
//import javax.swing.JComboBox.KeySelectionManager;





class NewSaleFrame extends JPanel
{
	private JLabel okrLabel;
	private JComboBox okrCombo;
	private AutoComplete clientCombo;
	private JComboBoxFire skladCombo;
	private NewClientDialog newClient=null;
	private JComboBox priceCombo;
	private JLabel priceLabel;
	private realTableModel model;
	private static InputCountTovar formInput = null;
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
		priceLabel = new JLabel("Прайс:");
		okrLabel = new JLabel("Округление:");
		okrCombo = new JComboBox();
		okrCombo.addItem("Без округления");
		okrCombo.addItem("До 1");
		okrCombo.addItem("До 0,1");
		okrCombo.addItem("До 0,01");
		JCheckBox editableCheck = new JCheckBox("Редактировать в ячейке");
		skladCombo = new JComboBoxFire();
		clientCombo = new AutoComplete();
		clientCombo.setEditable(true);
		priceCombo=new JComboBox();
		ResultSet rs = DataSet.QueryExec("select name from sklad order by name");
		try{
			rs.next();
			while (!rs.isAfterLast()){
				skladCombo.addItem(rs.getString("name"));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		rs = DataSet.QueryExec("select trim(name) from client where type in (1,2) order by name");
		try { 
			rs.next();
			while (!rs.isAfterLast()){
				clientCombo.addItem(rs.getString("trim(name)"));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		rs = DataSet.QueryExec("select trim(name) from type_price order by name");
		try { 
			rs.next();
			while (!rs.isAfterLast()){
				priceCombo.addItem(rs.getString("trim(name)"));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		priceCombo.setSelectedItem("Оптовый");

		model = new realTableModel((String)clientCombo.getSelectedItem(),(String)skladCombo.getSelectedItem(),0);
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
		priceLabel.setBounds(457, 1, 86, 22);
		priceCombo.setBounds(555, 1, 207, 22);
		okrLabel.setBounds(457, 28, 86, 22);
		okrCombo.setBounds(555, 28, 207, 22);
		editableCheck.setBounds(555, 58, 207, 22);
//Задаем слушателей
		clientCombo.addActionListener(new ClientChoose());
		barcodeButton.addActionListener(new barcode());
		skladCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				model.setSklad((String)skladCombo.getSelectedItem());
			}
		});

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
		add(priceLabel);
		add(priceCombo);
		add(editableCheck);
		
		clientCombo.fireActionEvent();		
		skladCombo.fireActionEvent();
		
	}
	private class ClientChoose implements ActionListener{
		public void actionPerformed(ActionEvent event){
			model.setIndDiscount(0);
			if (checkClient()){
			ResultSet rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'");
			
			try {
				rs.next();
				if (rs.getInt(1)==1){
					okrLabel.setVisible(false);
					okrCombo.setVisible(false);
					priceLabel.setVisible(false);
					priceCombo.setVisible(false);
					rs.close();
					rs=DataSet.QueryExec("select count(*) from discount where id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') and id_skl=(select id_skl from sklad where name='"+(String)skladCombo.getSelectedItem()+"')");
					rs.next();
					if (rs.getInt(1)>0){
						rs.close();
						rs=DataSet.QueryExec("select disc from discount where id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') and id_skl=(select id_skl from sklad where name='"+(String)skladCombo.getSelectedItem()+"')");
						rs.next();
						model.setIndDiscount(rs.getInt(1));}
				}else{
					model.setIndDiscount(0);
					okrLabel.setVisible(true);
					okrCombo.setVisible(true);
					priceCombo.setVisible(true);
					priceLabel.setVisible(true);
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
							priceLabel.setVisible(false);
							priceCombo.setVisible(false);
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
							priceLabel.setVisible(false);
							priceCombo.setVisible(false);
						}
					}
					catch (Exception e) { e.printStackTrace();}

				}
			}
//			ComboBoxEditor edit=clientCombo.getEditor();
//			edit.selectAll();
			clientCombo.getEditor().selectAll();
			model.setClient((String)clientCombo.getSelectedItem());
		}
		private boolean checkClient(){
			boolean ret=false;
			ResultSet rs=DataSet.QueryExec("Select count(*) from client where name = '"+(String)clientCombo.getSelectedItem()+"'");
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
	private class barcode implements ActionListener{
		public void actionPerformed(ActionEvent event){
			try{
				Input(inputBarcode.newcod(JOptionPane.showInputDialog("Введите штрих-код"),(String)skladCombo.getSelectedItem()));
				
			}
			catch (Exception e) {
				e.printStackTrace();
				// Вставить звук
			}
		}
	}
	private void Input(String aValue){
		 if (formInput==null)
			 formInput = new InputCountTovar();
		 int inBox=1;
		 ResultSet rs=DataSet.QueryExec("Select kol form tovar where name='"+aValue+"'");
		 try{
			 rs.next();
			 inBox=rs.getInt(1);
			 rs.close();
		 }
		 catch (Exception e){
			 e.printStackTrace();
		 }
		 
		 float Opt,One,Box;
		 rs=DataSet.QueryExec("select cost from price where id_tovar=(select id_tovar from tovar where name='"+aValue+"') and id_skl=(select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') and id_price=1");
		 try{
			 rs.next();
			 Opt=rs.getFloat(1);
			 rs.close();
		 }
		 catch (Exception e){
			 e.printStackTrace();
		 }
		 int res=model.present(aValue);
		 Box=0;
		 if (res==-1){
			 rs=DataSet.QueryExec("select cost from price where id_tovar=(select id_tovar from tovar where name='"+aValue+"') and id_skl=(select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') and id_price=(select id_price from type_price where name='"+(String)priceCombo.getSelectedItem()+"')");
			 try{
				 rs.next();
				 Box=rs.getFloat(1);
				 rs.close();
			 }
			 catch (Exception e){
				 e.printStackTrace();
			 }
		 }else{
			 Box=((Float)model.getValueAt(res, 3)).floatValue();
		 }
		boolean roz=false;
		One=Box;
		rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'");
		int disc = 0;
		try {
			rs.next();
			res=rs.getInt(1);
			rs.close();
			if (res==1){
				rs=DataSet.QueryExec("select disc from discount where id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') and id_skl=(select id_skl from sklad where name='"+(String)skladCombo.getSelectedItem()+"')");
				rs.next();
				disc=rs.getInt(1);
				Box=Box*(1-disc/100);
			}else{
				rs=DataSet.QueryExec("select kol from tovar where name='"+aValue+"'");
				
			}
			
				
		}
		catch (Exception e) { e.printStackTrace();}

		 int kolTov=formInput.showDialog(this, "Количество", 1, 2, 3, aValue, inBox);
		 
	}
}
class JComboBoxFire extends JComboBox{
	public void fireActionEvent()
	{
		super.fireActionEvent();
	}
}
	
