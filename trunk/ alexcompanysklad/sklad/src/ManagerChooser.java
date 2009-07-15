import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import javax.swing.*;



class ManagerChooser extends JPanel
{
	private JComboBox username;
	private JPasswordField password;
	private JButton okButton;
	private boolean ok;
	private JDialog dialog;
	private ChangePassword PasswordDialog;
	private String rul;
	public ManagerChooser() {
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
//		panel.setLocation(350, 250);
		panel.setLayout(new GridLayout(2,2));
		panel.add(new JLabel("Менеджер:"));
		username=new JComboBox();
		panel.add(username);
		panel.add(new JLabel("Пароль:"));
		panel.add(password=new JPasswordField(""));
		add(panel,BorderLayout.CENTER);
		okButton = new JButton("Ok!");
		okButton.addActionListener(new 
				ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						if(PasswordCheck()){
							ok=true;
							dialog.setVisible(false);
						}else{
							JOptionPane.showMessageDialog(dialog,"Пароль не правильный! \n Повторите ввод!","Неверный пароль",JOptionPane.ERROR_MESSAGE);
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
		JButton ChangePasButton = new JButton("Изменить пароль");
		ChangePasButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (PasswordDialog==null)
					PasswordDialog = new ChangePassword();
				PasswordDialog.setUserName(GetManager());
				PasswordDialog.showDialog(dialog, "Новый пароль для "+GetManager());
				ClearPassword();
				password.requestFocus();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(ChangePasButton );
		add(buttonPanel,BorderLayout.SOUTH);
		
	}
	private boolean PasswordCheck(){
		boolean ret=false;
		String SQL="select count(*) from manager where name='"+(String)username.getSelectedItem()+"' and password='"+(new String(password.getPassword()))+"'";
		
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
	private void ClearPassword(){
		password.setText("");
	}
	public String GetManager(){
		return (String)username.getSelectedItem();
	}
	public boolean showDialog(Component parent, String title){
		ok=false;
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
		
		username.removeAllItems();
		try {
			ResultSet rs = DataSet.QueryExec("select rtrim(manager.name) from manager inner join rules on manager.id_rules=rules.id_rules where rules.id_doc like '%"+rul+"%' order by manager.name",true);
			rs.next();
			while (!rs.isAfterLast()){
				username.addItem(rs.getString("rtrim(manager.name)"));
				rs.next();
			}
			rs.close();
		}
		catch (Exception e) { e.printStackTrace();}
		ClearPassword();
		password.requestFocus();
		dialog.setVisible(true);
		
		return ok;
	}
	public void setRul(String aValue){
		rul=aValue;
	}
}
