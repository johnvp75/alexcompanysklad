import java.awt.Font;
import java.awt.event.*;
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
	private static ListChoose formGroup=null;
	private JButton barcodeButton;
	private JTable naklTable;
	public MainFrame parent;
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
		barcodeButton = new JButton("Штрих-код(F1,+)");
		JButton listButton = new JButton("Выбрать из списка");
		JButton printButton = new JButton("Печать");
		JLabel skladLabel = new JLabel("Склад:");
		JLabel clientLabel = new JLabel("Клиент:");
		priceLabel = new JLabel("Прайс:");
		okrLabel = new JLabel("Округление:");
		okrCombo = new JComboBox();
		okrCombo.addItem("Без округления");
		okrCombo.addItem("До 0,1");
		okrCombo.addItem("До 1");
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
		
		naklTable=new JTable(model);
		Font font = new Font("Times New Roman",Font.PLAIN,16);
		naklTable.setFont(font);
		naklTable.setAutoCreateColumnsFromModel(false);
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
		barcodeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				BarCodeFire();
			}
		});
		skladCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				model.setSklad((String)skladCombo.getSelectedItem());
			}
		});
		editableCheck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				model.setEditable(((JCheckBox)(event.getSource())).isSelected());
			}
		});
		pressF1 press=new pressF1();
		addKeyListener(press);
		skladCombo.addKeyListener(press);
		clientCombo.getEditor().getEditorComponent().addKeyListener(press);
		editableCheck.addKeyListener(press);
		cancelButton.addKeyListener(press);
		listButton.addKeyListener(press);
		printButton.addKeyListener(press);
		saveButton.addKeyListener(press);
		barcodeButton.addKeyListener(press);
		okrCombo.addKeyListener(press);
		priceCombo.addKeyListener(press);
		clientCombo.getEditor().getEditorComponent().addFocusListener(new FocusAdapter(){
		    public void focusGained(FocusEvent event){
		        clientCombo.getEditor().selectAll();
		    }
		});
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (model.getRowCount()>0&& JOptionPane.showConfirmDialog(parent, "Внимание! Все введенные данные будут удалены! Продолжить?","Удаление",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION ){
					return;
				}
				model.removeAll();
				setVisible(false);
				parent.closeSaleFrame();

			}
		});
		listButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (formGroup==null)
					formGroup= new ListChoose();
				if (formGroup.showDialog(parent, "Выбор товара",skladCombo.getSelectedItem().toString()))
					Input(formGroup.getTovar());
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
		setFocusable(true);
//		this.requestFocus();
		
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
//			clientCombo.getEditor().selectAll();
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
	private void Input(String aValue){
		if (aValue==null)
			return;
		if (formInput==null)
			 formInput = new InputCountTovar();
		 int inBox=1;
		 ResultSet rs=DataSet.QueryExec("Select kol from tovar where name='"+aValue+"'");
		 try{
			 rs.next();
			 inBox=rs.getInt(1);
			 rs.close();
		 }
		 catch (Exception e){
			 e.printStackTrace();
		 }
		 
		 double Opt,One,Box;
		 Opt=0;
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
			 Box=((Double)model.getValueAt(res, 3)).doubleValue();
		 }
		boolean roz=false;
		One=Box;
		rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'");
		
		Box=Box*(1-model.getIndDiscount()/100);
		if (res==-1){
			One=One/inBox;
			One=(int)(One*Math.pow(10, 2-okrCombo.getSelectedIndex())+0.5)/Math.pow(10, 2-okrCombo.getSelectedIndex());
		}
		try {
			rs.next();
			if (!(rs.getInt(1)==1))
				roz=true;
		}
		catch (Exception e) { e.printStackTrace();}
		double aCost=Box;
		if (roz){
			aCost=One;
		}
		int kolTov=formInput.showDialog(this, "Количество", Box, Opt, One, aValue, inBox, roz);
		model.add(aValue, kolTov, aCost, 0);
//		naklTable.repaint();
		 
	}
	private void BarCodeFire(){
		try{
			Input(inputBarcode.newcod(JOptionPane.showInputDialog("Введите штрих-код"),(String)skladCombo.getSelectedItem()));
			
		}
		catch (Exception e) {
			e.printStackTrace();
			// Вставить звук
		}
	}
	public class pressF1 extends KeyAdapter{
		public void keyPressed(KeyEvent event){
			int keyCode=event.getKeyCode();
			if (keyCode==KeyEvent.VK_F1 || keyCode==107){
				event.setKeyCode(KeyEvent.VK_UNDEFINED);
				naklTable.requestFocus();
				BarCodeFire();
			}
		}
	}
}
class JComboBoxFire extends JComboBox{
	public void fireActionEvent()
	{
		super.fireActionEvent();
	}
}
