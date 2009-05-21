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
		count.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent event){
				if (event.getKeyChar()=='k')
					event.setKeyChar('E');
			}
		});
		costBoxLabel.setBounds(5, 5, 60, 22);
		costBox.setBounds(5, 5, 60, 22);
		costOneLabel.setBounds(5, 5, 60, 22);
		costOptLabel.setBounds(5, 5, 60, 22);
		countButton.setBounds(5, 5, 60, 22);
		countLabel.setBounds(5, 5, 60, 22);
		incaseLabel.setBounds(5, 5, 60, 22);
		costOne.setBounds(5, 5, 60, 22);
		costOpt.setBounds(5, 5, 60, 22);
		count.setBounds(5, 5, 60, 22);
		incase.setBounds(5, 5, 60, 22);
		name.setBounds(5, 5, 60, 22);
		
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
		class DigInput extends KeyAdapter{
			public void keyTyped(KeyEvent event){
				if (event.getKeyChar()=='k')
					event.setKeyChar('E');
			}
			
		}
		
	}
	public int showDialog(Component parent, String title, int Box, int Opt, int One, String aName, int ainCase){
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
	private void setCost(int Box, int Opt, int One){
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
