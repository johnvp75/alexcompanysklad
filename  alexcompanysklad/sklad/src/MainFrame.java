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
	}
	private JMenu saleMenu;
	private JMenu editMenu;
	private JMenu doceditMenu;
	private NewSaleFrame newFrame;
	private ManagerChooser dialog=null;
	private String UserName ="";
	private class NewSaleAction implements MenuListener{
		public void menuSelected(MenuEvent event)
		{
			
			if (dialog==null)
				dialog=new ManagerChooser();
			if (dialog.showDialog(MainFrame.this, "���� � �������")){
				newFrame.setVisible(true);
//				newFrame.requestFocus();

				saleMenu.setSelected(false);
				saleMenu.setEnabled(false);
				editMenu.setEnabled(false);
				doceditMenu.setEnabled(false);
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
	private String GetUserName(){
		return UserName;
	}
	public void closeSaleFrame(){
		saleMenu.setEnabled(true);
		editMenu.setEnabled(true);
		doceditMenu.setEnabled(true);
		
		SetUserName("");
		
	}
	
}
