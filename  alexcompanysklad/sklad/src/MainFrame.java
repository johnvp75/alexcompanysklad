import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


class MainFrame extends JFrame
{
	private JMenu saleMenu;
	private JMenu editMenu;
	private JMenu doceditMenu;
	private JMenu printMenu;
	private NewSaleFrame newFrame;
	private SelectDoc selectFrame;
	private ManagerChooser dialog=null;
	private TovarChooser Printdialog=null;
	private String UserName ="";
	private JPanel parentFrame;
	private CardLayout Switch;
	private String visibleFrame="noVisible";
	public MainFrame(){
		setTitle("Склад 4.0");
		setSize(800, 600);
		JMenuBar menuBar= new JMenuBar();
		setJMenuBar(menuBar);
		saleMenu=new JMenu("Продажа");
		menuBar.add(saleMenu);
		JMenuItem newSaleItem=new JMenuItem("Новый документ");
		saleMenu.add(newSaleItem);
		editMenu=new JMenu("Редактирование");
		menuBar.add(editMenu);
		JMenuItem barcodeItem=new JMenuItem("Штрих-код");
		editMenu.add(barcodeItem);
		JMenu docMenu =new JMenu("Накладные");
		editMenu.add(docMenu);
		JMenuItem regdocItem = new JMenuItem("Проведенные");
		JMenuItem nonregdocItem = new JMenuItem("Непроведенные");
		docMenu.add(regdocItem);
		docMenu.add(nonregdocItem);
		JMenu clientMenu=new JMenu("Клиенты");
		JMenuItem lgotiItem =new JMenuItem("Скидка");
		JMenuItem dataItem =new JMenuItem("Данные");
		editMenu.add(clientMenu);
		clientMenu.add(lgotiItem);
		clientMenu.add(dataItem);
		doceditMenu = new JMenu("Накладные в обработке");
		menuBar.add(doceditMenu);
		printMenu = new JMenu("Печать");
		JMenuItem printWorkDoc = new JMenuItem("Документы в обработке");
		JMenuItem printOldDoc = new JMenuItem("Проведенные документы");
		JMenuItem viewOldDoc = new JMenuItem("Просмотр проведенных документов");
		menuBar.add(printMenu);
		printMenu.add(printWorkDoc);
		printMenu.add(printOldDoc);
		printMenu.add(viewOldDoc);
		JMenu windowMenu = new JMenu("Окно");
		menuBar.add(windowMenu);
		JMenuItem windowcloseItem = new JMenuItem("Закрыть текущее окно");
		JMenuItem windowcloseallItem = new JMenuItem("Закрыть все окна");
		windowMenu.add(windowcloseItem);
		windowMenu.add(windowcloseallItem);
		parentFrame = new JPanel(new CardLayout());
		add(parentFrame);
		Switch=(CardLayout)parentFrame.getLayout();
		newFrame = new NewSaleFrame();
		newFrame.parent=this;
//		newFrame.setVisible(false);
		parentFrame.add(newFrame,"SaleFrame");
		selectFrame = new SelectDoc(2,false,newFrame);
//		selectFrame.setVisible(false);
		parentFrame.add(selectFrame,"SelectFrame");
		parentFrame.setVisible(false);
//		setGlassPane(selectFrame);
//		NewSaleAction NewSale=new NewSaleAction();
//		saleMenu.addMenuListener(new NewSaleAction());
		newSaleItem.addActionListener(new NewSaleAction());
		nonregdocItem.addActionListener(new editNonRegAction());
		printOldDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				int numb=new Integer(JOptionPane.showInputDialog("Введите номер"));
				printold(numb,false);
				
			}
		});
		viewOldDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				int numb=new Integer(JOptionPane.showInputDialog("Введите номер"));
				printold(numb,true);
				
			}
		});

		printWorkDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				print();
			}
		});
		barcodeItem.addActionListener(new EditBarCode());
		windowcloseallItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (getvisibleFrame().equals("SaleFrame") && newFrame.closeform())
					showFrame("noVisible");
				if (getvisibleFrame().equals("SelectFrame")&&newFrame.closeform())
					showFrame("noVisible");
			}
		});
	}
	private class NewSaleAction implements ActionListener{
		public void actionPerformed(ActionEvent event)
		{
			if (!anyVisible())
				return;
			if (dialog==null)
				dialog=new ManagerChooser();
				dialog.setRul(";2;");
			if (dialog.showDialog(MainFrame.this, "Вход в систему")){
//				newFrame.setVisible(true);
//				newFrame.requestFocus();
//				newFrame.setFocusable(true);
//				newFrame.skladCombo.requestFocus();
				saleMenu.setSelected(false);
				saleMenu.setEnabled(false);
				editMenu.setEnabled(false);
				printMenu.setEnabled(false);
				doceditMenu.setEnabled(false);
				SetUserName(dialog.GetManager());
				newFrame.showform();
			}else{
					saleMenu.setSelected(false);
//					saleMenu.set
				}
		}
	}
	private class editNonRegAction implements ActionListener{
		public void actionPerformed(ActionEvent event)
		{
			if (!anyVisible())
				return;
			if (dialog==null)
				dialog=new ManagerChooser();
				dialog.setRul(";2;");
			if (dialog.showDialog(MainFrame.this, "Вход в систему")){
//				newFrame.setVisible(true);
//				newFrame.requestFocus();
//				newFrame.setFocusable(true);
//				newFrame.skladCombo.requestFocus();
				saleMenu.setSelected(false);
				saleMenu.setEnabled(false);
				editMenu.setEnabled(false);
				printMenu.setEnabled(false);
				doceditMenu.setEnabled(false);
				SetUserName(dialog.GetManager());
				selectFrame.setType_doc(2);
				selectFrame.setRegister(false);
				selectFrame.initform();
				showFrame("SelectFrame");
//				selectFrame.setVisible(true);
			}else{
					saleMenu.setSelected(false);
//					saleMenu.set
				}

		}
	}
	private void SetUserName(String aUserName){
		UserName=aUserName;
		setTitle("Склад 4.0 менеджер "+aUserName);
	}
	public String GetUserName(){
		return UserName;
	}
	public void closeSaleFrame(){
		saleMenu.setEnabled(true);
		editMenu.setEnabled(true);
		doceditMenu.setEnabled(true);
		printMenu.setEnabled(true);
		SetUserName("");
		
	}
	public void print(){
		if (Printdialog==null)
			Printdialog=new TovarChooser();

		Vector<String> data =new Vector<String>(0);
		NumberFormat formatter = new DecimalFormat ( "0.00" );
		ResultSet rs=null;
		String curs_USD="";
		try{
			rs=DataSet.QueryExec("Select curs from curs_now where id_val=22", false);
			
			if (rs.next())
				curs_USD= formatter.format(rs.getDouble(1));
			rs=DataSet.QueryExec("Select trim(client.name), sum(document.sum*curs_now.curs) from (document inner join client on client.id_client=document.id_client) inner join curs_now on curs_now.id_val=document.id_val where (numb is NULL) and document.id_type_doc=2 group by trim(name),document.id_client",true );
			rs.next();
			while (!rs.isAfterLast()){
			String item=rs.getString(1)+" на сумму: "+formatter.format(rs.getDouble(2))+" грн.";
			data.addElement(item);
			rs.next();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		Printdialog.addTovar(data);
		if ((Printdialog.showDialog(MainFrame.this, "Выбор клиента")) && (Printdialog.getTovar()!=null)){
			String tovar=Printdialog.getTovar().substring(0, Printdialog.getTovar().indexOf(" на сумму: "));
			String Suma=Printdialog.getTovar().substring(Printdialog.getTovar().indexOf(" на сумму: ")+11);
//			int numb=0;
			int id=0;
			boolean isOpt=true;
			
			try{
//				DataSet.UpdateQuery("lock table document in exclusive mode");
				try{
					rs=DataSet.QueryExec("Select * from document where id_client in (select id_client from client where name='"+tovar+"')" +
						" and numb is null for update nowait", false);
				}catch(Exception e){
					JOptionPane.showMessageDialog(this, "Одна из накладных редактируеться, печать не возможна! \n Попробуйте позже!", "Ошибка блокировки!", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (!rs.next()){
					JOptionPane.showMessageDialog(this, "Накладные уже напечатаны другим пользователем!", "Ошибка блокировки!", JOptionPane.INFORMATION_MESSAGE);
					DataSet.rollback();
					return;
				}

				rs=DataSet.QueryExec("select type from client where name='"+tovar+"'", false);
				rs.next();
				if (rs.getInt(1)==2)
					isOpt=false;
			}catch (Exception e) {
				try {
					DataSet.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				return;
			}
			Vector<Vector<String>> OutData = new Vector<Vector<String>>(0);
			
			try {
//				rs=DataSet.QueryExec("select max(numb) from document where (to_number(to_char(day, 'YYYY'))=to_number(to_char(sysdate, 'YYYY'))) and (id_type_doc=2)", false) ;
				rs=DataSet.QueryExec("select max(numb) from document where (to_number(to_char(day, 'YYYY'))=to_number(to_char(sysdate, 'YYYY'))) and (id_type_doc=2)", false) ;
				if (!rs.next()){
//					numb=rs.getInt(1);
					DataSet.UpdateQuery1("drop sequence numb_real");
					DataSet.UpdateQuery1("CREATE SEQUENCE   numb_real  MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH 1 NOCACHE NOORDER");
					DataSet.commit1();
				}
				String SQL="Select id_doc from document where (numb is NULL) and (id_client=(select id_client from client where name='"+tovar+"'))";
				rs=DataSet.QueryExec(SQL, false);
				int Doc_count=0;
				while (rs.next()){
					Doc_count++;
//					numb++;
					id=rs.getInt(1);
					boolean last=!rs.next();
					DataSet.UpdateQuery("update document set numb=numb_real.nextval, day=sysdate where id_doc="+id);
					if (isOpt)
						rs=DataSet.QueryExec("select trim(tovar.name), tovar.kol, sum(lines.kol), cost, disc, sum(lines.kol*cost*(1-disc/100)) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, tovar.kol, cost, disc order by tovar.name", false);
					else
						rs=DataSet.QueryExec("select trim(tovar.name), sum(lines.kol*tovar.kol), cost/tovar.kol, sum(lines.kol*cost) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, cost/tovar.kol order by tovar.name", false);
					for (int i=0; i<OutData.size();i++)
						OutData.get(i).clear();
					OutData.clear();
					int j=0;
					while (rs.next()){
						Vector<String> Row=new Vector<String>(0);
						j++;
						Row.add(j+"");
						if (isOpt){
							Row.add(rs.getString(1));
							Row.add(rs.getString(2));
							Row.add(rs.getString(3));
							Row.add(formatter.format(rs.getDouble(4)));
							Row.add(rs.getString(5));
							Row.add(formatter.format(rs.getDouble(6)));
						}else{
							Row.add(rs.getString(1));
							Row.add(rs.getString(2));
							Row.add(formatter.format(rs.getDouble(3)));
							Row.add(formatter.format(rs.getDouble(4)));
						}
						OutData.add(Row);
					}
//					String SQL1=;
					rs=DataSet.QueryExec("select sum, trim(note), disc, trim(val.name), trim(manager.name), trim(sklad.name),numb from ((document inner join val on document.id_val=val.id_val) inner join manager on document.id_manager=manager.id_manager) inner join " +
							"sklad on document.id_skl=sklad.id_skl where id_doc="+id, false);
					String pref="";
					rs.next();
					if (rs.getString(2).charAt(0)=='&')
						pref=" (АКЦИЯ)";
					GregorianCalendar now=new GregorianCalendar();

					int size=OutData.size();
					if (isOpt) 
						{
						OutputOO.OpenDoc("nakl_opt.ots",true);
						OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 5,1);
						OutputOO.InsertOne("Накладная №"+rs.getString(7)+pref, 16, true, 1, 2);
						OutputOO.InsertOne("Получатель: "+tovar,11, true, 1,4);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,7,7);
						OutputOO.InsertOne("Валюта: "+rs.getString(4),7,false,1,7);
						OutputOO.InsertOne("ИТОГО:",10,false,5,9+size);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)/(1-rs.getDouble(3)/100)),10,false,7,9+size);
						OutputOO.InsertOne("Скидка",10,false,2,9+size+1);
						OutputOO.InsertOne(formatter.format(rs.getDouble(3))+"%",10,false,5,9+size+1);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)*(1/(1-rs.getDouble(3)/100)-1)),10,false,7,9+size+1);
						OutputOO.InsertOne("Итого со скидкой",10,false,2,9+size+2);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,7,9+size+2);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+4);
						if (last) 
							OutputOO.InsertOne("Итого по всем накладным ("+Doc_count+" шт.): "+Suma+" (курс USD="+curs_USD+")",10,true,2,9+size+6);
						}
					else
						{
						OutputOO.OpenDoc("nakl_roz.ots",true);
						OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 3,1);
						OutputOO.InsertOne("Накладная №"+rs.getString(7)+pref, 16, true, 1, 2);
						OutputOO.InsertOne("Получатель: "+tovar,11, true, 1,4);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,5,7);
						OutputOO.InsertOne("Итого:",10,false,2,9+size);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,5,9+size);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+2);

						}
					OutputOO.Insert(1, 9, OutData);
					OutputOO.print(2);
					OutputOO.CloseDoc();
					
					rs=DataSet.QueryExec(SQL, false);
				}
				DataSet.commit();
			} catch (Exception e) {
				try {
					DataSet.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
	}
	public void printold(int numb, boolean view){
		Vector<String> data =new Vector<String>(0);
		NumberFormat formatter = new DecimalFormat ( "0.00" );
		ResultSet rs=null;
			int id=0;
			boolean isOpt=true;
			String tovar="";
			
			try{
				rs=DataSet.QueryExec("select type,trim(name) from client where id_client=(select id_client from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')=to_char(sysdate,'YYYY'))", false);
				rs.next();
				tovar=rs.getString(2);
				if (rs.getInt(1)==2)
					isOpt=false;
			}catch (Exception e) {
				e.printStackTrace();
			}
			Vector<Vector<String>> OutData = new Vector<Vector<String>>(0);
			
			try {
				{
					rs=DataSet.QueryExec("select id_doc from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')=to_char(sysdate,'YYYY')", false);
					rs.next();
					id=rs.getInt(1);
					if (isOpt)
						rs=DataSet.QueryExec("select trim(tovar.name), tovar.kol, sum(lines.kol), cost, disc, sum(lines.kol*cost*(1-disc/100)) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, tovar.kol, cost, disc order by tovar.name", false);
					else
						rs=DataSet.QueryExec("select trim(tovar.name), sum(lines.kol*tovar.kol), cost/tovar.kol, sum(lines.kol*cost) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, cost/tovar.kol order by tovar.name", false);
					for (int i=0; i<OutData.size();i++)
						OutData.get(i).clear();
					OutData.clear();
					int j=0;
					while (rs.next()){
						Vector<String> Row=new Vector<String>(0);
						j++;
						Row.add(j+"");
						if (isOpt){
							Row.add(rs.getString(1));
							Row.add(rs.getString(2));
							Row.add(rs.getString(3));
							Row.add(formatter.format(rs.getDouble(4)));
							Row.add(rs.getString(5));
							Row.add(formatter.format(rs.getDouble(6)));
						}else{
							Row.add(rs.getString(1));
							Row.add(rs.getString(2));
							Row.add(formatter.format(rs.getDouble(3)));
							Row.add(formatter.format(rs.getDouble(4)));
						}
						OutData.add(Row);
					}
//					String SQL1=;
					rs=DataSet.QueryExec("select sum, trim(note), disc, trim(val.name), trim(manager.name), trim(sklad.name), to_char(day,'DD.MM.YYYY') from ((document inner join val on document.id_val=val.id_val) inner join manager on document.id_manager=manager.id_manager) inner join " +
							"sklad on document.id_skl=sklad.id_skl where id_doc="+id, false);
					String pref="";
					rs.next();
					if (rs.getString(2).charAt(0)=='&')
						pref=" (АКЦИЯ)";
					GregorianCalendar now=new GregorianCalendar();

					int size=OutData.size();
					if (isOpt) 
						{
						OutputOO.OpenDoc("nakl_opt.ots",!view);
						OutputOO.InsertOne("\""+rs.getString(7).substring(0, 1)+"\" "+Month(new Integer(rs.getString(7).substring(3, 4))-1)+" "+rs.getString(7).substring(6, 9)+"г.", 10, true, 5,1);
						OutputOO.InsertOne("Накладная №"+numb+pref, 16, true, 1, 2);
						OutputOO.InsertOne("Получатель: "+tovar,11, true, 1,4);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,7,7);
						OutputOO.InsertOne("Валюта: "+rs.getString(4),7,false,1,7);
						OutputOO.InsertOne("ИТОГО:",10,false,5,9+size);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)/(1-rs.getDouble(3)/100)),10,false,7,9+size);
						OutputOO.InsertOne("Скидка",10,false,2,9+size+1);
						OutputOO.InsertOne(formatter.format(rs.getDouble(3))+"%",10,false,5,9+size+1);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)*(1/(1-rs.getDouble(3)/100)-1)),10,false,7,9+size+1);
						OutputOO.InsertOne("Итого со скидкой",10,false,2,9+size+2);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,7,9+size+2);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+4);
						}
					else
						{
						OutputOO.OpenDoc("nakl_roz.ots",!view);
						OutputOO.InsertOne("\""+rs.getString(7).substring(0, 1)+"\" "+Month(new Integer(rs.getString(7).substring(3, 4))-1)+" "+rs.getString(7).substring(6, 9)+"г.", 10, true, 3,1);
						OutputOO.InsertOne("Накладная №"+numb+pref, 16, true, 1, 2);
						OutputOO.InsertOne("Получатель: "+tovar,11, true, 1,4);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,5,7);
						OutputOO.InsertOne("Итого:",10,false,2,9+size);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,5,9+size);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+2);

						}
					OutputOO.Insert(1, 9, OutData);
					if (!view){
						OutputOO.print(2);
						OutputOO.CloseDoc();
					}
					
				}
				DataSet.commit();
			} catch (Exception e) {
				try {
					DataSet.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
	
	private String Month(int aValue){
		switch (aValue+1){
		case 1:return "января";
		case 2:return "февраля";
		case 3:return "марта";
		case 4:return "апреля";
		case 5:return "мая";
		case 6:return "июня";
		case 7:return "июля";
		case 8:return "августа";
		case 9:return "сентября";
		case 10:return "октября";
		case 11:return "ноября";
		case 12:return "декабря";
		default: return "";
		}
	}
	private boolean anyVisible(){
		boolean ret=getvisibleFrame().equals("noVisible");
//		if (newFrame.isVisible())
//			ret=false;
//		if (selectFrame.isVisible())
//			ret=false;
		
		
		if (!ret)
			JOptionPane.showMessageDialog(this, "Сначала закройте все окна!", "Ошибка!", JOptionPane.INFORMATION_MESSAGE);
		return ret;
	}
	private class EditBarCode implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if (!anyVisible())
				return;
			JComboBox skladCombo=new JComboBox();
			try{
				ResultSet rs=DataSet.QueryExec("select trim(name) from sklad order by name", false);
				while (rs.next()){
					skladCombo.addItem(rs.getString(1));
				}
				if (skladCombo.getItemCount()>0)
					skladCombo.setSelectedIndex(0);
				else 
					return;
				JOptionPane.showInputDialog(skladCombo);
				String sklad=(String)skladCombo.getSelectedItem();
				ListChoose formGroup= new ListChoose();
				NewBarCode window;
				while (formGroup.showDialog(null, "Выбор товара",sklad)){
					window =new NewBarCode(sklad,formGroup.getTovar());
					window.setVisible(true);
				}
					

			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	public void showFrame(String nameLayout){
		if (nameLayout.equals("noVisible"))
			
			parentFrame.setVisible(false);
		else
		{
			parentFrame.setVisible(true);
			Switch.show(parentFrame, nameLayout);
		}
		setvisibleFrame(nameLayout);
	}
	private void setvisibleFrame(String aValue){
		visibleFrame=aValue;
	}
	
	private String getvisibleFrame(){
		return visibleFrame;
	}
}

