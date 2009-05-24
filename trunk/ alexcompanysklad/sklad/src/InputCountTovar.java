import java.awt.Component;
import java.awt.Frame;
import java.awt.event.*;

import javax.swing.*;


class InputCountTovar extends JPanel{
	private JTextField incase;
	private JTextField count;
	private JLabel name;
	private JLabel costBox;
	private JLabel costOpt;
	private JLabel costOne;
	private JDialog dialog;
	public InputCountTovar(){
		setLayout(null);
		name=new JLabel("");
		JLabel costBoxLabel=new JLabel("Цена за упаковку:");
		costBox=new JLabel("0,00");
		JLabel costOptLabel = new JLabel("Цена оптовая:");
		costOpt = new JLabel("0,00");
		JLabel costOneLabel = new JLabel("Цена за штуку");
		costOne = new JLabel("0,00");
		JLabel incaseLabel = new JLabel("Кол-во штук в упаковке");
		incase = new JTextField("1");
		incase.setEnabled(false);
		JButton countButton = new JButton("изменить");
		countButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				incase.setEnabled(true);
			}
		});
		incase.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (incase.getText().length()==0) 
					incase.setText("1");
				if (incase.getText().equals("0"))
					incase.setText("1");
				DataSet.QueryExec("Update tovar set kol="+incase.getText()+" where name='"+name.getText()+"'");
			}
			
		});
		JLabel countLabel = new JLabel("Количество:");
		count=new JTextField("1");
		count.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (count.getText().length()==0)
					count.setText("0");
				dialog.setVisible(false);
			}
		});
		DigInput listener =new DigInput();
		count.addKeyListener(listener);
		costBoxLabel.setBounds(15, 54, 104, 22);
		costBox.setBounds(132, 54, 40, 22);
		costOneLabel.setBounds(15, 82, 104, 22);
		costOptLabel.setBounds(178, 54, 85, 22);
		countButton.setBounds(221, 131, 68, 22);
		countLabel.setBounds(15, 187, 83, 22);
		incaseLabel.setBounds(15, 131, 142, 22);
		costOne.setBounds(132, 82, 40, 22);
		costOpt.setBounds(275, 54, 40, 22);
		count.setBounds(113, 187, 90, 22);
		incase.setBounds(173, 131, 29, 22);
		name.setBounds(15, 14, 308, 22);
		
		add(costBoxLabel);
		add(costBox);
		add(costOneLabel);
		add(costOptLabel);
		add(countButton);
		add(countLabel);
		add(incaseLabel);
		add(costOne);
		add(costOpt);
		add(count);
		add(incase);
		add(name);
		
	}
	class DigInput extends KeyAdapter{
		public void keyTyped(KeyEvent event){
			if (event.getKeyChar()=='k')
				event.setKeyChar('E');
		}
		
	}

	public int showDialog(Component parent, String title, double Box, double Opt, double One, String aName, int ainCase, boolean aRoz){
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
		setCost(Box,Opt,One);
		setNameTov(aName);
		setinCase(ainCase);
		dialog.setBounds(118, 150, 564, 299);
//		dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
		dialog.setVisible(true);
		return (new Integer(count.getText())).intValue();
		 
	}
	private void setCost(double Box, double Opt, double One){
		costBox.setText(Box+"");
		costOne.setText(One+"");
		costOpt.setText(Opt+"");
	}
	private void setNameTov(String aName){
		name.setText(aName);
		
	}
	private void setinCase(int ainCase){
		incase.setText(ainCase+"");
	}
}
