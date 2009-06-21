import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


class MainFrame extends JFrame
{
	
	public MainFrame(){
		setTitle("����� 4.0");
		setSize(800, 600);
		JMenuBar menuBar= new JMenuBar();
		setJMenuBar(menuBar);
		saleMenu=new JMenu("�������");
		menuBar.add(saleMenu);
		editMenu=new JMenu("��������������");
		menuBar.add(editMenu);
		JMenuItem barcodeItem=new JMenuItem("�����-���");
		editMenu.add(barcodeItem);
		JMenu docMenu =new JMenu("���������");
		editMenu.add(docMenu);
		JMenuItem regdocItem = new JMenuItem("�����������");
		JMenuItem nonregdocItem = new JMenuItem("�������������");
		docMenu.add(regdocItem);
		docMenu.add(nonregdocItem);
		JMenu clientMenu=new JMenu("�������");
		JMenuItem lgotiItem =new JMenuItem("������");
		JMenuItem dataItem =new JMenuItem("������");
		editMenu.add(clientMenu);
		clientMenu.add(lgotiItem);
		clientMenu.add(dataItem);
		doceditMenu = new JMenu("��������� � ���������");
		menuBar.add(doceditMenu);
		printMenu = new JMenu("������");
		JMenuItem printWorkDoc = new JMenuItem("��������� � ���������");
		JMenuItem printOldDoc = new JMenuItem("����������� ���������");
		menuBar.add(printMenu);
		printMenu.add(printWorkDoc);
		printMenu.add(printOldDoc);
		JMenu windowMenu = new JMenu("����");
		menuBar.add(windowMenu);
		JMenuItem windowcloseItem = new JMenuItem("������� ������� ����");
		JMenuItem windowcloseallItem = new JMenuItem("������� ��� ����");
		windowMenu.add(windowcloseItem);
		windowMenu.add(windowcloseallItem);
		newFrame = new NewSaleFrame();
		newFrame.parent=this;
		newFrame.setVisible(false);
		add(newFrame);
//		NewSaleAction NewSale=new NewSaleAction();
		saleMenu.addMenuListener(new NewSaleAction());
		printWorkDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (Printdialog==null)
					Printdialog=new TovarChooser();
				ResultSet rs=DataSet.QueryExec("Select distinct trim(client.name) from document inner join client on client.id_client=document.id_client where numb is NULL order by trim(name)",true );
				Vector<String> data =new Vector<String>(0);
				try{
					rs.next();
					while (!rs.isAfterLast()){
					String item=rs.getString(1);
					data.addElement(item);
					rs.next();
				}
				}catch(Exception e){
					e.printStackTrace();
				}
				Printdialog.addTovar(data);
				if (Printdialog.showDialog(null, "����� �������")){
					DataSet.UpdateQuery("lock table document in exclusive mode");
					
				}
			}
		});
	}
	private JMenu saleMenu;
	private JMenu editMenu;
	private JMenu doceditMenu;
	private JMenu printMenu;
	private NewSaleFrame newFrame;
	private ManagerChooser dialog=null;
	private TovarChooser Printdialog=null;
	private String UserName ="";
	private class NewSaleAction implements MenuListener{
		public void menuSelected(MenuEvent event)
		{
			
			if (dialog==null)
				dialog=new ManagerChooser();
			if (dialog.showDialog(MainFrame.this, "���� � �������")){
//				newFrame.setVisible(true);
//				newFrame.requestFocus();
				newFrame.showform();
//				newFrame.setFocusable(true);
//				newFrame.skladCombo.requestFocus();
				saleMenu.setSelected(false);
				saleMenu.setEnabled(false);
				editMenu.setEnabled(false);
				printMenu.setEnabled(false);
				doceditMenu.setEnabled(false);
				newFrame.requestFocus();
				SetUserName(dialog.GetManager());

			}else{
					saleMenu.setSelected(false);
//					saleMenu.set
				}
		}
		public void menuDeselected(MenuEvent event){
			
		}
		public void menuCanceled(MenuEvent event){
			
		}
		
	}
	private void SetUserName(String aUserName){
		UserName=aUserName;
		setTitle("����� 4.0 �������� "+aUserName);
	}
	public String GetUserName(){
		return UserName;
	}
	public void closeSaleFrame(){
		saleMenu.setEnabled(true);
		editMenu.setEnabled(true);
		doceditMenu.setEnabled(true);
		printMenu.setEnabled(true);
		SetUserName("");
		
	}
	
}

