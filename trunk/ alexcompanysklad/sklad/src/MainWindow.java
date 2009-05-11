import java.awt.*;
import java.awt.event.*;
//import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

//import org.kempelin.web.servlet.RegistrForm.TypeError;




public class MainWindow 
{
	public static void main (String[] args) 
	{
		SimpleFrame frame = new SimpleFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar= new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu saleMenu=new JMenu("Продажа");
		menuBar.add(saleMenu);
		JMenu editMenu=new JMenu("Редактирование");
		menuBar.add(editMenu);
		JMenuItem barcodeItem=new JMenuItem("Штрих-код");
		editMenu.add(barcodeItem);
		JMenu docMenu =new JMenu("Накладные");
		editMenu.add(docMenu);
		JMenuItem regdocItem = new JMenuItem("Проведенные");
		JMenuItem nonregdocItem = new JMenuItem("Непроведенные");
		docMenu.add(regdocItem);
		docMenu.add(nonregdocItem);
		JMenu clientMenu=new JMenu("Клиенты");
		JMenuItem lgotiItem =new JMenuItem("Скидка");
		JMenuItem dataItem =new JMenuItem("Данные");
		editMenu.add(clientMenu);
		clientMenu.add(lgotiItem);
		clientMenu.add(dataItem);
		JMenu doceditMenu = new JMenu("Накладные в обработке");
		menuBar.add(doceditMenu);
		JMenu windowMenu = new JMenu("Окно");
		menuBar.add(windowMenu);
		JMenuItem windowcloseItem = new JMenuItem("Закрыть текущее окно");
		JMenuItem windowcloseallItem = new JMenuItem("Закрыть все окна");
		windowMenu.add(windowcloseItem);
		windowMenu.add(windowcloseallItem);
		final NewSaleFrame newFrame = new NewSaleFrame();
		newFrame.setVisible(false);
		frame.add(newFrame);
		ActionListener NewSale=new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				newFrame.setVisible(true);
//				NewSaleFrame newFrame = new NewSaleFrame();
//				Container contentPane=frame.getContentPane();
				
//				newFrame.setVisible(true);
//				frame.add(newFrame);
			}
		};
		barcodeItem.addActionListener(NewSale);
		frame.setVisible(true);
	}
}

class NewSaleFrame extends JPanel
{
	public NewSaleFrame()
	{
//		setTitle("Ввод накладной");
//		setMaximizable(true);
//		setClosable(true);
//		setResizable(true);
//		setExtendedState(Frame.MAXIMIZED_BOTH);
		Connection cn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn = DriverManager.getConnection("jdbc:oracle:thin:@91.210.177.35:1521:XE", "sklad", "sklad");
		}
		catch (Exception e) { e.printStackTrace();}
		Statement st = null;
		ResultSet rs = null;
//			String init="";
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		JButton saveButton = new JButton("Сохранить");
		JButton cancelButton = new JButton("Отмена");
		JButton barcodeButton = new JButton("Штрих-код(F1,+)");
		JButton listButton = new JButton("Выбрать из списка");
		JButton printButton = new JButton("Печать");
		JLabel skladLabel = new JLabel("Склад:");
		JLabel clientLabel = new JLabel("Клиент:");
		JComboBox skladCombo = new JComboBox();
		JComboBox clientCombo = new JComboBox();
		realTableModel model = new realTableModel();
		JTable naklTable=new JTable(model);
		add(new JScrollPane(naklTable), "Center");
//		skladCombo.addItem("Test");
		String Query="select name from sklad order by name";
		try { 
			st = cn.createStatement();
			rs = st.executeQuery(Query);
			rs.next();
			while (!rs.isAfterLast()){
				skladCombo.addItem(rs.getString("name"));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		Query="select rtrim(name) from client where priznak in (1,2)";
		try { 
			if (st==null) 
				st = cn.createStatement();
			rs = st.executeQuery(Query);
			rs.next();
			while (!rs.isAfterLast()){
				clientCombo.addItem(rs.getString("rtrim(name)"));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}

		rs=null;
		add(saveButton);
		add(cancelButton);
		add(barcodeButton);
		add(listButton);
		add(printButton);
		add(skladLabel);
		add(skladCombo);
		add(clientLabel);
		add(clientCombo);
		add(naklTable);
		Spring s=Spring.constant(0,10000,10000);
//		Spring s1=Spring.constant(0,50000,50000);
		Spring sSouth=Spring.constant(-10);
		layout.putConstraint(SpringLayout.SOUTH, saveButton, sSouth ,SpringLayout.SOUTH,this); 
		layout.putConstraint(SpringLayout.SOUTH, cancelButton, sSouth ,SpringLayout.SOUTH,this);
		layout.putConstraint(SpringLayout.SOUTH, barcodeButton, sSouth ,SpringLayout.SOUTH,this);
		layout.putConstraint(SpringLayout.SOUTH, listButton, sSouth ,SpringLayout.SOUTH,this);
		layout.putConstraint(SpringLayout.SOUTH, printButton, sSouth ,SpringLayout.SOUTH,this);
		sSouth=Spring.constant(10);
		layout.putConstraint(SpringLayout.WEST, skladLabel, sSouth, SpringLayout.WEST, this );
		layout.putConstraint(SpringLayout.WEST, clientLabel, sSouth, SpringLayout.WEST, this );
		layout.putConstraint(SpringLayout.NORTH, skladCombo, sSouth ,SpringLayout.NORTH,this);
		sSouth=Spring.constant(13);
		layout.putConstraint(SpringLayout.NORTH, skladLabel, sSouth ,SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST, skladCombo, s, SpringLayout.EAST, skladLabel );
		sSouth=Spring.constant(40);
		layout.putConstraint(SpringLayout.NORTH, clientCombo, sSouth ,SpringLayout.NORTH,this);
		sSouth=Spring.constant(43);
		layout.putConstraint(SpringLayout.NORTH, clientLabel, sSouth ,SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST, clientCombo, s, SpringLayout.WEST, this );
		layout.putConstraint(SpringLayout.WEST, clientCombo, s, SpringLayout.EAST, clientLabel );
		
		layout.putConstraint(SpringLayout.WEST, saveButton, s, SpringLayout.WEST, this );
		layout.putConstraint(SpringLayout.WEST, cancelButton, s, SpringLayout.EAST, saveButton );
		layout.putConstraint(SpringLayout.WEST, barcodeButton, s, SpringLayout.EAST, cancelButton );
		layout.putConstraint(SpringLayout.WEST, listButton, s, SpringLayout.EAST, barcodeButton );
		layout.putConstraint(SpringLayout.WEST, printButton, s, SpringLayout.EAST, listButton );
		layout.putConstraint(SpringLayout.EAST, this, s, SpringLayout.EAST,printButton );
		
	}
}



class SimpleFrame extends JFrame
{
	public SimpleFrame(){
		setTitle("Склад 4.0 менеджер "+GetUserName());
		setSize(640, 480);

		

		
		
//		ButtonPanel panel=new ButtonPanel();
//		Container contentPane=getContentPane();
//		contentPane.add(panel);
//		StyleAction newAction= new StyleAction();
//		SimpleFrame.this.addWindowListener(newAction);
		
	}
	private class StyleAction extends WindowAdapter{
		public void windowOpened (WindowEvent e){
			try
			{
				UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
				SwingUtilities.updateComponentTreeUI(SimpleFrame.this);
			}catch(Exception er){
				er.printStackTrace();
			}
		}
	}
	private void SetUserName(String aUserName){
		UserName=aUserName;
	}
	private String GetUserName(){
		return UserName;
	}
	public String UserName ="";
}
class ButtonPanel extends JPanel{
	public ButtonPanel(){
		JButton yellowButton = new JButton("Yellow");
		JButton blueButton = new JButton("Blue");
		JButton redButton = new JButton("Red");
		
		add(yellowButton);
		add(blueButton);
		add(redButton);
		
		ColorAction yellowAction = new ColorAction(Color.yellow);
		ColorAction blueAction = new ColorAction(Color.blue);
		ColorAction redAction = new ColorAction(Color.red);
		
		yellowButton.addActionListener(yellowAction);
		blueButton.addActionListener(blueAction);
		redButton.addActionListener(redAction);
	}
	
	private class ColorAction implements ActionListener{
		public ColorAction (Color c){
			backgroundColor = c;
		}
		public void actionPerformed(ActionEvent event){
			setBackground(backgroundColor);
		}
		private Color backgroundColor;
	
	}
}

class realTableModel extends AbstractTableModel{
	dataCont nakl = new dataCont("","",0);
	private boolean Editable = false;
	public void setClient(String aValue){
		nakl.setNameClient(aValue);
	}
	public void setSklad(String aValue){
		nakl.setNameSklad(aValue);
	}
	public void setIndDiscount(int aValue){
		nakl.setIndDiscount(aValue);
	}
	public boolean getEditable(){
		return Editable;
	}
	public void setEditable(boolean aValue){
		Editable=aValue;
	}
	public int getRowCount(){
		return nakl.getSize();
	}
	public int getColumnCount(){
		return 6;
	}
	public Object getValueAt(int row, int column){
		return nakl.getValueAt(row, column);
	}
	public String getColumnName(int column){
		switch (column){
		case 1:
			return "№";
		case 2:
			return "Наименование";
		case 3:
			return "Кол-во";
		case 4:
			return "Цена";
		case 5:
			return "Сумма";
		case 6:
			return "Скидка";
		default:
			return "";
		}
	}	
	public void setValueAt(Object aValue, int row, int column){
		nakl.setValueAt(aValue, row, column);
	}
	public boolean isCellEditable(int row, int column){
		if (row>nakl.getSize()){
			return false;
		}else 
			if (getEditable()){
				switch (column){
				case 1:
					return false;
				case 2:
					return false;
				case 3:
					return true;
				case 4:
					return true;
				case 5:
					return false;
				case 6:
					return true;
				default:
					return false;
				}	
			}else{
				return false;
			}
	}
}
class dataCont{
	private Vector name, count,cost,discount;
	private String nameClient, nameSklad;
	private int inddiscount;
	public dataCont (String anameClient,String anameSklad, int ainddiscount){
		name=new Vector(0);
		count=new Vector(0);
		cost=new Vector(0);
		discount=new Vector(0);
		nameClient=anameClient;
		nameSklad=anameSklad;
		inddiscount=ainddiscount;
	}
	public void setNameClient(String aValue){
		nameClient=aValue;
	}
	public void setNameSklad(String aValue){
		nameSklad=aValue;
	}
	public void setIndDiscount(int aValue){
		inddiscount=aValue;
	}
	public String getNameClient(){
		return nameClient;
	}
	public int getIndDiscount(){
		return inddiscount;
	}
	public String getNameSklad(){
		return nameSklad;
	}
	private void newCount(int aCount){
		count.add(new Integer(aCount));
	}
	private void setCount(Object aCount, int pos){
		count.setElementAt(aCount, pos);
	}
	private Object getCount(int pos){
		return count.elementAt(pos);
	}
	private void newName(String aName){
		name.add(aName);
	}
	private void setName(Object aName, int pos){
		name.setElementAt(aName, pos);
	}
	private String getName(int pos){
		return (String)name.elementAt(pos);
	}
	private void newCost(double aCost){
		cost.add(new Double(aCost));
	}
	private void setCost(Object aCost, int pos){
		cost.setElementAt(aCost, pos);
	}
	private Object getCost(int pos){
		return cost.elementAt(pos);
	}
	private void newDiscount(double aDiscount){
		discount.add(new Double(aDiscount));
	}
	private void setDiscount(Object aDiscount, int pos){
		discount.setElementAt(aDiscount, pos);
	}
	private Object getDiscount(int pos){
		return discount.elementAt(pos);
	}
	public void add(String aName, int aCount, double aCost, double aDiscount){
		newCount(aCount);
		newName(aName);
		newCost(aCost);
		newDiscount(aDiscount);
	}
	/*
	public Boolean set(String aName, int aCount, double aCost, double aDiscount,int pos){
		if (getSize()<pos){
			return false;
		}else{
			setCount(aCount, pos);
			setName(aName, pos);
			setCost(aCost,pos);
			setDiscount(aDiscount, pos);
			return true;
		}
	}
	*/
	public boolean remove(int pos){
		if (getSize()<pos){
			return false;
		}else{
			name.removeElementAt(pos);
			count.removeElementAt(pos);
			cost.removeElementAt(pos);
			discount.removeElementAt(pos);
			return true;
		}
	}
	public int getSize(){
		return name.size();
	}
	public Object getValueAt(int row, int column){
		switch (column){
		case 1:
			return (Integer)row;
		case 2:
			return getName(row);
		case 3:
			return getCount(row);
		case 4:
			return getCost(row);
		case 5:
			return (Double)(((Integer)getCount(row)).intValue()*((Double)getCost(row)).doubleValue()*(1-((Double)getDiscount(row)).doubleValue()/100));
		case 6:
			return getDiscount(row);
		default:
			return null;
		}
	}
	public void setValueAt(Object aValue, int row, int column){
		switch (column){
		case 2:
			setName(aValue,row);
		case 3:
			setCount(aValue, row);
		case 4:
			setCost(aValue,row);
		case 6:
			setDiscount(aValue,row);
		}
	}
}	
