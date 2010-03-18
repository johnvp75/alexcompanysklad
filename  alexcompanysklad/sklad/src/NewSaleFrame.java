import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;




//import javax.swing.JComboBox.KeySelectionManager;





class NewSaleFrame extends MyPanel
{
	private JLabel okrLabel;
	private JComboBox okrCombo;
	private AutoComplete clientCombo;
	private JComboBoxFire skladCombo;
	private NewClientDialog newClient=null;
	private JComboBox priceCombo;
	private JLabel priceLabel;
	private JLabel itogo;
	private JLabel itogowo;
	private JLabel itogoallLabel;
	private naklTableModel model;
	public static InputCountTovar formInput = null;
	private static ListChoose formGroup=null;
	private JButton barcodeButton;
	private MyTable naklTable;
	public MainFrame parent;
	private JCheckBox editableCheck;
	private String note;
	private JPopupMenu popup;
	private int p;
	private double itogoall=0.0;
	private JTextField noteText;
	private ActionListener clientlistener;
	private ActionListener skladlistener;
	private boolean Changed;
	private int id_doc;
	private JButton infoButton;
	public NewSaleFrame()
	{
//		setTitle("���� ���������");
//		setMaximizable(true);
//		setClosable(true);
//		setResizable(true);
//		setExtendedState(Frame.MAXIMIZED_BOTH);
/*		Connection cn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			cn = DriverManager.getConnection("jdbc:oracle:thin:@91.210.177.35:1521:XE", "sklad", "sklad");
		}
		catch (Exception e) { e.printStackTrace();}
		Statement st = null;
		ResultSet rs = null;
*/
		setLayout(null);
		JButton saveButton = new JButton("���������");
		JButton cancelButton = new JButton("������");
		barcodeButton = new JButton("�����-���(F1)");
		JButton listButton = new JButton("������� �� ������");
		JButton printButton = new JButton("������");
		JLabel skladLabel = new JLabel("�����:");
		JLabel clientLabel = new JLabel("������:");
		JLabel noteLabel = new JLabel("����������");
		noteText = new JTextField("");
		itogo = new JLabel("����� (�������� ������): 0,00");
		itogowo = new JLabel("����� (�� �������� ������): 0,00");
		itogoallLabel= new JLabel("����� �� ���� ���������: 0,00");
		priceLabel = new JLabel("�����:");
		okrLabel = new JLabel("����������:");
		okrCombo = new JComboBox();
		okrCombo.addItem("��� ����������");
		okrCombo.addItem("�� 0,1");
		okrCombo.addItem("�� 1");
		infoButton = new JButton("����������");
		editableCheck = new JCheckBox("������������� � ������");
		skladCombo = new JComboBoxFire();
		clientCombo = new AutoComplete();
		clientCombo.setEditable(true);
		priceCombo=new JComboBox();
		ResultSet rs=null;
		try{
			rs = DataSet.QueryExec("select trim(name) from sklad order by name",true);
			rs.next();
			while (!rs.isAfterLast()){
				skladCombo.addItem(rs.getString(1));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		skladCombo.setSelectedIndex(0);
		
		try {
			rs = DataSet.QueryExec("select trim(name) from client where type in (1,2) order by upper(name)",true);
			rs.next();
			while (!rs.isAfterLast()){
				clientCombo.addItem(rs.getString(1));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		clientCombo.setSelectedIndex(0);
		try {
			rs = DataSet.QueryExec("select trim(name) from type_price order by name",true);
			rs.next();
			while (!rs.isAfterLast()){
				priceCombo.addItem(rs.getString(1));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		
		int disc=0;
		try{
			rs = DataSet.QueryExec("select disc from discount where id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') and id_skl=(select id_skl from sklad where name='"+(String)skladCombo.getSelectedItem()+"')",true);
			if (rs.next()){
				disc=rs.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model = new naklTableModel((String)clientCombo.getSelectedItem(),(String)skladCombo.getSelectedItem(),disc,true);
		
		naklTable=new MyTable(model);
//		Font font = new Font("Times New Roman",Font.PLAIN,16);
//		naklTable.setFont(font);
		naklTable.setAutoCreateColumnsFromModel(false);
		naklTable.getColumnModel().getColumn(0).setMaxWidth(30);
		naklTable.getColumnModel().getColumn(1).setMaxWidth(455);
		naklTable.getColumnModel().getColumn(2).setMaxWidth(50);
		naklTable.getColumnModel().getColumn(3).setMaxWidth(71);
		naklTable.getColumnModel().getColumn(4).setMaxWidth(96);
		naklTable.getColumnModel().getColumn(5).setMaxWidth(58);
		JScrollPane ScrollTable=new JScrollPane(naklTable);
		naklTable.setColumnSelectionAllowed(true);
		naklTable.setRowSelectionAllowed(true);
		naklTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT). 
        put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), 
            "selectNextColumnCell");
		
		popup = new JPopupMenu();
		JMenuItem del = new JMenuItem("������� ������");
		popup.add(del);
//		naklTable.setComponentPopupMenu(popup);

		saveButton.setBounds(34, 495, 104, 22);
		cancelButton.setBounds(159, 495, 104, 22);
		barcodeButton.setBounds(285, 495, 150, 22);
		listButton.setBounds(455, 495, 163, 22);
		printButton.setBounds(638, 495, 104, 22);
		skladLabel.setBounds(10, 28, 58, 22);
		skladCombo.setBounds(79, 28, 207, 22);
		clientLabel.setBounds(10, 58, 61, 22);
		clientCombo.setBounds(79, 58, 207, 22);
		infoButton.setBounds(296, 58, 104, 22);
		ScrollTable.setBounds(6, 89, 769, 335);
		priceLabel.setBounds(457, 1, 86, 22);
		priceCombo.setBounds(555, 1, 207, 22);
		okrLabel.setBounds(457, 28, 86, 22);
		okrCombo.setBounds(555, 28, 207, 22);	
		editableCheck.setBounds(555, 58, 207, 22);
		itogo.setBounds(250, 425, 400, 22);
		itogowo.setBounds(50, 425, 400, 22);
		itogoallLabel.setBounds(450, 425, 400, 22);
		noteLabel.setBounds(10, 450, 90, 22);
		noteText.setBounds(100, 450, 670, 22);
		
//������ ����������
		clientlistener=new ClientChoose();
		clientCombo.addActionListener(clientlistener);
		barcodeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				BarCodeFire();
			}
		});
		skladlistener=new SkladChoose();
		skladCombo.addActionListener(skladlistener);
		editableCheck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				model.setEditable(((JCheckBox)(event.getSource())).isSelected());
			}
		});
		pressF1 press=new pressF1();
		addKeyListener(press);
		skladCombo.addKeyListener(press);
//		clientCombo.
		clientCombo.getEditor().getEditorComponent().addKeyListener(press);
		editableCheck.addKeyListener(press);
		cancelButton.addKeyListener(press);
		listButton.addKeyListener(press);
		printButton.addKeyListener(press);
		saveButton.addKeyListener(press);
		barcodeButton.addKeyListener(press);
		infoButton.addKeyListener(press);
		okrCombo.addKeyListener(press);
		priceCombo.addKeyListener(press);
		naklTable.addKeyListener(press);
		noteText.addKeyListener(press);
		clientCombo.getEditor().getEditorComponent().addFocusListener(new FocusAdapter(){
		    public void focusGained(FocusEvent event){
		        clientCombo.getEditor().selectAll();
		    }
		    public void focusLost(FocusEvent event){
		    	clientChooseMet();
		    }
		});
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (model.getRowCount()>0&& JOptionPane.showConfirmDialog(parent, "��������! ��� ��������� ������ ����� �������! ����������?","��������",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION ){
					return;
				}
				model.removeAll();
//				setVisible(false);
				parent.showFrame("noVisible");
//				ChooserStreamIn.close();
				parent.closeSaleFrame();

			}
		});
		listButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (formGroup==null)
					formGroup= new ListChoose();
				if (formGroup.showDialog(parent, "����� ������",skladCombo.getSelectedItem().toString()))
					Input(formGroup.getTovar(),1);
			}
		});
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				save();
			}
		});
		model.addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent event){
				itogo.setText("����� (�������� ������): "+model.summ());
				itogowo.setText("����� (�� �������� ������): "+model.summvo());
				NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
				double curs=1;
				try{
					ResultSet rs=DataSet.QueryExec1("select curs from curs_now where id_val=(select id_val from type_price where name='"+priceCombo.getSelectedItem()+"')", false);
					if (rs.next())
						curs=rs.getDouble(1);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				itogoallLabel.setText("����� �� ���� ���������: "+formatter.format(model.summ()*curs+getItogoall()));
				if (model.getRowCount()==0){
					skladCombo.setEnabled(true);
					priceCombo.setEnabled(true);
				}
				else{
					skladCombo.setEnabled(false);
					priceCombo.setEnabled(false);
				}
			}
		});
		printButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (save()){
//					setVisible(false);
					parent.showFrame("noVisible");
					parent.closeSaleFrame();
					parent.print();
				}
			}
		});
		del.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent event){
				int row=p/naklTable.getRowHeight();
				model.removeRow(row);
			}
		});
		naklTable.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent event){
				if (event.getButton()==MouseEvent.BUTTON3){
					popup.show(naklTable, event.getX(), event.getY());
					p=event.getY();
				}
				if (event.getButton()==MouseEvent.BUTTON1 && event.getClickCount()==2){
					if (!(((JTextField)naklTable.getEditorComponent())==null)){
						((JTextField)naklTable.getEditorComponent()).addKeyListener(new KeyAdapter(){
							public void keyTyped(KeyEvent event){
								if (event.getKeyChar()==',')
									event.setKeyChar('.');
							}
						});
					}
				}
			}
		});
		naklTable.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent event){
				if (!editableCheck.isSelected())
					return;
				
				if (event.getKeyCode()==event.VK_ENTER){
					return;
				}
				if ((event.getKeyCode()>=event.VK_0 && event.getKeyCode()<=event.VK_9) || (event.getKeyChar()=='.') || (event.getKeyCode()>=event.VK_NUMPAD0 && event.getKeyCode()<=event.VK_NUMPAD9)){
					if (naklTable.getEditingColumn()==-1){
						naklTable.editCellAt(naklTable.getSelectedRow(), naklTable.getSelectedColumn());
						((JTextField)naklTable.getEditorComponent()).selectAll();
					}
				}

			}
			public void keyTyped(KeyEvent event){
				if (event.getKeyChar()==',')
					event.setKeyChar('.');
			}
		});
		infoButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				ResultSet rs;
				if (newClient==null)
					newClient=new NewClientDialog();
				newClient.setClient(((String)clientCombo.getSelectedItem()).trim());
				if (newClient.showDialog(NewSaleFrame.this, "���� ������ �������",true) && !((String)clientCombo.getSelectedItem()).trim().equals(newClient.getClient())){
					clientCombo.removeActionListener(clientlistener);
					clientCombo.removeAllItems();
					try {
						rs = DataSet.QueryExec("select rtrim(name) from client where type in (1,2) order by name",true);
						rs.next();
						while (!rs.isAfterLast()){
							clientCombo.addItem(rs.getString("rtrim(name)"));
							rs.next();
						}
					}
					catch (Exception e) { e.printStackTrace();}
					clientCombo.setSelectedItem(newClient.getClient());
					
					try {
						rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'",true);
						rs.next();
						if (rs.getInt(1)==1){
							okrLabel.setVisible(false);
							okrCombo.setVisible(false);
							priceLabel.setVisible(false);
							priceCombo.setVisible(false);
						}
					}
					catch (Exception e) { e.printStackTrace();}
					clientCombo.addActionListener(clientlistener);
				}				
			}
		});
		
//		SelectionListener listener = new SelectionListener(naklTable);
//	    naklTable.getSelectionModel().addListSelectionListener(listener);
//	    naklTable.getColumnModel().getSelectionModel().addListSelectionListener(listener);
//��������� �������� �� �����
		add(saveButton);
		add(cancelButton);
		add(barcodeButton);
		add(listButton);
		add(printButton);
		add(skladLabel);
		add(skladCombo);
		add(clientLabel);
		add(clientCombo);
		add(infoButton);
		add(ScrollTable);
		add(okrLabel);
		add(okrCombo);
		add(priceLabel);
		add(priceCombo);
		add(editableCheck);
		add(itogo);
		add(itogowo);
		add(noteLabel);
		add(noteText);
		add(itogoallLabel);
		
		clientCombo.fireActionEvent();		
		skladCombo.fireActionEvent();
		setFocusable(true);
//		this.requestFocus();
		
	}
	private class ClientChoose implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if (((JComboBox)event.getSource()).getModel().getSize()==0)
				return;
			clientChooseMet();
		}
	}
	private class SkladChoose implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if (((JComboBox)event.getSource()).getModel().getSize()==0)
				return;
			model.setSklad((String)skladCombo.getSelectedItem());
			ChooserStreamIn.setSklad((String)skladCombo.getSelectedItem());
			try {
				ResultSet rs=DataSet.QueryExec("select trim(name) from type_price where id_price=(select id_price from sklad where name = '"+(String)skladCombo.getSelectedItem()+"' )", false);
				rs.next();
				priceCombo.setSelectedItem(rs.getString(1));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clientCombo.fireActionEvent();
		}
	}
	public void clientChooseMet(){
		model.setIndDiscount(0);
		if (checkClient()){
		try {
			ResultSet rs=DataSet.QueryExec("Select type, trunc(months_between(sysdate, day)) from client where name='"+(String)clientCombo.getSelectedItem()+"'",true);
			rs.next();
//			GregorianCalendar lastEdit = new GregorianCalendar();
//			lastEdit.setTime(rs.getDate(2));
			if (rs.getInt(2)>3)
				infoButton.setBackground(Color.RED);
			else
				infoButton.setBackground(barcodeButton.getBackground());
			
			if (rs.getInt(1)==1){
				okrLabel.setVisible(false);
				okrCombo.setVisible(false);
				rs.close();
				rs=DataSet.QueryExec("select count(*) from discount where id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') and id_skl=(select id_skl from sklad where name='"+(String)skladCombo.getSelectedItem()+"')",true);
				rs.next();
				if (rs.getInt(1)>0){
					rs.close();
					rs=DataSet.QueryExec("select disc from discount where id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') and id_skl=(select id_skl from sklad where name='"+(String)skladCombo.getSelectedItem()+"')",true);
					rs.next();
					model.setIndDiscount(rs.getInt(1));}
				else{
					model.setIndDiscount(0);
				}
			}else{
				okrLabel.setVisible(true);
				okrCombo.setVisible(true);
			}
				
		}
		catch (Exception e) { e.printStackTrace();}}
		else{
			ResultSet rs;
			if (newClient==null)
				newClient=new NewClientDialog();
			newClient.setClient(((String)clientCombo.getSelectedItem()).trim());
			if (newClient.showDialog(NewSaleFrame.this, "���� ������ �������",false)){
				clientCombo.removeAllItems();
				
				try {
					rs = DataSet.QueryExec("select rtrim(name) from client where type in (1,2) order by name",true);
					rs.next();
					while (!rs.isAfterLast()){
						clientCombo.addItem(rs.getString("rtrim(name)"));
						rs.next();
					}
				}
				catch (Exception e) { e.printStackTrace();}
				clientCombo.setSelectedItem(newClient.getClient());
				
				try {
					rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'",true);
					rs.next();
					if (rs.getInt(1)==1){
						okrLabel.setVisible(false);
						okrCombo.setVisible(false);
						priceLabel.setVisible(false);
						priceCombo.setVisible(false);
					}
				}
				catch (Exception e) { e.printStackTrace();}
				
			}else{
				clientCombo.setSelectedIndex(0);
				
				try {
					rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'",true);
					rs.next();
					if (rs.getInt(1)==1){
						okrLabel.setVisible(false);
						okrCombo.setVisible(false);
						priceLabel.setVisible(false);
						priceCombo.setVisible(false);
					}
				}
				catch (Exception e) { e.printStackTrace();}

			}
		}
//		ComboBoxEditor edit=clientCombo.getEditor();
//		edit.selectAll();
//		clientCombo.getEditor().selectAll();
		ResultSet rs;
		try {
			rs = DataSet.QueryExec1("Select sum(document.sum*curs_now.curs) from document inner join curs_now on curs_now.id_val=document.id_val where (numb is NULL) and document.id_type_doc=2 and id_client=(Select id_client from client where name='"+clientCombo.getSelectedItem()+"')",true );
			rs.next();
			setItogoall(rs.getDouble(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.setClient((String)clientCombo.getSelectedItem());
		itogo.setText("����� (�������� ������): "+model.summ());
		double curs=1;
		try{
			rs=DataSet.QueryExec1("select curs from curs_now where id_val=(select id_val from type_price where name='"+priceCombo.getSelectedItem()+"')", false);
			if (rs.next())
				curs=rs.getDouble(1);
		}catch(Exception e){
			e.printStackTrace();
		}

		NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
		itogoallLabel.setText("����� �� ���� ���������: "+formatter.format(model.summ()*curs+getItogoall()));
	}
	private boolean checkClient(){
		boolean ret=false;
		
		try {
			ResultSet rs=DataSet.QueryExec("Select count(*) from client where name = '"+(String)clientCombo.getSelectedItem()+"'",true);
			rs.next();
			if (rs.getInt(1)>0){
				ret=true;
			}
			rs.close();
		}
		catch (Exception e) { }
		return ret;

	}
	
	
	public void Input(String aValue, int aCount){
		 if (aValue==null)
			return;
		 if (formInput==null)
			 formInput = new InputCountTovar();
		 int akcia=0;
		 int isakcia=0;
		 int inBox=1;
		 ResultSet rs;
		 try{
			 rs=DataSet.QueryExec("Select kol from tovar where name='"+aValue+"'",false);
			 rs.next();
			 inBox=rs.getInt(1);
			 rs.close();
		 }
		 catch (Exception e){
			 e.printStackTrace();
		 }
		 
		 double Opt,One,Box;
		 Opt=0;
		 
		 try{
			 rs=DataSet.QueryExec("select cost from price where id_tovar=(select id_tovar from tovar where name='"+aValue+"') and id_skl=(select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') and id_price=(select id_price from sklad where name='"+skladCombo.getSelectedItem()+"')",false);
			 rs.next();
			 Opt=rs.getFloat(1);
			 rs.close();
		 }
		 catch (Exception e){
			 e.printStackTrace();
		 }
		 int res=model.present(aValue);
		 Box=0;
		 if (res==-1){
			 
			 try{
				 rs=DataSet.QueryExec("select cost,akciya,isakcia from price where id_tovar=(select id_tovar from tovar where name='"+aValue+"') and id_skl=(select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') and id_price=(select id_price from type_price where name='"+(String)priceCombo.getSelectedItem()+"')",false);
				 rs.next();
				 Box=rs.getFloat(1);
				 akcia=rs.getInt(2);
				 isakcia=rs.getInt(3);
				 rs.close();
			 }
			 catch (Exception e){
				 e.printStackTrace();
			 }
		 }else{
			 Box=((Double)model.getValueAt(res, 3)).doubleValue();
		 }
		boolean roz=false;
		One=Box;
		
		
		Box=Box*(1-model.getIndDiscount()/100);
		if (res==-1){
			One=One/inBox;
			One=(int)(One*Math.pow(10, 2-okrCombo.getSelectedIndex())+0.5)/Math.pow(10, 2-okrCombo.getSelectedIndex());
		}
		try {
			rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'",true);
			rs.next();
			if (!(rs.getInt(1)==1))
				roz=true;
		}
		catch (Exception e) { e.printStackTrace();}
		double aCost=Box;
		if (roz){
			aCost=One;
		}
		int kolTov=formInput.showDialog(this, "����������", Box, Opt, One, aValue, inBox,aCount, roz);
		if (editableCheck.isSelected()){
			naklTable.requestFocus();
			if (!(((JTextField)naklTable.getEditorComponent())==null))
				((JTextField)naklTable.getEditorComponent()).postActionEvent();
			int row=model.add(aValue, kolTov, aCost, akcia, isakcia);
			naklTable.editCellAt(row, 2);
			((JTextField)naklTable.getEditorComponent()).selectAll();
		}else
			{
			int row=model.add(aValue, kolTov, aCost, akcia, isakcia);
			naklTable.setRowSelectionInterval(row, row);
			naklTable.setColumnSelectionInterval(2, 2);
			}
		if (InputCountTovar.getNext())
			BarCodeFire();
		 
	}
	private void BarCodeFire(){
		try{
			RetBarCode cod=inputBarcode.newcod(JOptionPane.showInputDialog("������� �����-���"),(String)skladCombo.getSelectedItem(),(String)priceCombo.getSelectedItem());
			Input(cod.Name,cod.Count);
			
		}
		catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			e.printStackTrace();
			// �������� ����
		}
//		if (InputCountTovar.getNext())
//			BarCodeFire();
	}
	public class pressF1 extends KeyAdapter{
		public void keyPressed(KeyEvent event){
			int keyCode=event.getKeyCode();
			if (keyCode==KeyEvent.VK_F1){
				event.setKeyCode(KeyEvent.VK_UNDEFINED);
				naklTable.requestFocus();
				BarCodeFire();
			}
		}
	}
	public void showform(){
		ResultSet rs=null;
		try{
			rs = DataSet.QueryExec("select trim(name) from sklad order by name",true);
			rs.next();
			skladCombo.removeActionListener(skladlistener);
			skladCombo.removeAllItems();
			
			while (!rs.isAfterLast()){
				skladCombo.addItem(rs.getString(1));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		skladCombo.setSelectedIndex(0);
		skladCombo.addActionListener(skladlistener);
		clientCombo.removeActionListener(clientlistener);
		try {
			rs = DataSet.QueryExec("select trim(name) from client where type in (1,2) order by upper(name)",true);
			rs.next();
			clientCombo.removeAllItems();
			while (!rs.isAfterLast()){
				clientCombo.addItem(rs.getString(1));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		clientCombo.setSelectedIndex(0);
		clientCombo.addActionListener(clientlistener);
		try {
			rs = DataSet.QueryExec("select trim(name) from type_price order by name",true);
			rs.next();
			priceCombo.removeAllItems();
			while (!rs.isAfterLast()){
				priceCombo.addItem(rs.getString(1));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		int disc=0;
		try{
			rs = DataSet.QueryExec("select disc from discount where id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') and id_skl=(select id_skl from sklad where name='"+(String)skladCombo.getSelectedItem()+"')",true);
			if (rs.next()){
				disc=rs.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			rs=DataSet.QueryExec("select trim(name) from type_price where id_price=(select id_price from sklad where name = '"+(String)skladCombo.getSelectedItem()+"' )", false);
			rs.next();
			priceCombo.setSelectedItem(rs.getString(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		okrCombo.setSelectedIndex(0);
		editableCheck.setSelected(false);
		setNote("");
		noteText.setText("");
		parent.showFrame("SaleFrame");
		skladCombo.grabFocus();
		MainWindow.Scaner.init(1, (String)skladCombo.getSelectedItem(), (String)priceCombo.getSelectedItem(), this);
	}
	public void showform(int id_doc){
		this.showform();
		ResultSet rs=null;
		String SQL;
		try{
			SQL=String.format("Select trim(c.name), trim(s.name), d.id_val, d.sum, d.note, d.disc from document d, sklad s, client c where d.id_doc=%s and d.id_client=c.id_client and d.id_skl=s.id_skl", id_doc);
			rs=DataSet.QueryExec(SQL, false);
			if (rs.next()){
				skladCombo.setSelectedItem(rs.getString(2));
				clientCombo.setSelectedItem(rs.getString(1));
				
			}
		}
	}
	private void setNote(String aValue){
		note=aValue;
	}
	private String getNote(){
		return note;
	}
	private boolean save(){
		boolean ret=true;
		setNote(noteText.getText());
		for (int i=0; i<model.getRowCount(); i++){
			if(((Integer)model.getValueAt(i, 2)).intValue()==0){
				model.removeRow(i);
				i--;
			}
		}
		for (int i=0; i<model.getRowCount(); i++){
			if(((Double)model.getValueAt(i, 3)).doubleValue()==0){
				JOptionPane.showMessageDialog(parent,"������� ���� �����������! \n ������ ������������! ","������",JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		// ���������� �����
		String SQL;
		ResultSet rs1;
		int roz=0;
		SQL="lock table document in exclusive mode";
		try{
			int id=1;
			DataSet.UpdateQuery(SQL);
			rs1=DataSet.QueryExec("select type from client where name='"+(String)clientCombo.getSelectedItem()+"'", true);
			rs1.next();
			if (rs1.getInt(1)==2){
				roz=1;
			}
			rs1=DataSet.QueryExec("select id_doc from document where id_doc=(select max(id_doc) from document)", false);
			if (rs1.next())
				id=rs1.getInt(1)+1;
			
//			if (model.summ()>model.summAkcia()){
			if (model.getRowCount()-presentAkcia()>0){
				SQL="select id_doc,sum from document where (numb is NULL) and (id_type_doc=2) and (id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"')) " +
					"and (id_skl = (select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"')) and " +
					"(disc="+model.getIndDiscount()+") and not(substr(note,1,1)='&') and id_manager=(select id_manager from manager where name='"+parent.GetUserName()+"')" +
							" and note='-"+getNote()+"'" ;
				rs1=DataSet.QueryExec(SQL, false);
				if (rs1.next()){
					id=rs1.getInt(1);
					SQL="update document set sum="+(rs1.getDouble(2)+model.summ()-model.summAkcia())+" where id_doc="+id;
				}else{
					SQL="insert into document (id_type_doc, id_doc, id_client, id_skl, id_val, sum, note, disc, id_manager) select 2 as id_type_doc,"+id+" as id_doc"+
						", (select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') as id_client" +
						", (select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') as id_skl"+
						", (select distinct id_val from type_price where name='"+(String)priceCombo.getSelectedItem()+"') as id_val" +
						", "+(model.summ()-model.summAkcia())+" as sum ,'-"+getNote()+"' as note, "+model.getIndDiscount()+" as disc, " +
						" id_manager from manager where name='"+parent.GetUserName()+"'";
				}
				DataSet.UpdateQuery(SQL);
				for (int i=0;i<model.getRowCount();i++){
					if (!(model.getAkcia(i))){
						SQL="insert into lines (id_doc,kol,cost,disc,id_tovar) select "+id+" as id_doc, (select "+model.getValueAt(i,2)+"/(tovar.kol*"+roz+"+"+(1-roz)+") from tovar where name='"+model.getValueAt(i, 1)+"')"+
							" as kol, (select "+model.getValueAt(i,3)+"*(tovar.kol*"+roz+"+"+(1-roz)+") from tovar where name='"+model.getValueAt(i, 1)+"')"+" as cost, "+model.getValueAt(i, 5)+" as disc, id_tovar from tovar where name='"+
							model.getValueAt(i, 1)+"'";
						DataSet.UpdateQuery(SQL);
					}
				}
				rs1=DataSet.QueryExec("select id_doc from document where id_doc=(select max(id_doc) from document)", false);
				if (rs1.next())
					id=rs1.getInt(1)+1;
			}
			if (presentAkcia()>0){
				SQL="select id_doc,sum from document where (numb is NULL) and (id_type_doc=2) and (id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"')) " +
					"and (id_skl = (select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"')) and " +
					"(disc=0)and (substr(note,1,1)='&') and id_manager=(select id_manager from manager where name='"+parent.GetUserName()+"')" +
							" and note='&"+getNote()+"'" ;
				rs1=DataSet.QueryExec(SQL, false);
				if (rs1.next()){
					id=rs1.getInt(1);
					SQL="update document set sum="+(rs1.getDouble(2)+model.summAkcia())+" where id_doc="+id;
				}else{
					SQL="insert into document (id_type_doc, id_doc, id_client, id_skl, id_val, sum, note, disc, id_manager) select 2 as id_type_doc,"+id+" as id_doc"+
						", (select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') as id_client" +
						", (select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') as id_skl"+
						", (select distinct id_val from type_price where name='"+(String)priceCombo.getSelectedItem()+"') as id_val" +
						", "+model.summAkcia()+" as sum ,'&"+getNote()+"' as note, 0 as disc, " +
						" id_manager from manager where name='"+parent.GetUserName()+"'";
				}
				DataSet.UpdateQuery(SQL);
				for (int i=0;i<model.getRowCount();i++){
					if (model.getAkcia(i)){
						SQL="insert into lines (id_doc,kol,cost,disc,id_tovar) select "+id+" as id_doc, (select "+model.getValueAt(i,2)+"/(tovar.kol*"+roz+"+"+(1-roz)+") from tovar where name='"+model.getValueAt(i, 1)+"')"+
							" as kol, (select "+model.getValueAt(i,3)+"*(tovar.kol*"+roz+"+"+(1-roz)+") from tovar where name='"+model.getValueAt(i, 1)+"')"+" as cost, "+model.getValueAt(i, 5)+" as disc, id_tovar from tovar where name='"+
							model.getValueAt(i, 1)+"'";
						DataSet.UpdateQuery(SQL);
					}
			}

			}
			DataSet.commit();
			model.removeAll();
			String name= (String)clientCombo.getSelectedItem();
			showform();
			clientCombo.setSelectedItem(name);
//			setVisible(false);
//			ChooserStreamIn.close();
//			parent.closeSaleFrame();
		}
		catch(Exception e){
			 try {
				DataSet.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 ret=false;
			 e.printStackTrace();
		}
		return ret;
	}
	private int presentAkcia(){
		int ret=0;
		for (int i=0;i<model.getRowCount();i++)
			if (model.getAkcia(i))
				ret++;
		return ret;
	}
	@Override
	public boolean closeform(){
		if (model.getRowCount()>0&& JOptionPane.showConfirmDialog(parent, "��������! ��� ��������� ������ ����� �������! ����������?","��������",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION ){
			return false;
		}
		model.removeAll();
//		setVisible(false);
		parent.showFrame("noVisible");
		parent.closeSaleFrame();
//		ChooserStreamIn.close();
		return true;
	}
	public double getItogoall() {
		return itogoall;
	}
	public void setItogoall(double itogoall) {
		this.itogoall = itogoall;
	}
	public boolean isChanged() {
		return Changed;
	}
	public void setChanged(boolean changed) {
		Changed = changed;
	}
	public int getId_doc() {
		return id_doc;
	}
	public void setId_doc(int idDoc) {
		id_doc = idDoc;
	}
	

}
class JComboBoxFire extends JComboBox{
	public void fireActionEvent()
	{
		super.fireActionEvent();
	}
}


/*class SelectionListener implements ListSelectionListener {
	private JTable table;
	public SelectionListener(JTable table){
		this.table=table;
	}
	public void valueChanged(ListSelectionEvent event){
		if (table.getEditingColumn()==-1 && table.getSelectedColumn()==2){
			int row=table.getSelectedRow();
			table.changeSelection(row, 3, false, false);
			if (table.getCellEditor() != null) {
				table.getCellEditor().stopCellEditing();
		    }
			return;
		}
		if (table.getEditingColumn()==-1 && table.getSelectedColumn()==3){
			
			int row=table.getSelectedRow();
			return;
		    }
		
	}


}

*/
