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
	public ManagerChooser() {
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
//		panel.setLocation(350, 250);
		panel.setLayout(new GridLayout(2,2));
		panel.add(new JLabel("Менеджер:"));
		username=new JComboBox();
		ResultSet rs = DataSet.QueryExec("select rtrim(manager.name) from manager inner join rules on manager.id_rules=rules.id_rules where rules.id_doc like '%;2;%' order by manager.name",true);
		try { 
			rs.next();
			while (!rs.isAfterLast()){
				username.addItem(rs.getString("rtrim(manager.name)"));
				rs.next();
			}
			rs.close();
		}
		catch (Exception e) { e.printStackTrace();}
		
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
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel,BorderLayout.SOUTH);
	}
	private boolean PasswordCheck(){
		boolean ret=false;
		String z="select count(*) from manager where name='"+(String)username.getSelectedItem()+"' and password='"+(new String(password.getPassword()))+"'";
		ResultSet rs = DataSet.QueryExec(z,true);
		try { 
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
		dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
		ClearPassword();
		password.requestFocus();
		dialog.setVisible(true);
		
		return ok;
	}
}
