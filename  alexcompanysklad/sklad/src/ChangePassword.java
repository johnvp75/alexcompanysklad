import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;


public class ChangePassword extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPasswordField OldPassword;
	private JPasswordField NewPassword1;
	private JPasswordField NewPassword2;
	private JButton okButton;
	private String UserName;
	private JDialog dialog;
//	private boolean ok;
	public ChangePassword() {
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
//		panel.setLocation(350, 250);
		panel.setLayout(new GridLayout(3,2));
		panel.add(new JLabel("Текущий пароль:"));
		panel.add(OldPassword=new JPasswordField(""));
		panel.add(new JLabel("Новый пароль:"));
		panel.add(NewPassword1=new JPasswordField(""));
		panel.add(new JLabel("Повторите новый пароль:"));
		panel.add(NewPassword2=new JPasswordField(""));
		add(panel,BorderLayout.CENTER);
		okButton = new JButton("Ok!");
		okButton.addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						if(OldPasswordCheck()){
							if (PasswordCheck()){
								try {
									DataSet.QueryExec("update manager set password = '"+(new String(NewPassword1.getPassword()))+"' where name='"+getUserName()+"'", true);
								} catch (Exception e) {
									e.printStackTrace();
								}
								dialog.setVisible(false);
							}
							else
							{
								JOptionPane.showMessageDialog(dialog,"Пароли не совпадают! \n Повторите ввод!","Неверный пароль",JOptionPane.ERROR_MESSAGE);
								ClearPassword();
								
							}
						}else{
							JOptionPane.showMessageDialog(dialog,"Текущий пароль не правильный! \n Повторите ввод!","Неверный пароль",JOptionPane.ERROR_MESSAGE);
							ClearPassword();
						}
					}
				});
		JButton cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						dialog.setVisible(false);
					}
				});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel,BorderLayout.SOUTH);
		

	}
	private String getUserName(){
		return UserName;
	}
	public void setUserName(String aValue){
		UserName=aValue;
	}
	
	private boolean OldPasswordCheck(){
		boolean ret=false;
		String SQL="select count(*) from manager where name='"+getUserName()+"' and password='"+(new String(OldPassword.getPassword()))+"'";
		
		try {
			ResultSet rs = DataSet.QueryExec(SQL,true);
			rs.next();
			if (rs.getInt(1)>0){
				ret=true;
			}
			rs.close();
		}
		catch (Exception e) { }
		return ret;
	}
	private boolean PasswordCheck(){
		return (new String(NewPassword1.getPassword())).equals((new String(NewPassword2.getPassword())));
	}
	private void ClearPassword(){
		OldPassword.setText("");
		NewPassword1.setText("");
		NewPassword2.setText("");
		OldPassword.requestFocus();
	}
	public void showDialog(Component parent, String title){
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame)parent;
		else
			owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
		if (dialog==null || dialog.getOwner()!=owner){
			dialog=new JDialog(owner,true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.pack();
		}
		dialog.setTitle(title);
//		dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
		dialog.setLocationRelativeTo(parent);
		ClearPassword();
		dialog.setVisible(true);
	}

}
