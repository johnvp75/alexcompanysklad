import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


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
		
		frame.setVisible(true);
	
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
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows");
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

