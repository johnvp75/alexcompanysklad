import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.*;

import javax.swing.*;


class InputCountTovar extends JPanel{
	private JTextField incase;
	private JTextField count;
	private JLabel name;
	private JLabel costBox;
	private JLabel costOpt;
	private JLabel costOne;
	private JLabel costBoxLabel;
	private JLabel costOneLabel;
	private JDialog dialog;
	private static boolean next;
	public InputCountTovar(){
		setLayout(null);
		name=new JLabel("");
//		Font font = new Font("Times New Roman",Font.PLAIN,14);
		name.setFont(new Font("Times New Roman",Font.BOLD,20));
		costBoxLabel=new JLabel("Цена за упаковку:");
		costBox=new JLabel("0,00");
		JLabel costOptLabel = new JLabel("Цена оптовая:");
		costOpt = new JLabel("0,00");
		costOneLabel = new JLabel("Цена за штуку");
		costOne = new JLabel("0,00");
		JLabel incaseLabel = new JLabel("Кол-во штук в упаковке");
		count=new JTextField("1");
		count.selectAll();
		JButton inCaseButton = new JButton("Изменить");
		incase = new JTextField("1");
		incase.setEnabled(false);
		inCaseButton.setFocusable(false);
		inCaseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				incase.setEnabled(true);
				incase.selectAll();
				incase.requestFocus();
			}
		});
		incase.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (incase.getText().length()==0) 
					incase.setText("1");
				if (incase.getText().equals("0"))
					incase.setText("1");
				DataSet.QueryExec("Update tovar set kol="+incase.getText()+" where name='"+name.getText()+"'",true);
				((JTextField)event.getSource()).transferFocus();
			}
			
		});
		JLabel countLabel = new JLabel("Количество:");
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
		count.setBounds(113, 187, 90, 22);
		inCaseButton.setBounds(221, 131, 95, 22);
		countLabel.setBounds(15, 187, 83, 22);
		incaseLabel.setBounds(15, 131, 142, 22);
		costOne.setBounds(132, 82, 40, 22);
		costOpt.setBounds(275, 54, 40, 22);
		incase.setBounds(173, 131, 29, 22);
		name.setBounds(15, 14, 308, 22);
		
/*		costOptLabel.setFont(font);
		costBoxLabel.setFont(font);
		costOneLabel.setFont(font);
		costBox.setFont(font);
		costOne.setFont(font);
		costOpt.setFont(font);
		inCaseButton.setFont(font);
		incaseLabel.setFont(font);
		incase.setFont(font);
		countLabel.setFont(font);
		count.setFont(font);
*/
		
		add(costBoxLabel);
		add(costBox);
		add(costOneLabel);
		add(costOptLabel);
		add(count);
		add(inCaseButton);
		add(countLabel);
		add(incaseLabel);
		add(costOne);
		add(costOpt);
		add(incase);
		add(name);
		
	}
	class DigInput extends KeyAdapter{
		public void keyTyped(KeyEvent event){
			if (!( String.valueOf(event.getKeyChar())).matches("[0-9]"))
//				event.setKeyCode(0);
				event.setKeyChar(KeyEvent.CHAR_UNDEFINED);
		}
		public void keyPressed(KeyEvent event){
			int keyCode=event.getKeyCode();
			if (keyCode==KeyEvent.VK_F1 || keyCode==107){
				event.setKeyCode(KeyEvent.VK_UNDEFINED);
				setNext(true);
				dialog.setVisible(false);
			}
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
		setNext(false);
		costBox.setVisible(!aRoz);
		costBoxLabel.setVisible(!aRoz);
		costOne.setVisible(aRoz);
		costOneLabel.setVisible(aRoz);
		dialog.setTitle(title);
		setCost(Box,Opt,One);
		setNameTov(aName);
		setinCase(ainCase);
		setCount();
		dialog.setBounds(200, 150, 400, 299);
//		dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
		dialog.setVisible(true);
		int ret=(new Integer(count.getText())).intValue();
		if (aRoz)
			ret=ret*((new Integer(incase.getText())).intValue());
		return ret;
		 
	}
	private void setCost(double Box, double Opt, double One){
		NumberFormat formatter = new DecimalFormat ( "0.00" ) ; 
	    String s = formatter.format ( Box ) ;
		costBox.setText(s);
		s= formatter.format ( One ) ;
		costOne.setText(s);
		s= formatter.format ( Opt ) ;
		costOpt.setText(s);
	}
	private void setNameTov(String aName){
		name.setText(aName);
		
	}
	private void setinCase(int ainCase){
		incase.setText(ainCase+"");
	}
	private void setCount(){
		count.setText("1");
		count.selectAll();
	}
	public static boolean getNext(){
		return next;
	}
	private void setNext(boolean aValue){
		next=aValue;
	}
}
