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
		JLabel costBoxLabel=new JLabel("���� �� ��������:");
		costBox=new JLabel("0,00");
		JLabel costOptLabel = new JLabel("���� �������:");
		costOpt = new JLabel("0,00");
		JLabel costOneLabel = new JLabel("���� �� �����");
		costOne = new JLabel("0,00");
		JLabel incaseLabel = new JLabel("���-�� ���� � ��������");
		incase = new JTextField("1");
		incase.setEnabled(false);
		JButton countButton = new JButton("��������");
		countButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				incase.setEnabled(true);
			}
		});
		JLabel countLabel = new JLabel("����������:");
		count=new JTextField("1");
		count.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (count.getText().length()==0)
					count.setText("0");
				dialog.setVisible(false);
			}
		});
		
	}
}
