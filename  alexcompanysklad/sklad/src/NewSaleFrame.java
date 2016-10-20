 import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;




//import javax.swing.JComboBox.KeySelectionManager;





class NewSaleFrame extends MyPanel
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static int MIN_SUMM_FOR_CARD=20000;
	
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
//	private JButton sumForSale;
	private naklTableModel model;
	public static InputCountTovar formInput = null;
	public int clientKoeficientForDiscount=0;
	private static ListChoose formGroup=null;
	private JButton barcodeButton;
	private MyTable naklTable;
	private JLabel isInputCard = new JLabel("Карта не проведена");
	public MainFrame parent;
	private JCheckBox editableCheck;
	private String note;
	private JPopupMenu popup;
	private int p;
	private double itogoall=0.0;
	private double koefForPrice=1;
	private JTextField noteText;
	private ActionListener clientlistener;
	private ActionListener skladlistener;
	private boolean Checking=false;
//	private Double sumForSaleInOtherDoc=0.0;
	private JCheckBox isKoefForPrice;
	private JTextField fieldForInputKoefForPrice;
//	private String oldClientName="";
	
	private int id_doc;
	private JButton infoButton;
	public NewSaleFrame()
	{
		setLayout(null);
		JButton saveButton = new JButton("Сохранить");
		JButton cancelButton = new JButton("Отмена");
		barcodeButton = new JButton("Штрих-код(F1)");
		JButton listButton = new JButton("Выбрать из списка");
		JButton printButton = new JButton("Печать");
		JLabel skladLabel = new JLabel("Склад:");
		JLabel clientLabel = new JLabel("Клиент:");
		isInputCard.setForeground(Color.RED);
		isInputCard.setFont(new Font("Arial",Font.BOLD,12));
		JLabel noteLabel = new JLabel("Примечание");
		noteText = new JTextField("");
		itogo = new JLabel("Итого (учитывая скидку): 0,00");
		itogowo = new JLabel("Итого (не учитывая скидку): 0,00");
		itogoallLabel= new JLabel("Итого по всем накладным: 0,00");
//		sumForSale=new JButton("Сумма по акции");
//		sumForSale.setVisible(MainFrame.isSale());
		priceLabel = new JLabel("Прайс:");
		okrLabel = new JLabel("Округление:");
		okrCombo = new JComboBox();
		okrCombo.addItem("Без округления");
		okrCombo.addItem("До 0,1");
		okrCombo.addItem("До 1");
		infoButton = new JButton("Информация");
		editableCheck = new JCheckBox("Редактировать в ячейке",false);
		skladCombo = new JComboBoxFire();
		clientCombo = new AutoComplete();
		clientCombo.setEditable(true);
		priceCombo=new JComboBox();
		isKoefForPrice= new JCheckBox("Процент",false);
		fieldForInputKoefForPrice=new JTextField("0");
		fieldForInputKoefForPrice.setVisible(false);
		model = new naklTableModel("","",new IndividualDiscount(),true);
		
		naklTable=new MyTable(model);
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
		JMenuItem del = new JMenuItem("Удалить строку");
		popup.add(del);
//		naklTable.setComponentPopupMenu(popup);

		saveButton.setBounds(34, 495, 104, 22);
		cancelButton.setBounds(159, 495, 104, 22);
		barcodeButton.setBounds(285, 495, 150, 22);
		listButton.setBounds(455, 495, 163, 22);
		printButton.setBounds(638, 495, 104, 22);
		skladLabel.setBounds(10, 1, 58, 22);
		skladCombo.setBounds(79, 1, 207, 22);
		clientLabel.setBounds(10, 28, 61, 22);
		clientCombo.setBounds(79, 28, 207, 22);
		isInputCard.setBounds(10, 58, 160, 22);
		infoButton.setBounds(296, 28, 104, 22);
		ScrollTable.setBounds(6, 89, 769, 335);
		priceLabel.setBounds(407, 1, 86, 22);
		priceCombo.setBounds(505, 1, 170, 22);
		okrLabel.setBounds(407, 28, 86, 22);
		okrCombo.setBounds(505, 28, 170, 22);	
		editableCheck.setBounds(555, 58, 207, 22);
		itogo.setBounds(220, 425, 200, 22);
		itogowo.setBounds(10, 425, 210, 22);
		itogoallLabel.setBounds(420, 425, 200, 22);
//		sumForSale.setBounds(650, 425, 120, 22);
		noteLabel.setBounds(10, 450, 90, 22);
		noteText.setBounds(100, 450, 670, 22);
		isKoefForPrice.setBounds(690, 1, 70, 22);
		fieldForInputKoefForPrice.setBounds(690, 28, 70, 22);
		
//Задаем слушателей
		clientlistener=new ClientChoose();
		
		
		ActionListener[] listeners=clientCombo.getActionListeners();
		for (ActionListener element:listeners){
			clientCombo.removeActionListener(element);
		}
		clientCombo.addActionListener(clientlistener);
		for (ActionListener element:listeners){
			clientCombo.addActionListener(element);
		}
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
		isKoefForPrice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fieldForInputKoefForPrice.setText("0");
				setKoefForPrice(0);
				fieldForInputKoefForPrice.setVisible(((JCheckBox)(e.getSource())).isSelected());
				
			}
		});
		fieldForInputKoefForPrice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setKoefForPrice(new Integer(((JTextField)(e.getSource())).getText()));
				}catch(Exception exc){
					exc.printStackTrace();
					JOptionPane.showMessageDialog(null, "Необходимо вводить целое число!");
					((JTextField)(e.getSource())).setText("0");
					setKoefForPrice(0);
				}
				
				
			}
		});
		fieldForInputKoefForPrice.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				try {
					setKoefForPrice(new Integer(((JTextField)(e.getSource())).getText()));
				}catch(Exception exc){
					exc.printStackTrace();
					JOptionPane.showMessageDialog(null, "Необходимо вводить целое число!");
					((JTextField)(e.getSource())).setText("0");
					setKoefForPrice(0);
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				((JTextField)(e.getSource())).selectAll();
				
			}
		});
		pressF1 press=new pressF1();
		addKeyListener(press);
		skladCombo.addKeyListener(press);
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
		isKoefForPrice.addKeyListener(press);
		fieldForInputKoefForPrice.addKeyListener(press);
		clientCombo.getEditor().getEditorComponent().addFocusListener(new FocusAdapter(){
		    public void focusGained(FocusEvent event){
		        clientCombo.getEditor().selectAll();
		    }
		    public void focusLost(FocusEvent event){
		    	System.out.println("Вошел фокус");
		    	clientChooseMet(true);
		    	
		    }
		});
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (getId_doc()==0&&model.getRowCount()>0&& JOptionPane.showConfirmDialog(parent, "Внимание! Все введенные данные будут удалены! Продолжить?","Удаление",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION ){
					return;
				}
				if (getId_doc()!=0&&model.isChanged()&&JOptionPane.showConfirmDialog(parent, "Внимание! Все изменения будут удалены! Продолжить?","Отмена",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION)
					return;
				try{
					DataSet.rollback1();
				}catch(Exception e){
					e.printStackTrace();
				}
				
				model.removeAll();
//				setVisible(false);
				parent.showFrame("noVisible");
//				ChooserStreamIn.close();
				parent.closeSaleFrame();
				isKoefForPrice.setSelected(false);
				fieldForInputKoefForPrice.setText("0");
				setKoefForPrice(0);
			}
		});
		listButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (formGroup==null)
					formGroup= new ListChoose();
				if (formGroup.showDialog(parent, "Выбор товара",skladCombo.getSelectedItem().toString()))
					Input(formGroup.getTovar(),1);
			}
		});
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (isOld()){
					JOptionPane.showMessageDialog(null, "Информация устарела. \n Пока не обновите работать не будет!", "Информация", JOptionPane.ERROR_MESSAGE);
					return;
					}
				save();
			}
		});
/*		sumForSale.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NumberFormat formatter = new DecimalFormat ("0.00") ;
				JOptionPane.showMessageDialog(null, String.format("Сумма по акции: %s", formatter.format(parent.CalcSumForSale((String)clientCombo.getSelectedItem())+getSumForSaleInCurrentDoc())), "Акция!", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
*/		
		model.addTableModelListener(modlis);
		printButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (isOld()){
					JOptionPane.showMessageDialog(null, "Информация устарела. \n Пока не обновите работать не будет!", "Информация", JOptionPane.ERROR_MESSAGE);
					return;
					}
				if (save()){
//					setVisible(false);
					parent.showFrame("noVisible");
					parent.closeSaleFrame();
					isKoefForPrice.setSelected(false);
					fieldForInputKoefForPrice.setText("0");
					setKoefForPrice(0);
					parent.newPrint();
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
				
				if (event.getKeyCode()==KeyEvent.VK_ENTER){
					return;
				}
				if ((event.getKeyCode()>=KeyEvent.VK_0 && event.getKeyCode()<=KeyEvent.VK_9) || (event.getKeyChar()=='.') || (event.getKeyCode()>=KeyEvent.VK_NUMPAD0 && event.getKeyCode()<=KeyEvent.VK_NUMPAD9)){
					if (naklTable.getEditingColumn()==-1){
						naklTable.editCellAt(naklTable.getSelectedRow(), naklTable.getSelectedColumn());
						((JTextField)naklTable.getEditorComponent()).selectAll();
					}
				}// else event.setKeyCode(event.);

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
				if (newClient.showDialog(NewSaleFrame.this, "Ввод нового клиента",true) && !((String)clientCombo.getSelectedItem()).trim().equals(newClient.getClient())){
					clientCombo.removeActionListener(clientlistener);
					clientCombo.removeAllItems();
					try {
						rs = DataSet.QueryExec("select rtrim(name) from client where type in (1,2,3) order by name",true);
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
						if (rs.getInt(1)!=2){
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
//Добавляем элементы на форму
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
//		add(sumForSale);
		add(noteLabel);
		add(noteText);
		add(itogoallLabel);
		add(isKoefForPrice);
		add(fieldForInputKoefForPrice);
		add(isInputCard);
		clientCombo.fireActionEvent();		
		skladCombo.fireActionEvent();
		setFocusable(true);
//		this.requestFocus();
		
	}
	private class ClientChoose implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if (((JComboBox)event.getSource()).getModel().getSize()==0)
				return;
			clientChooseMet(false);
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
				e.printStackTrace();
			}
//			IndividualDiscount indDisc=new IndividualDiscount(0);
//			model.setIndDiscount(indDisc);
			
//			clientCombo.fireActionEvent();
		}
	}
	public void clientChooseMet(boolean focusLost){
//		long timing;

		if (Checking||(!clientCombo.isSelected()&&!focusLost))
			return;
    	System.out.println("Вошел action");
		IndividualDiscount indDisc=new IndividualDiscount();
		model.setIndDiscount(indDisc);
		
		if (isPresentClientNameInBase()){
		try {
			String SQL=String.format("Select c.type, trunc(months_between(sysdate, c.day)),c.id_client,nvl(c.iscardinput,0),nvl(c.card_numb,0), nvl(t.sum,0) from client c,(select id_client, sum(sum)/12 as sum from DOCUMENT where day between SYSDATE-365 and SYSDATE group by id_client) t where name='%s' and t.ID_CLIENT (+) = c.ID_CLIENT ", clientCombo.getSelectedItem());
			ResultSet rs=DataSet.QueryExec(SQL,false);
			rs.next();
			clientKoeficientForDiscount=rs.getInt(4);
			int typeClient=rs.getInt(1);
			if (typeClient==1 && clientKoeficientForDiscount==1){
				isInputCard.setForeground(Color.GREEN);
				isInputCard.setText("Карта проведена");
				}
			else if (typeClient==1 && rs.getInt(5)!=0) {
				isInputCard.setForeground(Color.RED);
				isInputCard.setText("Карта не проведена");
			} else if (typeClient==1 && rs.getInt(6)>MIN_SUMM_FOR_CARD) {
				isInputCard.setForeground(Color.RED);
				isInputCard.setText("Выдать карту (F12)");
			}else{
				isInputCard.setForeground(Color.GREEN);
				isInputCard.setText("Без карты");
			}
			isInputCard.repaint();
			if (rs.getInt(2)>3)
				{infoButton.setBackground(Color.RED);
				infoButton.setForeground(Color.RED);
				}
			else
				{infoButton.setBackground(barcodeButton.getBackground());
				infoButton.setForeground(barcodeButton.getForeground());
				}
			int id_client=rs.getInt(3);
			if (typeClient!=2){
				okrLabel.setVisible(false);
				okrCombo.setVisible(false);
				rs.close();
				
				SQL=String.format("Select id_skl from sklad where name='%s'", (String)skladCombo.getSelectedItem());
// 2
//				timing=(new GregorianCalendar()).getTimeInMillis();
				rs=DataSet.QueryExec(SQL, false);
//				System.out.println("2: "+((new GregorianCalendar()).getTimeInMillis()-timing));
				
				rs.next();
				int id_skl=rs.getInt(1);
				rs.close();		
				model.setIndDiscount(getDiscForClient(id_client,clientKoeficientForDiscount));
			}else{
				okrLabel.setVisible(true);
				okrCombo.setVisible(true);
			}
				
		}
		catch (Exception e) { e.printStackTrace();}}
		else {
			Checking=true;
			if(JOptionPane.showConfirmDialog(NewSaleFrame.this, "Ввести нового клиента?", "Ввод нового клиента", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION && JOptionPane.showConfirmDialog(NewSaleFrame.this, "Вы уверены что нужно ввести нового клиента?", "Ввод нового клиента", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
			ResultSet rs;
			if (newClient==null)
				newClient=new NewClientDialog();
			newClient.setClient(((String)clientCombo.getSelectedItem()).trim());
			if (newClient.showDialog(NewSaleFrame.this, "Ввод нового клиента",false)){
				clientCombo.removeAllItems();
				
				try {
					rs = DataSet.QueryExec("select rtrim(name) from client where type in (1,2,3) order by name",false);
					rs.next();
					while (!rs.isAfterLast()){
						clientCombo.addItem(rs.getString("rtrim(name)"));
						rs.next();
					}
				}
				catch (Exception e) { e.printStackTrace();}
				clientCombo.setSelectedItem(newClient.getClient());
				
				try {
					rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'",false);
					rs.next();
					if (rs.getInt(1)!=2){
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
					rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'",false);
					rs.next();
					if (rs.getInt(1)!=2){
						okrLabel.setVisible(false);
						okrCombo.setVisible(false);
						priceLabel.setVisible(false);
						priceCombo.setVisible(false);
					}
				}
				catch (Exception e) { e.printStackTrace();}

			}
		}else{
			clientCombo.setSelectedIndex(0);
		}
			}
		Checking=false;
		ResultSet rs;
		try {
// 3
//			timing=(new GregorianCalendar()).getTimeInMillis();

			rs = DataSet.QueryExec("Select sum(document.sum*curs_now.curs) from document inner join curs_now on curs_now.id_val=document.id_val where (numb is NULL) and document.id_type_doc=2 and id_client=(Select id_client from client where name='"+clientCombo.getSelectedItem()+"')",false );
//			System.out.println("3: "+((new GregorianCalendar()).getTimeInMillis()-timing));
			rs.next();
			setItogoall(rs.getDouble(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.setClient((String)clientCombo.getSelectedItem());
		itogo.setText("Итого (учитывая скидку): "+model.summ());
//		sumForSale.setVisible(MainFrame.isSale() && !okrCombo.isVisible());
		double curs=1;
		try{
// 4
//			timing=(new GregorianCalendar()).getTimeInMillis();

			rs=DataSet.QueryExec("select curs from curs_now where id_val=(select id_val from type_price where name='"+priceCombo.getSelectedItem()+"')", false);
//			System.out.println("4: "+((new GregorianCalendar()).getTimeInMillis()-timing));			
			if (rs.next())
				curs=rs.getDouble(1);
		}catch(Exception e){
			e.printStackTrace();
		}

		NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
		if (getId_doc()!=0){
			try{
				String SQL=String.format("Select trim(c.name) from client c,document d where d.id_doc=%s and d.id_client=c.id_client", getId_doc());
// 5
//				timing=(new GregorianCalendar()).getTimeInMillis();
				
				rs=DataSet.QueryExec(SQL, false);
//				System.out.println("5: "+((new GregorianCalendar()).getTimeInMillis()-timing));
				
				if (rs.next()&&clientCombo.getSelectedItem().equals(rs.getString(1))){
					SQL=String.format("Select sum*(1-disc/100) from document where id_doc=%s", getId_doc());
// 6
//					timing=(new GregorianCalendar()).getTimeInMillis();

					rs=DataSet.QueryExec(SQL, false);
//					System.out.println("6: "+((new GregorianCalendar()).getTimeInMillis()-timing));					
					
					rs.next();
					setItogoall(getItogoall()-rs.getDouble(1)*curs);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		itogoallLabel.setText("Итого по всем накладным: "+formatter.format(model.summ()*curs+getItogoall()));
		this.transferFocus();

	}
	private boolean isPresentClientNameInBase(){
		boolean ret=false;
		
		try {
			ResultSet rs=DataSet.QueryExec("Select count(*) from client where name = '"+(String)clientCombo.getSelectedItem()+"'",false);
			rs.next();
			if (rs.getInt(1)>0){
				ret=true;
			}
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret;

	}
	private boolean isOld(){
		try{
			ResultSet rs=DataSet.QueryExec("Select trunc(months_between(sysdate, day)) from client where name='"+(String)clientCombo.getSelectedItem()+"'",false);
			rs.next();
			if (rs.getInt(1)>3) 
				return true;
			else
				return false;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private boolean IsBan(String nameOfItem){
		try{
			String SQL=String.format("select max(nvl(g.ban,0)) from groupid g, kart k, tovar t where t.name= '%s' and k.id_tovar = t.id_tovar and k.id_group = g.id_group", nameOfItem);
			ResultSet rs=DataSet.QueryExec(SQL, false);
			if (rs.next()) {
				return rs.getInt(1)==1;
			}
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ошибка проверки запрета продаж. \nОбратитесь к администратору.", "Ошибка!", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	public void Input(String aValue, int aCount){
		 if (aValue==null)
			return;
		 if (formInput==null)
			 formInput = new InputCountTovar();
		 if (IsBan(aValue)){
			 JOptionPane.showMessageDialog(null, String.format("Продажа этого товара запрешена \n %s",aValue), "Операция запрещена", JOptionPane.ERROR_MESSAGE);
			 return;
		 }
		 int akcia=0;
		 int isakcia=0;
		 int inBox=1;
		 int id_group=0;
		 int id_tovar;
		 ResultSet rs;
		 String SQL;
		 try{
			 rs=DataSet.QueryExec("Select kol,id_tovar from tovar where name='"+aValue+"'",false);
			 rs.next();
			 inBox=rs.getInt(1);
			 id_tovar=rs.getInt(2);
			 rs.close();
			 SQL=String.format("Select distinct id_group from kart where id_tovar=%s", id_tovar);
			 rs=DataSet.QueryExec(SQL, false);
			 rs.next();
			 id_group=rs.getInt(1);
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
				 SQL=String.format("select cost*%s,akciya,isakcia from price where id_tovar=(select id_tovar from tovar where name='%s') and id_skl=(select id_skl from SKLAD where name='%s') and id_price=(select id_price from type_price where name='%s')",getKoefForPrice(), aValue,(String)skladCombo.getSelectedItem(),(String)priceCombo.getSelectedItem());
				 rs=DataSet.QueryExec(SQL, false);
//				 rs=DataSet.QueryExec("select cost,akciya,isakcia from price where id_tovar=(select id_tovar from tovar where name='"+aValue+"') and id_skl=(select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') and id_price=(select id_price from type_price where name='"+(String)priceCombo.getSelectedItem()+"')",false);
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
		double aCost=Box;
		
		Box=Box*(1-new Double(model.getIndDiscount(id_group))/100);
		if (res==-1){
			One=One/inBox;
			One=(int)(One*Math.pow(10, 2-okrCombo.getSelectedIndex())+0.5)/Math.pow(10, 2-okrCombo.getSelectedIndex());
		}
		try {
			rs=DataSet.QueryExec("Select type from client where name='"+(String)clientCombo.getSelectedItem()+"'",true);
			rs.next();
			if (rs.getInt(1)==2)
				roz=true;
		}
		catch (Exception e) { e.printStackTrace();}
		
		if (roz){
			aCost=One;
		}
		int kolTov=formInput.showDialog(this, "Количество", Box, Opt, One, aValue, inBox,aCount, roz);
		if (editableCheck.isSelected()){
			naklTable.requestFocus();
			if (!(((JTextField)naklTable.getEditorComponent())==null))
				((JTextField)naklTable.getEditorComponent()).postActionEvent();
			int row=model.add(aValue, kolTov, aCost, akcia, isakcia, id_group);
			naklTable.setRowSelectionInterval(row, row);
			naklTable.setColumnSelectionInterval(2, 2);
			naklTable.editCellAt(row, 2);
			((JTextField)naklTable.getEditorComponent()).selectAll();
			naklTable.scrollRectToVisible(naklTable.getCellRect(row, 2, false));
		}else
			{
			int row=model.add(aValue, kolTov, aCost, akcia, isakcia, id_group);
			naklTable.setRowSelectionInterval(row, row);
			naklTable.setColumnSelectionInterval(2, 2);
			naklTable.scrollRectToVisible(naklTable.getCellRect(row, 2, false));
			}
		if (InputCountTovar.getNext())
			BarCodeFire();
		 
	}
	private void BarCodeFire(){
		try{
			RetBarCode cod=InputBarcode.newcod(JOptionPane.showInputDialog("Введите штрих-код"),(String)skladCombo.getSelectedItem(),(String)priceCombo.getSelectedItem());
			Input(cod.Name,cod.Count);
			
		}
		catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			e.printStackTrace();
		}
	}
	public class pressF1 extends KeyAdapter{
		public void keyPressed(KeyEvent event){
			int keyCode=event.getKeyCode();
			if (keyCode==KeyEvent.VK_F1){
				event.setKeyCode(KeyEvent.VK_UNDEFINED);
				naklTable.requestFocus();
				BarCodeFire();
			}
			if (keyCode==KeyEvent.VK_F11){
				event.setKeyCode(KeyEvent.VK_UNDEFINED);
				String cod=JOptionPane.showInputDialog(null, "Введите номер карты клиента", "Ввод карты", JOptionPane.QUESTION_MESSAGE);
				for (int i=cod.length();i<12;i++){
					cod="0"+cod;
				}
				getClientCodFromScaner(cod);
			}
			if (keyCode==KeyEvent.VK_F12){
				event.setKeyCode(KeyEvent.VK_UNDEFINED);
				if (!isInputCard.getText().equals("Выдать карту (F12)")){
					return;
				}
				int icod=0;
				String cod="";
				do
				{
				cod=JOptionPane.showInputDialog(null, String.format("Введите номер новой карты для %s",clientCombo.getSelectedItem()), "Выдача карты", JOptionPane.QUESTION_MESSAGE);
				
				try{
					if (cod==null)
						return;
					icod=Integer.parseInt(cod, 10);
					if (!(icod<10001 && icod>99))
						throw new NumberFormatException();
				}catch(NumberFormatException ex){
					JOptionPane.showMessageDialog(null, "Введите корректный номер от 100 до 10000", "Неверный номер", JOptionPane.ERROR_MESSAGE);
				}
				
				}while(!(icod<10001 && icod>99));
				for (int i=cod.length();i<12;i++){
					cod="0"+cod;
				}
				String SQL=String.format("Update client set card_numb='%s' where name='%s'", cod,clientCombo.getSelectedItem());
				try{
					DataSet.QueryExec(SQL, true);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				getClientCodFromScaner(cod);
			}

		}
	}
	public void showform(){
		initcombo();
		setId_doc(0);
		setNote("");
		noteText.setText("");
		parent.showFrame("SaleFrame");
		skladCombo.grabFocus();
		model.fireTableDataChanged();
		model.setChanged(false);
		MainWindow.Scaner.init(1, (String)skladCombo.getSelectedItem(), (String)priceCombo.getSelectedItem(), this);
	}
	public void showform(int id_doc){
		initcombo();
		ResultSet rs=null;
		String SQL;
		model.removeTableModelListener(modlis);
		try{
			try{
				SQL=String.format("Select d.*,l.* from document d,lines l where d.id_doc=%s and l.id_doc=d.id_doc for update nowait", id_doc);
				DataSet.QueryExec1(SQL, false);
			}catch(Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Документ заблокирован другим пользователем! Попробуйте позже.", "Ошибка блокировки", JOptionPane.ERROR_MESSAGE);
				return;
			}
			SQL=String.format("Select trim(c.name), trim(s.name),trim(p.name), trim(d.note), d.disc, c.type, p.id_price, s.id_skl, c.id_client from document d, sklad s, client c, type_price p where d.id_doc=%s and d.id_client=c.id_client and d.id_skl=s.id_skl and p.id_price=d.id_price", id_doc);
			rs=DataSet.QueryExec1(SQL, false);
			if (rs.next()){
				setId_doc(id_doc);
				skladCombo.setSelectedItem(rs.getString(2));
				clientCombo.setSelectedItem(rs.getString(1));
				priceCombo.setSelectedItem(rs.getString(3));
				model.setIndDiscount(getDiscForClient(rs.getInt(9),clientKoeficientForDiscount));
//				model.setIndDiscount(new IndividualDiscount(rs.getInt(5)));
				setNote(rs.getString(4).substring(1));
				noteText.setText(getNote());
				int isakciya=(rs.getString(4).charAt(0)=='&'?1:0);
				if (rs.getInt(6)==2)
					SQL=String.format("Select trim(t.name), l.kol*t.kol, l.cost/t.kol, l.disc, k.ID_GROUP from lines l, tovar t, (select distinct id_tovar, id_group from kart) k where l.id_doc=%s and l.id_tovar=t.id_tovar and l.ID_TOVAR=k.ID_TOVAR", id_doc);
				else
					SQL=String.format("Select trim(t.name), l.kol, l.cost, p.AKCIYA, k.ID_GROUP from lines l, tovar t, price p, (select distinct id_tovar, id_group from kart) k where l.id_doc=%s and l.id_tovar=t.id_tovar and l.ID_TOVAR=k.ID_TOVAR and p.ID_PRICE=%s and p.ID_TOVAR=l.ID_TOVAR", id_doc,rs.getInt(7));
				rs=DataSet.QueryExec1(SQL, false);
				while (rs.next()){
					model.add(rs.getString(1), rs.getInt(2), rs.getDouble(3), rs.getInt(4), isakciya, rs.getInt(5));
				}
//				parent.showFrame("noVisible");
				parent.showFrame("SaleFrame");
				skladCombo.grabFocus();
				MainWindow.Scaner.init(1, (String)skladCombo.getSelectedItem(), (String)priceCombo.getSelectedItem(), this);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addTableModelListener(modlis);
		model.fireTableDataChanged();
		model.setChanged(false);
	}
	private void setNote(String aValue){
		note=aValue;
		model.setChanged(true);
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
				JOptionPane.showMessageDialog(parent,"Нулевые цены недопустимы! \n Будьте внимательней! ","Ошибка",JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		//Создаем модель для строк с отдельными скидками
/*		naklTableModel LinesWithPersonDiscount;
		LinesWithPersonDiscount= new naklTableModel((String)clientCombo.getSelectedItem(), (String)skladCombo.getSelectedItem(), 0, true);
		int ii=0;
		//for (int i=0;i<model.getRowCount(); i++){
		while (ii<model.getRowCount()){	
			if (!model.getAkcia(ii) && isPersonalDiscount((String)clientCombo.getSelectedItem(), (String)skladCombo.getSelectedItem(), (String)model.getValueAt(ii, 1))){
				LinesWithPersonDiscount.add((String)model.getValueAt(ii, 1), (Integer)model.getValueAt(ii, 2), (Double)model.getValueAt(ii, 3), personalDiscount((String)clientCombo.getSelectedItem(), (String)skladCombo.getSelectedItem(), (String)model.getValueAt(ii, 1)), 0);
				model.removeRow(ii);
			}else{
				ii++;
			};
		}
*/		
		// Записываем шапку
		String SQL;
		ResultSet rs1;
		if (getId_doc()!=0&&!model.isChanged()){
			model.removeAll();
			String name= (String)clientCombo.getSelectedItem();
			showform();
			clientCombo.setSelectedItem(name);
			return true;
		}
		if (getId_doc()!=0){
			try{
				DataSet.commit1();
				SQL =String.format("delete from lines where id_doc=%s", getId_doc());
				DataSet.UpdateQuery(SQL);
				SQL =String.format("delete from document where id_doc=%s", getId_doc());
				DataSet.UpdateQuery(SQL);

			}catch(Exception e){
				e.printStackTrace();
				try{
					DataSet.rollback();
					JOptionPane.showMessageDialog(null, "Ошибка записи, повторите попытку!", "Ошибка", JOptionPane.ERROR_MESSAGE);
					return false;
				}catch(Exception exp){
					exp.printStackTrace();
					JOptionPane.showMessageDialog(null, "Критическая ошибка. Обратитесь к администратору!/n(ERROR-01)", "Ошибка", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		int roz=0;
//		SQL="lock table document in exclusive mode";
		SQL=String.format("Select * from document where id_client=(select max(id_client) from client where name='%s') and numb is null for update nowait",(String)clientCombo.getSelectedItem());
		
		try{
			int id=1;
			DataSet.UpdateQuery(SQL);
			rs1=DataSet.QueryExec("select type from client where name='"+(String)clientCombo.getSelectedItem()+"'", true);
			rs1.next();
			if (rs1.getInt(1)==2){
				roz=1;
			}
			rs1=DataSet.QueryExec("select max(id_doc) from document", false);
			if (rs1.next())
				id=rs1.getInt(1)+1;
			
				

//Запись шапки новая
			
			if (model.getRowCount()-presentAkcia()>0){
				SQL="select id_doc,sum from document where (numb is NULL) and (id_type_doc=2) and (id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"')) " +
					"and (id_skl = (select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"')) and " +
					"(disc=0) and not(substr(note,1,1)='&') and id_manager=(select id_manager from manager where name='"+parent.GetUserName()+"')" +
							" and note='-"+getNote()+"' and id_price=(select id_price from type_price where name ='"+(String)priceCombo.getSelectedItem()+"')" ;
				rs1=DataSet.QueryExec(SQL, false);
				if (rs1.next()){
					id=rs1.getInt(1);
					SQL="update document set sum="+(rs1.getDouble(2)+model.summ()-model.summAkcia())+" where id_doc="+id;
				}else{
					SQL="insert into document (id_type_doc, id_doc, id_client, id_skl, id_val, sum, note, disc, id_price,isclientcard, id_manager) select 2 as id_type_doc,"+id+" as id_doc"+
						", (select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') as id_client" +
						", (select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') as id_skl"+
						", (select distinct id_val from type_price where name='"+(String)priceCombo.getSelectedItem()+"') as id_val" +
						", "+(model.summ()-model.summAkcia())+" as sum ,'-"+getNote()+"' as note, 0 as disc" +
						", (select id_price from type_price where name='"+(String)priceCombo.getSelectedItem()+"') as id_price "+ 
						", " +clientKoeficientForDiscount+ " as isclientcard,"+
						" id_manager from manager where name='"+parent.GetUserName()+"'";
				}
			
			
			
				DataSet.UpdateQuery(SQL);
				for (int i=0;i<model.getRowCount();i++){
					if (!(model.getAkcia(i))){
						SQL="insert into lines (id_doc,kol,cost,disc,id_tovar) select "+id+" as id_doc, (select "+model.getValueAt(i,2)+"/(tovar.kol*"+roz+"+"+(1-roz)+") from tovar where name='"+model.getValueAt(i, 1)+"')"+
							" as kol, (select "+model.getValueAt(i,3)+"*(tovar.kol*"+roz+"+"+(1-roz)+") from tovar where name='"+model.getValueAt(i, 1)+"')"+" as cost, "+model.getRowDiscount(i)+" as disc, id_tovar from tovar where name='"+
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
							" and note='&"+getNote()+"' and id_price=(select id_price from type_price where name ='"+(String)priceCombo.getSelectedItem()+"')" ;
				rs1=DataSet.QueryExec(SQL, false);
				if (rs1.next()){
					id=rs1.getInt(1);
					SQL="update document set sum="+(rs1.getDouble(2)+model.summAkcia())+" where id_doc="+id;
				}else{
					SQL="insert into document (id_type_doc, id_doc, id_client, id_skl, id_val, sum, note, disc,id_price ,isclientcard, id_manager) select 2 as id_type_doc,"+id+" as id_doc"+
						", (select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') as id_client" +
						", (select id_skl from SKLAD where name='"+(String)skladCombo.getSelectedItem()+"') as id_skl"+
						", (select distinct id_val from type_price where name='"+(String)priceCombo.getSelectedItem()+"') as id_val" +
						", "+model.summAkcia()+" as sum ,'&"+getNote()+"' as note, 0 as disc" +
						", (select id_price from type_price where name='"+(String)priceCombo.getSelectedItem()+"') as id_price " +
						", " +clientKoeficientForDiscount+ " as isclientcard,"+
						" id_manager from manager where name='"+parent.GetUserName()+"'";
				}
				DataSet.UpdateQuery(SQL);


//Новая запись строк
								
				for (int i=0;i<model.getRowCount();i++){
					if (model.getAkcia(i)){
						SQL="insert into lines (id_doc,kol,cost,disc,id_tovar) select "+id+" as id_doc, (select "+model.getValueAt(i,2)+"/(tovar.kol*"+roz+"+"+(1-roz)+") from tovar where name='"+model.getValueAt(i, 1)+"')"+
							" as kol, (select "+model.getValueAt(i,3)+"*(tovar.kol*"+roz+"+"+(1-roz)+") from tovar where name='"+model.getValueAt(i, 1)+"')"+" as cost, "+model.getRowDiscount(i)+" as disc, id_tovar from tovar where name='"+
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
			JOptionPane.showMessageDialog(null, "Запись не удалась. Возможно кто-то редактирует документ. \n Повторите попытку.","Ошибка блокировки.",JOptionPane.ERROR_MESSAGE);
			 try {
				DataSet.rollback();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "Критическая ошибка. Обратитесь к администратору!/n (ERROR-02)", "Ошибка", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				ret=false;
			}
			 ret=false;
			 e.printStackTrace();
		}
		return ret;
	}
	
/*	private boolean isPersonalDiscount(String aName,String aSklad,String aTovar){
		String SQL;
		SQL=String.format("select count(*) from DISCOUNT where ID_CLIENT=(select id_client from client where name='%s') and id_skl=(select ID_SKL from sklad where name='%s') and ID_GROUP in (SELECT id_group from GROUPID start with ID_GROUP=(select distinct id_group from kart where id_tovar=(select id_tovar from tovar where name='%s')) connect by prior GROUPID.PARENT_GROUP=GROUPID.ID_GROUP)", aName,aSklad,aTovar);
		boolean ret=false;
		try{
			ResultSet rs=DataSet.QueryExec(SQL, false);
			ret=rs.getInt(1)!=0;
		}
		catch (Exception Ex){
			Ex.printStackTrace();
		}
		return ret;
		
	}
	
	private int personalDiscount(String aName,String aSklad,String aTovar){
		String SQL;
		SQL=String.format("select disc from DISCOUNT where ID_CLIENT=(select id_client from client where name='%s') and id_skl=(select ID_SKL from sklad where name='%s') and ID_GROUP in (SELECT id_group from GROUPID start with ID_GROUP=(select distinct id_group from kart where id_tovar=(select id_tovar from tovar where name='%s')) connect by prior GROUPID.PARENT_GROUP=GROUPID.ID_GROUP)", aName,aSklad,aTovar);
		int ret=0;
		try{
			ResultSet rs=DataSet.QueryExec(SQL, false);
			ret=rs.getInt(1);
		}
		catch (Exception Ex){
			Ex.printStackTrace();
		}
		return ret;
		
	}
*/	
	
	private int presentAkcia(){
		int ret=0;
		for (int i=0;i<model.getRowCount();i++)
			if (model.getAkcia(i))
				ret++;
		return ret;
	}
	@Override
	public boolean closeform(){
		if (model.getRowCount()>0&& JOptionPane.showConfirmDialog(parent, "Внимание! Все введенные данные будут удалены! Продолжить?","Удаление",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION ){
			return false;
		}
		model.removeAll();
//		setVisible(false);
		parent.initBackground();
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
	public int getId_doc() {
		return id_doc;
	}
	public void setId_doc(int idDoc) {
		id_doc = idDoc;
	}
	private void initcombo(){
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
			rs = DataSet.QueryExec("select trim(name) from client where type in (1,2,3) order by upper(name)",true);
			rs.next();
			clientCombo.removeAllItems();
			while (!rs.isAfterLast()){
				clientCombo.addItem(rs.getString(1));
				rs.next();
			}
		}
		catch (Exception e) { e.printStackTrace();}
		isInputCard.setBackground(Color.RED);
		isInputCard.setText("Карта не проведена");
		clientKoeficientForDiscount=0;
		clientCombo.setSelectedIndex(0);
		ActionListener[] listeners=clientCombo.getActionListeners();
		for (ActionListener element:listeners){
			clientCombo.removeActionListener(element);
		}
		clientCombo.addActionListener(clientlistener);
		for (ActionListener element:listeners){
			clientCombo.addActionListener(element);
		}
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
/*		int disc=0;
		try{
			rs = DataSet.QueryExec("select disc from discount where id_client=(select id_client from client where name='"+(String)clientCombo.getSelectedItem()+"') and id_skl=(select id_skl from sklad where name='"+(String)skladCombo.getSelectedItem()+"') and id_group is null",true);
			if (rs.next()){
				disc=rs.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
*/
		try {
			rs=DataSet.QueryExec("select trim(name) from type_price where id_price=(select id_price from sklad where name = '"+(String)skladCombo.getSelectedItem()+"' )", false);
			rs.next();
			priceCombo.setSelectedItem(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		okrCombo.setSelectedIndex(0);
		editableCheck.setSelected(false);
	}
	private TableModelListener modlis=new TableModelListener(){
		public void tableChanged(TableModelEvent event){
			itogo.setText("Итого (учитывая скидку): "+model.summ());
			itogowo.setText("Итого (не учитывая скидку): "+model.summvo());
/*			String sumForSale_asString=parent.CalcSumForSale((String)clientCombo.getSelectedItem());
			if (sumForSale_asString==null || okrCombo.isVisible()){
				sumForSale.setVisible(false);
			}else{
				sumForSale.setText(String.format("Сумма по акции: %s", sumForSale_asString));
				sumForSale.setVisible(true);
			}
*/
			NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
			double curs=1;
			try{
				ResultSet rs=DataSet.QueryExec1("select curs from curs_now where id_val=(select id_val from type_price where name='"+priceCombo.getSelectedItem()+"')", false);
				if (rs.next())
					curs=rs.getDouble(1);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			itogoallLabel.setText("Итого по всем накладным: "+formatter.format(model.summ()*curs+getItogoall()));
			if (model.getRowCount()==0){
				skladCombo.setEnabled(true);
				priceCombo.setEnabled(true);
			}
			else{
				skladCombo.setEnabled(false);
				priceCombo.setEnabled(false);
			}
		}
	};
/*
	private Double getSumForSaleInOtherDoc() {
		return sumForSaleInOtherDoc;
	}
	private void setSumForSaleInOtherDoc(Double sumForSaleInOtherDoc) {
		this.sumForSaleInOtherDoc = sumForSaleInOtherDoc;
	}

	private Double getSumForSaleInCurrentDoc(){
		Double sumInDocument=0.0;
		Vector<String> listOfName=parent.ChooseNameForSale(model.getCommaName(), (String)skladCombo.getSelectedItem());
		for (int i=0; i<listOfName.size();i++){
			sumInDocument=sumInDocument+model.getSumByName(listOfName.get(i));
		}
		return sumInDocument;
	}
*/	
	
	private double getKoefForPrice() {
		return koefForPrice;
	}
	private void setKoefForPrice(double koefForPrice) {
		this.koefForPrice = 1+koefForPrice/100;
	}
	public static IndividualDiscount getDiscForClient(int id_client,int clientKoeficientForDiscount) throws Exception{
//		System.out.println("Зашел");
		IndividualDiscount indDisc=new IndividualDiscount();
		indDisc.setId_client(id_client);
		if (clientKoeficientForDiscount==0)
			return indDisc;
		String SQL=String.format("select disc, id_skl from discount where id_client='%s' and id_group is null", id_client);
		ResultSet rs=DataSet.QueryExec(SQL,true);
		while(rs.next()){
			indDisc.addGeneralDiscount(rs.getInt(1),rs.getInt(2));
			}
		SQL=String.format("select CONNECT_BY_ROOT disc disc, id_group, CONNECT_BY_ROOT id_skl from (select distinct g.id_group,g.PARENT_GROUP,d.disc,d.id_skl from GROUPID g left join (select id_group,disc,id_skl from discount where ID_CLIENT=%1$s ) d on d.ID_GROUP=g.ID_GROUP start with g.ID_GROUP in (select id_group from discount where ID_CLIENT=%1$s and ID_GROUP is not null) CONNECT by g.PARENT_GROUP=prior g.ID_GROUP) start with not (disc is null) connect by parent_group= prior id_group and disc is null", id_client);
		rs.close();
		rs=DataSet.QueryExec(SQL, false);
		while (rs.next()){
			indDisc.addDiscount(rs.getInt(2), rs.getInt(1), rs.getInt(3));
		}
		rs.close();
		return indDisc;
	}

	public void getClientCodFromScaner(String cod){
		try{
//			System.out.println(cod);
			String SQL;
			int rez=0;
			int numbClient=new Integer(cod.substring(8, 11));
			if (numbClient>10){
				SQL=String.format("update client set iscardinput=1 where card_numb='%s'", cod);
			}else{
				SQL=String.format("update client set iscardinput=1 where name='%s'", clientCombo.getSelectedItem());
			}
			rez=DataSet.UpdateQuery(SQL);
			if (rez==0) {
				JOptionPane.showMessageDialog(null, "Введенная карта не зарегистрированна в системе.", "Неизвестная карта", JOptionPane.ERROR_MESSAGE);
				return;
			}
			DataSet.commit();
			String name;
			if (numbClient>10){
				SQL=String.format("Select trim(name) from client where card_numb='%s'", cod);
				ResultSet rs=DataSet.QueryExec(SQL, false);
				rs.next();
				name=rs.getString(1);
				Checking=true;

				ActionListener actions[]=clientCombo.getActionListeners();
				for(ActionListener action:actions){
					clientCombo.removeActionListener(action);
				}
				clientCombo.setSelectedItem(name);
				for(ActionListener action:actions){
					clientCombo.addActionListener(action);
				}

				Checking=false;
				
			}else{
				name=(String)clientCombo.getSelectedItem();
			}
			clientCombo.fireActionEvent();
			JOptionPane.showMessageDialog(null, String.format("Введенная карта %s",name), "Карта клиента", JOptionPane.INFORMATION_MESSAGE);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
class JComboBoxFire extends JComboBox{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void fireActionEvent()
	{
		super.fireActionEvent();
	}
}


