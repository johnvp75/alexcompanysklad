import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.*;
import java.sql.ResultSet;

import javax.swing.*;


class NewClientDialog extends JPanel {
	private JTextField clientname;
	private JButton okButton;
	private JTextField address;
	private JTextArea phone;
	private JRadioButton opt;
	private boolean ok;
	private JDialog dialog;
	private boolean info;
	private String old;
	private JButton editButton;
	private JRadioButton roz;
	public NewClientDialog(){
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel clientLabel = new JLabel("Название (Ф.И.О.):");
		JLabel addressLabel = new JLabel("Адрес:");
		JLabel phoneLabel=new JLabel("Телефон:");
		JLabel typeLabel=new JLabel("Тип клиента:");
		clientname=new JTextField("");
		address=new JTextField("");
		phone = new JTextArea("");
		phone.setFont(new Font(Font.SERIF,Font.PLAIN,12));
		ButtonGroup type = new ButtonGroup();
		opt=new JRadioButton("Оптовый",true);
		roz=new JRadioButton("Розничный",false);
		type.add(opt);
		type.add(roz);
		
		clientLabel.setBounds(15, 18, 122, 22);
		clientname.setBounds(137, 18, 396, 22);
		addressLabel.setBounds(15, 49, 122, 22);
		address.setBounds(137, 49, 396, 22);
		phoneLabel.setBounds(15, 103, 122, 22);
		phone.setBounds(137, 80, 396, 51);
		typeLabel.setBounds(165, 172, 80, 22);
		opt.setBounds(283, 157, 93, 22);
		roz.setBounds(283, 185, 93, 22);
		
		clientname.addActionListener(new nextFocus());
		address.addActionListener(new nextFocus());

		
		panel.add(clientLabel);
		panel.add(clientname);
		panel.add(addressLabel);
		panel.add(address);
		panel.add(phoneLabel);
		panel.add(phone);
		panel.add(typeLabel);
		panel.add(opt);
		panel.add(roz);
		
		panel.setBounds(0, 0, 557, 211);
		add(panel,BorderLayout.CENTER);
		
		okButton=new JButton("Добавить");
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (clientname.getText().trim().length()==0){
					JOptionPane.showMessageDialog(dialog,"Поле название не может быть пустым","Ошибка",JOptionPane.ERROR_MESSAGE);
				}
				else{
					if (CheckClient()){
						addClient();
						ok=true;
						dialog.setVisible(false);
					}else{
						JOptionPane.showMessageDialog(dialog,"Такой клиент уже существует в базе! \n Будьте внимательней! ","Ошибка",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		editButton=new JButton("Редактировать");
		editButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				clientname.setEditable(true);
				address.setEditable(true);
				phone.setEditable(true);
				roz.setEnabled(true);
				opt.setEnabled(true);
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
		buttonPanel.add(editButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel,BorderLayout.SOUTH);


	}
	public boolean CheckClient(){
		boolean ret=false;
		if (info && getClient().equals(old))
			return true;
		try {
			ResultSet rs=DataSet.QueryExec("Select count(*) from client where name like '%"+clientname.getText().trim()+"%'",true);
			rs.next();
			if (rs.getInt(1)==0){
				ret=true;
			}
			rs.close();
		}
		catch (Exception e) { }
		return ret;

	}
	public void addClient(){
		int typeChoose=2;
		if (opt.isSelected()){
			typeChoose=1;
		}
		String query;
		if (info)
			query="update client set name='"+getClient()+"', adres='"+address.getText().trim()+"', phone='"+phone.getText().trim()+"', type="+typeChoose+", day=sysdate where name='"+old+"'";
		else
			query="insert into client (name,adres,phone,type,day) values ('"+clientname.getText().trim()+"','"+address.getText().trim()+"','"+phone.getText().trim()+"',"+typeChoose+", sysdate)";
		try {
			DataSet.QueryExec(query,true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "Ошибка записи!");
			e.printStackTrace();
		}
//		DataSet.QueryExec("commit");
	}
	public String getClient(){
		return clientname.getText().trim();
	}
	public void setClient(String value){
		clientname.setText(value);
		old=value;
	}
	public boolean showDialog(Component parent, String title, boolean Info){
		ok=false;
		info=Info;
		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame)parent;
		else
			owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
		if (dialog==null || dialog.getOwner()!=owner){
			dialog=new JDialog(owner,true);
			dialog.add(this);
//			dialog.getRootPane().setDefaultButton(okButton);
			dialog.pack();
		}
		dialog.setTitle(title);
		dialog.setBounds(118, 150, 564, 299);
//		dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
		if (Info)
			try{
				ResultSet rs=DataSet.QueryExec("Select adres, phone, type from client where name='"+getClient()+"'", false);
				if (rs.next()){
					address.setText(rs.getString(1).trim());
					phone.setText(rs.getString(2).trim());
					if (rs.getInt(3)==2)
						roz.setSelected(true);
					else
						opt.setSelected(true);
				}
				editButton.setEnabled(true);
				address.setEditable(false);
				clientname.setEditable(false);
				phone.setEditable(false);
				roz.setEnabled(false);
				opt.setEnabled(false);
				okButton.setText("Принять");
			}catch(Exception e){
				e.printStackTrace();
			}
			else{
				address.setText("");
				phone.setText("");
				editButton.setEnabled(false);
				address.setEditable(true);
				clientname.setEditable(true);
				phone.setEditable(true);
				roz.setEnabled(true);
				opt.setEnabled(true);
				okButton.setText("Добавить");
			}
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		return ok;
	}
	private class nextFocus implements ActionListener{
		public void actionPerformed(ActionEvent event){
			((JTextField)event.getSource()).transferFocus();
		}
	}
}
