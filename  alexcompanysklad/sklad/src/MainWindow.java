import javax.swing.JFrame;
import javax.swing.UIManager;








public class MainWindow 
{
	
	public static void main (String[] args) 
	{

		try {
			UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
}


