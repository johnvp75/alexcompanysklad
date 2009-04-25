import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MainWindow 
{
	public static void main (String[] args) 
	{
		SimpleFrame frame = new SimpleFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	
	}
}
class SimpleFrame extends JFrame
{
	public SimpleFrame(){
		setTitle("ButtonTest");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		ButtonPanel panel=new ButtonPanel();
		Container contentPane=getContentPane();
		contentPane.add(panel);
		StyleAction newAction= new StyleAction();
		SimpleFrame.this.addWindowListener(newAction);
		
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
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 200;
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

