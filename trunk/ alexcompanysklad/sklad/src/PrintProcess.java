import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;



public class PrintProcess extends JDialog {

	public static boolean Printing=true;
	public static boolean CancelPrint=false;
	
	private JLabel print;
	private JLabel clientName;
	private JButton cancel;
	private JPanel mainPanel;
	private boolean status;
	private int time=10;
	
	public PrintProcess(){
		setStatus(Printing);
		Font fontPlain=new Font("Times New Roman", Font.PLAIN,14);
		Font fontBold=new Font("Times New Roman", Font.BOLD,12);
		print=new JLabel ("Печатается:");
		clientName=new JLabel ();
		cancel=new JButton("Отменить");
		mainPanel= new JPanel();
		print.setFont(fontPlain);
		clientName.setFont(fontBold);
		cancel.setFont(fontPlain);
		print.setHorizontalAlignment(JLabel.CENTER);
		clientName.setHorizontalAlignment(JLabel.CENTER);
		print.setBounds(1, 5, 180, 20);
		clientName.setBounds(1, 27, 180, 20);
		cancel.setBounds(35, 50, 120, 30);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setStatus(CancelPrint);
				hideDialog();
			}
		});
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				if (time>0) {
					setStatus(CancelPrint);
					
				}
			}
		});
		mainPanel.setLayout(null);
		mainPanel.add(print);
		mainPanel.add(clientName);
		mainPanel.add(cancel);
		this.setBounds(120, 120, 220, 120);
		this.add(mainPanel);
		this.add(mainPanel);
	}
	private void hideDialog (){
		this.setVisible(false);
	}

	public boolean ShowDialog(String aName){
		this.setModal(true);
		clientName.setText(aName);
		
		int delay = 1000; 
		ActionListener taskPerformer = new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    	if (time==0){
		    		hideDialog();
		    	}
		    	cancel.setText("Отменить (" + time + ")");
		    	time--;
		          
		    }
		};
		new Timer(delay, taskPerformer).start();
		this.setVisible(true);
		return isStatus();
	}
	
	private boolean isStatus() {
		return status;
	}


	private void setStatus(boolean status) {
		this.status = status;
	}
}
