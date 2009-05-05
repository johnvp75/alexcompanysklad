import java.awt.*;
import java.awt.event.*;
//import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
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
		JTable naklTable=new JTable();
		TableColumn nCol = new TableColumn();
		nCol.setHeaderValue("№");

		naklTable.addColumn(nCol);
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

