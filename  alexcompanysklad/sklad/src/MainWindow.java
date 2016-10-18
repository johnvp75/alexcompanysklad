import javax.swing.JFrame;
import javax.swing.UIManager;








public class MainWindow 
{
	public static ChooserStreamIn Scaner;
//	public static Thread ThScaner; 
	public static void main (String[] args) 
	{

		try {
//			UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		MainFrame frame;
		frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Scaner = new ChooserStreamIn();
		(new Thread(Scaner)).start();
		frame.setVisible(true);
	}
	
}


