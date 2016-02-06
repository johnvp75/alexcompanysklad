import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
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
	private static final boolean SALE=true;
	private static final GregorianCalendar STARTDATE=new GregorianCalendar(2011,7,25,0,0);
	private static final GregorianCalendar ENDDATE=new GregorianCalendar(2011,8,20,23,59);
	private static final double MIN_SUM_FOR_SALE_1=300.00;
	private static final int DISCOUNT_FOR_SALE_1=5;
	private static final double MIN_SUM_FOR_SALE_2=1000.00;
	private static final int DISCOUNT_FOR_SALE_2=7;
	private static final int GROUP_FOR_SALE[]={120000,130000,80000,480000,540000};
	private static final int SKLAD_FOR_SALE=4;

	
	private JMenu saleMenu;
	private JMenu editMenu;
	private JMenu doceditMenu;
	private JMenu printMenu;
	private JMenu exportMenu;
	private JMenu priceMenu;
	private NewSaleFrame newFrame;
	private SelectDoc selectFrame;
	private ManagerChooser dialog=null;
	private TovarChooser Printdialog=null;
	private String UserName ="";
	private JPanel parentFrame;
	private CardLayout Switch;
	private String visibleFrame="noVisible";
	private Image backgroundImage;
	
	public void paint( Graphics g ) { 
	    super.paint(g);
	    GregorianCalendar day=new GregorianCalendar();
	    GregorianCalendar holyday=(GregorianCalendar)day.clone();
	    holyday.set(2011, 2, 7);
	    if (day.equals(holyday))
	    	g.drawImage(backgroundImage, 10, 53, null);
	}
	public MainFrame() {

		initBackground();
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
		JMenuItem printWorkDocOne = new JMenuItem("Документы в обработке выборочно");
		JMenuItem printOldDoc = new JMenuItem("Проведенные документы");
		JMenuItem viewOldDoc = new JMenuItem("Просмотр проведенных документов");
		menuBar.add(printMenu);
		printMenu.add(printWorkDoc);
		printMenu.add(printWorkDocOne);
		printMenu.add(printOldDoc);
		printMenu.add(viewOldDoc);
		exportMenu = new JMenu ("Экспорт/Импорт");
		menuBar.add(exportMenu);
		JMenuItem rozexport=new JMenuItem("Экспорт розничной накладной");
		exportMenu.add(rozexport);
		JMenuItem impRemoteScanDoc = new JMenuItem("Импорт данных с автономного сканера");
		exportMenu.add(impRemoteScanDoc);
		priceMenu=new JMenu("Цены");
		menuBar.add(priceMenu);
		JMenuItem updateRozPrice=new JMenuItem("Обновить розничные цены");
		priceMenu.add(updateRozPrice);
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
		impRemoteScanDoc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (anyVisible()){
					
				}
				
			}
		});
		updateRozPrice.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					DataSet.UpdateQuery("delete from price where id_price=8");
					DataSet.UpdateQuery("insert into price (id_price,id_tovar,id_skl,cost,akciya,isakcia) select 8,id_tovar,id_skl,max(cost),0,0 from price where id_price=3 group by id_tovar,id_skl");
					DataSet.commit();
					JOptionPane.showMessageDialog(null, "Цены обновлены!", "Успех.", JOptionPane.INFORMATION_MESSAGE);
				}catch(Exception ex){
					try{
						DataSet.rollback();
					}catch(Exception ex1){
						ex1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Крах системы!", "Критическая ошибка!", JOptionPane.ERROR_MESSAGE);
					}
					JOptionPane.showMessageDialog(null, "Ошибка обновления!\n Попробуйте позже.", "Ошибка!", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				
			}
		});
		rozexport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				int numb=new Integer(JOptionPane.showInputDialog("Введите номер"));
				export(numb,true);
				repaint();
			}
		});

		printOldDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				int numb=new Integer(JOptionPane.showInputDialog("Введите номер"));
				newPrintold(numb,false);
				
			}
		});
		viewOldDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				int numb=new Integer(JOptionPane.showInputDialog("Введите номер"));
				newPrintold(numb,true);
				
			}
		});

		printWorkDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				newPrint();
			}
		});
		barcodeItem.addActionListener(new EditBarCode());
		windowcloseallItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				initBackground();
				repaint();
				if (getvisibleFrame().equals("SaleFrame") && newFrame.closeform())
					showFrame("noVisible");
				if (getvisibleFrame().equals("SelectFrame")&&newFrame.closeform())
					showFrame("noVisible");
			}
		});
	}
	public void initBackground(){
		try {
			backgroundImage=ImageIO.read(new File("image.jpg"));
		} catch (IOException e) {
			backgroundImage=null;
		}
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
				backgroundImage=null;
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
		initBackground();
		this.repaint();
		saleMenu.setEnabled(true);
		editMenu.setEnabled(true);
		doceditMenu.setEnabled(true);
		printMenu.setEnabled(true);
		SetUserName("");
		
	}
	public void print_old(){
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
			String item=rs.getString(1)+" на сумму: "+formatter.format(rs.getDouble(2))+" руб.";
			data.addElement(item);
			rs.next();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		Printdialog.addTovar(data);
		if ((Printdialog.showDialog(MainFrame.this, "Выбор клиента")) && (Printdialog.getTovar()!=null)){
			String clientName=Printdialog.getTovar().substring(0, Printdialog.getTovar().indexOf(" на сумму: "));
			int id_client;
			String Suma="";
			double sum=0;
			int id=0;
			boolean isOpt=true;
			double amountOfDiscount=0;
			if (JOptionPane.showConfirmDialog(null, String.format("Вы уверены что хотите напечатать\nдокументы %s? ", clientName), "Вы уверенны?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.NO_OPTION)
				return;
			PrintProcess question=new PrintProcess();
			if (question.ShowDialog(clientName)==PrintProcess.CANCEL_PRINT){
				return;
			}
			try{
				try{
					rs=DataSet.QueryExec("Select * from document where id_client in (select id_client from client where name='"+clientName+"')" +
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
				rs=DataSet.QueryExec("select type,id_client from client where name='"+clientName+"'", false);
				rs.next();
				id_client=rs.getInt(2);
				if (rs.getInt(1)==2)
					isOpt=false;
				
				if (isOpt && SALE)
					amountOfDiscount=CalcSale(id_client);
				if (amountOfDiscount<0)
					return;
				rs=DataSet.QueryExec(String.format("Select sum(document.sum*curs_now.curs) from document, curs_now where curs_now.id_val=document.id_val and (document.numb is NULL) and document.id_type_doc=2 and document.id_client=(select id_client from client where name='%s') ",clientName),false );
				rs.next();
				sum=rs.getDouble(1);
				Suma=formatter.format(sum)+" руб.";
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

			Suma=formatter.format(sum)+" руб.";
			String messageDiscount="Скидка по акции составила: "+formatter.format(amountOfDiscount)+" руб.";
			Vector<Vector<String>> OutData = new Vector<Vector<String>>(0);
			
			try {
				rs=DataSet.QueryExec("select nvl(max(numb),-1) from document where (to_number(to_char(day, 'YYYY'))=to_number(to_char(sysdate, 'YYYY'))) and (id_type_doc=2)", false) ;
				if ((!rs.next()) || (rs.getInt(1)==-1) ){
					DataSet.UpdateQuery1("drop sequence numb_real");
					DataSet.UpdateQuery1("CREATE SEQUENCE   numb_real  MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH 1 NOCACHE NOORDER");
					DataSet.commit1();
				}
				String SQL="Select id_doc, id_skl from document where (numb is NULL) and (id_client=(select id_client from client where name='"+clientName+"'))";
				rs=DataSet.QueryExec(SQL, false);
				int Doc_count=0;
				boolean first=true;
				int startRow=0;
				while (rs.next()){
					Doc_count++;
					id=rs.getInt(1);
					int skl=rs.getInt(2);
					boolean last=!rs.next();
					DataSet.UpdateQuery("update document set numb=numb_real.nextval, day=sysdate where id_doc="+id);
					if (isOpt)
						rs=DataSet.QueryExec("select trim(tovar.name), tovar.kol, sum(lines.kol), cost, disc, sum(lines.kol*cost*(1-disc/100)) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, tovar.kol, cost, disc order by tovar.name", false);
					else{
						String SQLr=(skl==2 | skl==3)?specialPrintForShop(id,skl):"select trim(tovar.name), sum(lines.kol*tovar.kol), cost/tovar.kol, sum(lines.kol*cost) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, cost/tovar.kol order by "+(skl!=8?"tovar.name":"substr(upper(trim(tovar.name)),instr(trim(tovar.name),' ')+1),to_number(substr(upper(trim(tovar.name)),1,instr(trim(tovar.name),' ')-1),'999999999.99')");
						rs=DataSet.QueryExec(SQLr, false);
					}
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
						OutputOO.OpenDoc("nakl_opt.ots",true,false);
						if (first){
							OutputOO.OpenDoc("nakl_sum.ots",true,true);
							OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 5,1,true);
							OutputOO.InsertOne("Получатель: "+clientName,11, true, 1,4,true);

						}
						OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 5,1,false);
						OutputOO.InsertOne("Получатель: "+clientName,11, true, 1,4,false);
						boolean repeat=false;
						do{
							OutputOO.InsertOne("Накладная №"+rs.getString(7)+pref, repeat?11:16, true, repeat?2:1, (repeat?startRow:0)+2,repeat);
							OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,(repeat?startRow+3+(first?3:0):6),repeat);
							OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,repeat?6:7,(repeat?startRow+4+(first?3:0):7),repeat);
							OutputOO.InsertOne("Валюта: "+rs.getString(4),7,false,1,(repeat?startRow+4+(first?3:0):7),repeat);
							OutputOO.InsertOne("ИТОГО:",10,false,5,(repeat?startRow+6+(first?3:0):9)+size,repeat);
							OutputOO.InsertOne(formatter.format(rs.getDouble(1)/(1-rs.getDouble(3)/100)),10,false,7,(repeat?startRow+6+(first?3:0):9)+size,repeat);
							OutputOO.InsertOne("Скидка",10,false,2,(repeat?startRow+6+(first?3:0):9)+size+1,repeat);
							OutputOO.InsertOne(formatter.format(rs.getDouble(3))+"%",10,false,5,(repeat?startRow+6+(first?3:0):9)+size+1,repeat);
							OutputOO.InsertOne(formatter.format(rs.getDouble(1)*(1/(1-rs.getDouble(3)/100)-1)),10,false,7,(repeat?startRow+6+(first?3:0):9)+size+1,repeat);
							OutputOO.InsertOne("Итого со скидкой",10,false,2,(repeat?startRow+6+(first?3:0):9)+size+2,repeat);
							OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,7,(repeat?startRow+6+(first?3:0):9)+size+2,repeat);
							OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,(repeat?startRow+6+(first?3:0):9)+size+4,repeat);
							if (last) {
								OutputOO.InsertOne("Итого по всем накладным ("+Doc_count+" шт.): "+Suma+" (курс USD="+curs_USD+")",10,true,2,(repeat?startRow+6+(first?3:0):9)+size+6,repeat);
								if (SALE && amountOfDiscount>0)
									OutputOO.InsertOne(messageDiscount,10,true,2,(repeat?startRow+6+(first?3:0):9)+size+8,repeat);
							}
							repeat=!repeat;
						}while(repeat);
						}
					else
						{
						OutputOO.OpenDoc("nakl_roz.ots",true,false);
						OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 3,1,false);
						OutputOO.InsertOne("Накладная №"+rs.getString(7)+pref, 16, true, 1, 2,false);
						OutputOO.InsertOne("Получатель: "+clientName,11, true, 1,4,false);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6,false);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,5,7,false);
						OutputOO.InsertOne("Итого:",10,false,2,9+size,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,5,9+size,false);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+2,false);

						}
					OutputOO.Insert(1, 9, OutData,false);
					OutputOO.Insert(1, startRow+6+(first?3:0), OutData,true);
					startRow=startRow+size+(first?14:11);					
					if (isOpt){
						OutputOO.print(1,false);
						if (last){
							OutputOO.print(1,true);
							OutputOO.CloseDoc(true);
						}
					}
					else{
						OutputOO.print(3,false);
					}
					OutputOO.CloseDoc(false);
					
					rs=DataSet.QueryExec(SQL, false);
					first=false;
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
		this.repaint();
	}
	
	private void insertHead(int startRow,boolean summary){
		
	}
	
	private String specialPrintForShop(int id, int skl) throws Exception{
		String name="";
		int prefixBarCode=0;
		if (skl==2){
			name=" Очки с/з";
			prefixBarCode=60000;
		}
		
		if (skl==3){
			name=" Шляпа";
			prefixBarCode=1392306;
		}
		String LocateSQL=String.format("select distinct cost from lines where not (cost in (select price from glassforshop where id_skl=%s)) and id_doc=%s",skl, id);
		ResultSet rs1=DataSet.QueryExec(LocateSQL, false);
		try{
			while (rs1.next()){
				LocateSQL=String.format("Insert into glassforshop (name,barcode,price,id_skl) values ('%s','%s',%s,%s)",rs1.getInt(1)+name,BarCode.GenerateBarCode(prefixBarCode,true),rs1.getString(1),skl );
				DataSet.UpdateQuery1(LocateSQL);
			}
			DataSet.commit1();
		}catch(Exception e){
			e.printStackTrace();
			DataSet.rollback1();
			JOptionPane.showMessageDialog(null, "Ошибка записи новых штрих кодов");
		}
		return String.format("select trim(gfs.name), sum(l.kol),l.cost,sum(l.kol)*l.cost from glassforshop gfs,lines l where gfs.price=l.cost and id_doc=%s and gfs.id_skl=%s group by l.cost,trim(gfs.name), trim(gfs.barcode) order by l.cost", id, skl);
	}
	
	public void printold(int numb, boolean view){
//		Vector<String> data =new Vector<String>(0);
		NumberFormat formatter = new DecimalFormat ( "0.00" );
		ResultSet rs=null;
			int id=0;
			boolean isOpt=true;
			String tovar="";
			
			try{
				rs=DataSet.QueryExec("select type,trim(name) from client where id_client=(select id_client from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')=to_char(sysdate,'YYYY'))", false);
				//rs=DataSet.QueryExec("select type,trim(name) from client where id_client=(select id_client from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')='2014')", false);
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
					rs=DataSet.QueryExec("select id_doc, id_skl from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')=to_char(sysdate,'YYYY')", false);
					//rs=DataSet.QueryExec("select id_doc, id_skl from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')='2014'", false);
					rs.next();
					id=rs.getInt(1);
					int skl=rs.getInt(2);
					if (isOpt)
						rs=DataSet.QueryExec("select trim(tovar.name), tovar.kol, sum(lines.kol), cost, disc, sum(lines.kol*cost*(1-disc/100)) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, tovar.kol, cost, disc order by tovar.name", false);
					else{
						String SQL=(skl==2|skl==3)?specialPrintForShop(id,skl):"select trim(tovar.name), sum(lines.kol*tovar.kol), cost/tovar.kol, sum(lines.kol*cost) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, cost/tovar.kol order by "+(skl!=8?"tovar.name":"substr(upper(trim(tovar.name)),instr(trim(tovar.name),' ')+1),to_number(substr(upper(trim(tovar.name)),1,instr(trim(tovar.name),' ')-1),'999999999')");
						rs=DataSet.QueryExec(SQL, false);
					}
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
					rs=DataSet.QueryExec("select sum, trim(note), disc, trim(val.name), trim(manager.name), trim(sklad.name), to_char(document.day,'DD.MM.YYYY') from ((document inner join val on document.id_val=val.id_val) inner join manager on document.id_manager=manager.id_manager) inner join " +
							"sklad on document.id_skl=sklad.id_skl where id_doc="+id, false);
					String pref="";
					rs.next();
					if (rs.getString(2).charAt(0)=='&')
						pref=" (АКЦИЯ)";
					GregorianCalendar now=new GregorianCalendar();

					int size=OutData.size();
					if (isOpt) 
						{
						OutputOO.OpenDoc("nakl_opt.ots",!view,false);
						OutputOO.InsertOne("\""+rs.getString(7).substring(0, 2)+"\" "+Month(new Integer(rs.getString(7).substring(3, 5))-1)+" "+rs.getString(7).substring(6, 10)+"г.", 10, true, 5,1,false);
						OutputOO.InsertOne("Накладная №"+numb+pref, 16, true, 1, 2,false);
						OutputOO.InsertOne("Получатель: "+tovar,11, true, 1,4,false);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6,false);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,7,7,false);
						OutputOO.InsertOne("Валюта: "+rs.getString(4),7,false,1,7,false);
						OutputOO.InsertOne("ИТОГО:",10,false,5,9+size,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)/(1-rs.getDouble(3)/100)),10,false,7,9+size,false);
						OutputOO.InsertOne("Скидка",10,false,2,9+size+1,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(3))+"%",10,false,5,9+size+1,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)*(1/(1-rs.getDouble(3)/100)-1)),10,false,7,9+size+1,false);
						OutputOO.InsertOne("Итого со скидкой",10,false,2,9+size+2,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,7,9+size+2,false);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+4,false);
						}
					else
						{
						OutputOO.OpenDoc("nakl_roz.ots",!view,false);
						OutputOO.InsertOne("\""+rs.getString(7).substring(0, 2)+"\" "+Month(new Integer(rs.getString(7).substring(3, 5))-1)+" "+rs.getString(7).substring(6, 10)+"г.", 10, true, 3,1,false);
						OutputOO.InsertOne("Накладная №"+numb+pref, 16, true, 1, 2,false);
						OutputOO.InsertOne("Получатель: "+tovar,11, true, 1,4,false);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6,false);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,5,7,false);
						OutputOO.InsertOne("Итого:",10,false,2,9+size,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,5,9+size,false);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+2,false);

						}
					OutputOO.Insert(1, 9, OutData,false);
					if (!view){
						OutputOO.print(1,false);
						OutputOO.CloseDoc(false);
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
			this.repaint();
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
			repaint();
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
	private void export( int numb, boolean roz){
		if (roz){
			try{
				ResultSet rs=DataSet.QueryExec(String.format("select type from client where id_client = (select id_client " +
						"from document where numb=%s and id_type_doc=2 and to_char(day,'YYYY')=to_char(sysdate,'YYYY'))", numb), false);
				rs.next();
				if (rs.getInt(1)!=2){
					JOptionPane.showMessageDialog(null, "Это не розничный документ!!!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
					return ;
				}
				NumberFormat formatter = new DecimalFormat ( "0.00" );
				Vector<Vector<String>> OutData = new Vector<Vector<String>>(0);
				rs=DataSet.QueryExec(String.format("select id_doc,id_skl from document where numb=%s and id_type_doc=2 and to_char(day,'YYYY')=to_char(sysdate,'YYYY')", numb), false);
				rs.next();
				int id=rs.getInt(1);
				int skl=rs.getInt(2);
				GenerateBarCodeForMissing(id);
				String SQL=(skl==2 | skl==3)
						?String.format("select trim(gfs.barcode), trim(gfs.name), sum(l.kol),l.cost from glassforshop gfs,lines l where gfs.price=l.cost and id_doc=%s and gfs.id_skl=%s group by l.cost,trim(gfs.name), trim(gfs.barcode) order by l.cost", id, skl)
						:String.format("select b.bar_code, trim(t.name), sum(l.kol*t.kol), l.cost/t.kol from lines l, tovar t, (select max(trim(bar_code)) as bar_code, id_tovar, id_skl from bar_code where for_shops=1 group by id_tovar, id_skl) b, document d where t.id_tovar = l.id_tovar and b.id_tovar=l.id_tovar and l.id_doc = d.id_doc and d.id_skl=b.id_skl and d.id_doc=%s group by b.bar_code, trim(t.name), l.cost/t.kol order by %s", id,
						skl!=8?"trim(t.name)":"substr(upper(trim(t.name)),instr(trim(t.name),' ')+1),to_number(substr(upper(trim(t.name)),1,instr(trim(t.name),' ')-1),'999999999.99')");
				rs=DataSet.QueryExec(SQL, false);
				for (int i=0; i<OutData.size();i++)
					OutData.get(i).clear();
				OutData.clear();
				if (skl!=8 && skl!=3){
					while (rs.next()){
						Vector<String> Row=new Vector<String>(0);
						Row.add(rs.getString(1));
						Row.add(rs.getString(2));
						Row.add(rs.getString(3));
						Row.add((formatter.format(rs.getDouble(4))).replace('.', ','));
						OutData.add(Row);
					}
				}else{
					OutData=exportForJuv(rs);
				}
				OutputOO.OpenDoc("export_roz.ots",false,false);
				OutputOO.Insert(1, 2, OutData,false);
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		this.repaint();
	}
	private Vector<Vector<String>> exportForJuv(ResultSet rs1) throws SQLException{
		Vector<Vector<String>> ret=new Vector<Vector<String>>(0);
		NumberFormat formatter = new DecimalFormat ( "0.00" );
		if (rs1.next()){
			int firstBlank=rs1.getString(2).indexOf(" ");
			int endBlank=rs1.getString(2).indexOf(" ", firstBlank+1);
			String groupName=rs1.getString(2).substring(firstBlank+1, endBlank>0?endBlank:rs1.getString(2).length());
			
			Vector<String> Row=new Vector<String>(0);
			Row.add(" ");
			Row.add(" ");
			Row.add("1");
			Row.add(" ");
			Row.add(" ");
			Row.add(" ");//номер группы
			Row.add(groupName);
			ret.add(Row);
			String newGroupName=groupName;
			String oldGroupName="";
			do{
				oldGroupName=newGroupName;
				firstBlank=rs1.getString(2).indexOf(" ");
				endBlank=rs1.getString(2).indexOf(" ", firstBlank+1);
				newGroupName=rs1.getString(2).substring(firstBlank+1, endBlank>0?endBlank:rs1.getString(2).length());
				if (!newGroupName.equals(oldGroupName)){
					Row=new Vector<String>(0);
					Row.add(" ");
					Row.add(" ");
					Row.add("1");
					Row.add(" ");
					Row.add(" ");
					Row.add(" ");//номер группы
					Row.add(oldGroupName);
					ret.add(Row);
					Row=new Vector<String>(0);
					Row.add(" ");
					Row.add(" ");
					Row.add("1");
					Row.add(" ");
					Row.add(" ");
					Row.add(" ");//номер группы
					Row.add(newGroupName);
					ret.add(Row);
				}
				Row=new Vector<String>(0);
				Row.add(rs1.getString(1));
				Row.add(rs1.getString(2));
				Row.add(rs1.getString(3));
				Row.add((formatter.format(rs1.getDouble(4))).replace('.', ','));
				Row.add(" ");
				Row.add(numbOfGroupFromName(newGroupName));//номер группы
				Row.add((formatter.format(rs1.getDouble(4))).replace('.', ',')+" руб.");
				ret.add(Row);
			}while (rs1.next());
			Row=new Vector<String>(0);
			Row.add(" ");
			Row.add(" ");
			Row.add("1");
			Row.add(" ");
			Row.add(" ");
			Row.add(" ");//номер группы
			Row.add(newGroupName);
			ret.add(Row);
		}
		return ret;
	}
	
	private String numbOfGroupFromName(String groupName) {
		String SQL=String.format("Select substr(name,0,2) from groupid where parent_group=1310000 and name like '%s'", '%'+groupName+'%');
		String ret=null;
		try{
		ResultSet rs=DataSet.QueryExec1(SQL, false);
		if (rs.next())
			ret=rs.getString(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	private void repaintform(){
		this.repaint();
	}
	private void GenerateBarCodeForMissing(int IdDocumentForWork){
		String SQL=String.format("select id_tovar, (select max(id_skl)  from document where id_doc=%1$s) as id_skl from(select l.id_tovar,b.bar_code from lines l  left join (select id_tovar, bar_code from bar_code where for_shops=1 and id_skl=(select id_skl from document where id_doc=%1$s)) b on l.id_tovar=b.id_tovar where l.id_doc=%1$s) where bar_code is null",IdDocumentForWork);
		try{
			int idSklad=0;
			ResultSet TableIdTovarWhithNullShopsBarcode=DataSet.QueryExec(SQL, false);
			if (TableIdTovarWhithNullShopsBarcode.next()){
				idSklad=TableIdTovarWhithNullShopsBarcode.getInt(2);
			}
			else
				return;
			do{
				int idTovarForGenerated=TableIdTovarWhithNullShopsBarcode.getInt(1);
				if (!IsFixExistentCodeForShopsBarcode(idTovarForGenerated, idSklad)){
					BarCode barCodeForSave=new BarCode(GenerateBarcode(idTovarForGenerated, idSklad),idTovarForGenerated, idSklad,1,true);
					SaveBarCodeInDatabase(barCodeForSave);
				}
			}while(TableIdTovarWhithNullShopsBarcode.next());
			DataSet.commit1();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Ошибка при работе с TableIdTovarWhithNullShopsBarcode");
			e.printStackTrace();
			try{
				DataSet.rollback1();
			}catch(Exception er){
				er.printStackTrace();
			}
		}
	}
	private String GenerateBarcode(int idTovar, int idSklad){
		String generatedBarcode="";
		try{
        	String SQL=String.format("Select distinct id_group from kart where id_tovar=%s and id_skl=%s", idTovar,idSklad);
        	ResultSet resultwithIdGroupForTovar=DataSet.QueryExec1(SQL, false);
        	int idGroupIncludingTovar=0;
        	if (resultwithIdGroupForTovar.next())
        		idGroupIncludingTovar=resultwithIdGroupForTovar.getInt(1);
        	else
        		return null;
        	resultwithIdGroupForTovar.close();
        	SQL=String.format("select max(substr(bar_code,%s,5)) from bar_code where bar_code like '%s%s'", (new Integer(idGroupIncludingTovar)).toString().length()+1,idGroupIncludingTovar,"%");
        	ResultSet resultWithMaximumBarcodeForThisGroup=DataSet.QueryExec1(SQL, false);
        	int maximumBarcodeForThisGroup=1;
        	if (resultWithMaximumBarcodeForThisGroup.next())
        		maximumBarcodeForThisGroup=resultWithMaximumBarcodeForThisGroup.getInt(1);
        	generatedBarcode=String.format("%s%05d", idGroupIncludingTovar,maximumBarcodeForThisGroup+1);
        	String copyGeneratedBarcodeForCalculateCheckSum=String.format("%07d%05d", idGroupIncludingTovar,maximumBarcodeForThisGroup+1);
        	Integer checksumForGeneratedBarcode=new Integer(0);
        	for (int i=2;i<13;i=i+2)
        		checksumForGeneratedBarcode=checksumForGeneratedBarcode+(Integer.valueOf(copyGeneratedBarcodeForCalculateCheckSum.substring(i-1, i)));
        	checksumForGeneratedBarcode=checksumForGeneratedBarcode*3;
        	for (int i=1;i<12;i=i+2)
        		checksumForGeneratedBarcode=checksumForGeneratedBarcode+(Integer.valueOf(copyGeneratedBarcodeForCalculateCheckSum.substring(i-1, i)));
        	checksumForGeneratedBarcode=10-((Double)((((checksumForGeneratedBarcode.doubleValue()/10)-checksumForGeneratedBarcode/10)*10)+0.1)).intValue();
        	generatedBarcode=generatedBarcode+checksumForGeneratedBarcode.toString().substring(checksumForGeneratedBarcode.toString().length()-1);
        }
        catch(Exception e){
        	JOptionPane.showMessageDialog(null, "Ошибка генерации штрих-кода", "Ошибка!", JOptionPane.ERROR_MESSAGE);
        	e.printStackTrace();
        	return null;
        }
        return generatedBarcode;
	}
	private Boolean IsFixExistentCodeForShopsBarcode(int idTovarForCheck, int idSklad){
		int madeReplacement=0;
		try{
			String SQL=String.format("update bar_code set for_shops=1 where id_tovar=%s and id_skl=%s and length(trim(bar_code)) between 12 and 14 and instr(regexp_replace(trim(bar_code),'[^[:digit:]]','&'),'&')=0", idTovarForCheck,idSklad);
			madeReplacement=DataSet.UpdateQuery1(SQL);
		}catch(Exception e){
			e.printStackTrace();
		}
		return madeReplacement>0;
	}
	private void SaveBarCodeInDatabase(BarCode savingBarcode){
		try{
			String SQL=String.format("insert into bar_code (id_tovar, id_skl, bar_code, count, for_shops) values (%s, %s, '%s', %s, %s)", savingBarcode.getIdTovar(),savingBarcode.getIdSklad(),savingBarcode.getCode(),savingBarcode.getCountTovarForOneCode(),savingBarcode.getBarcodeForShops()?1:null);
			DataSet.UpdateQuery1(SQL);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Ошибка зыписи штрих-кода", "Ошибка!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private double CalcSale(int aId_client){
		double sumOfDiscount=CalcSumForSale(aId_client);
		double curs=0.00;
		if (sumOfDiscount<MIN_SUM_FOR_SALE_1)
			return 0;
		int discount=0;
		if (sumOfDiscount>=MIN_SUM_FOR_SALE_1 && sumOfDiscount<MIN_SUM_FOR_SALE_2)
			discount=DISCOUNT_FOR_SALE_1;
		if (sumOfDiscount>=MIN_SUM_FOR_SALE_2)
			discount=DISCOUNT_FOR_SALE_2;
		try{
			sumOfDiscount=sumOfDiscount*discount/100;
			String SQL=String.format("update lines set disc=%s where rowid in (select l.rowid from lines l, document d where l.id_doc = d.id_doc and d.id_type_doc=2 and d.numb is null and d.id_client=%s and l.disc=0 and substr(d.note,1,1)!='&' and l.id_tovar in (select distinct id_tovar FROM kart WHERE id_group in (select id_group FROM groupid start with parent_group in (%s) CONNECT BY prior id_group=parent_group) and id_skl=%s))", discount, aId_client,IntArrayToCommaString(GROUP_FOR_SALE),SKLAD_FOR_SALE);
			DataSet.UpdateQuery(SQL);
			SQL=String.format("update document set sum = (select sum(l.kol*l.cost*(1-l.disc/100)*(1-d.disc/100)) from document d, lines l where document.id_doc = d.id_doc and l.id_doc = d.id_doc) where document.id_type_doc=2 and document.numb is null and document.id_client=%s", aId_client);
			DataSet.UpdateQuery(SQL);
			SQL="Select curs from curs_now where id_val=22";
			ResultSet rs=DataSet.QueryExec(SQL, false);
			rs.next();
			curs=rs.getDouble(1);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Ошибка расчета акции.\nОбатитесь к администратору.", "Ошибка!", JOptionPane.ERROR_MESSAGE);
			sumOfDiscount=-1;
			e.printStackTrace();
			try{
				DataSet.rollback();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return sumOfDiscount*curs;
	}
	
	private double CalcSumForSale(int aId_client){
		double sumForSale=0;
		if (!isSale()){
			return sumForSale;
		}
		try{
			String SQL=String.format("select sum(l.kol*l.cost) from lines l, document d, curs_now c  where l.id_doc = d.id_doc and d.id_type_doc=2 and d.numb is null and d.id_client=%s and l.disc=0 and substr(d.note,1,1)!='&' and l.id_tovar in (select distinct id_tovar FROM kart WHERE id_group in (select id_group FROM groupid start with parent_group in (%s) CONNECT BY prior id_group=parent_group) and id_skl=%s) and c.id_val=d.id_val", aId_client,IntArrayToCommaString(GROUP_FOR_SALE),SKLAD_FOR_SALE);
			ResultSet rs=DataSet.QueryExec(SQL, false);
			if (rs.next())
				sumForSale=rs.getDouble(1);
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ошибка в модуле:CalcSumForSale", "Ошибка!", JOptionPane.ERROR_MESSAGE);
		}
		return sumForSale;
	}
	
	public Double CalcSumForSale(String aName_client){
		double sumForSale=0;
		if (!isSale()){
			return null;
		}
		try{
			String SQL=String.format("select sum(l.kol*l.cost) from lines l, document d  where l.id_doc = d.id_doc and d.id_type_doc=2 and d.numb is null and d.id_client=%s and l.disc=0 and substr(d.note,1,1)!='&' and l.id_tovar in (select distinct id_tovar FROM kart WHERE id_group in (select id_group FROM groupid start with parent_group in (%s) CONNECT BY prior id_group=parent_group) and id_skl=%s)", getId_clientByName(aName_client),IntArrayToCommaString(GROUP_FOR_SALE),SKLAD_FOR_SALE);
			ResultSet rs=DataSet.QueryExec(SQL, false);
			if (rs.next())
				sumForSale=rs.getDouble(1);
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ошибка в модуле: CalcSumForSale", "Ошибка!", JOptionPane.ERROR_MESSAGE);
		}
	
		return sumForSale;
	}
	
	private String IntArrayToCommaString(int[] array){
		String resultString="";
		for (int element:GROUP_FOR_SALE){
			resultString=resultString+", "+element;
		}
		if (resultString.length()>0)
			resultString=resultString.substring(2);
		return resultString; 

	}
	
	private int getId_clientByName(String aNameClient){
		int id_client=-1;
		try{
			String SQL=String.format("select id_client from client where name='%s'", aNameClient);
			ResultSet rs=DataSet.QueryExec(SQL, false);
			if (rs.next())
				id_client=rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ошибка в модуле: getId_clientByName", "Ошибка!", JOptionPane.ERROR_MESSAGE);
		}
		return id_client;
	}
	
	public Vector<String> ChooseNameForSale(String commaName, String skladName){
		Vector<String> nameForSale=new Vector<String>(0); 
		try{
			String SQL=String.format("Select id_skl from sklad where name='%s'", skladName);
			ResultSet rs=DataSet.QueryExec(SQL, false);
			int id_skl=0;
			if (rs.next())
				id_skl=rs.getInt(1);
			if (id_skl!=SKLAD_FOR_SALE)
				return nameForSale;
			SQL=String.format("select trim(t2.name) from (select distinct k.id_group, t1.name from (select id_tovar, name from tovar where name in (%s)) t1, kart k where k.id_tovar=t1.id_tovar and k.id_skl=%s) t2," +
					" (select t.root,t.id_group from groupid g1,(select distinct CONNECT_BY_ROOT id_group root, id_group from groupid g connect by prior g.id_group= g.parent_group ) t " +
					"where t.root= g1.id_group and g1.parent_group is null) r where r.id_group=t2.id_group and r.root in (%s)", commaName, SKLAD_FOR_SALE, IntArrayToCommaString(GROUP_FOR_SALE));
			rs=DataSet.QueryExec(SQL, false);
			while (rs.next()){
				nameForSale.add(rs.getString(1));
			}
		}catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ошибка в модуле: ChooseNameForSale", "Ошибка!", JOptionPane.ERROR_MESSAGE);
		}
		return nameForSale;
	}
	
	public static Boolean isSale(){
		return ((new GregorianCalendar()).after(STARTDATE) && (new GregorianCalendar()).before(ENDDATE)) && SALE; 
	}
	
	public void newPrint(){
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
			String item=rs.getString(1)+" на сумму: "+formatter.format(rs.getDouble(2))+" руб.";
			data.addElement(item);
			rs.next();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		Printdialog.addTovar(data);
		if ((Printdialog.showDialog(MainFrame.this, "Выбор клиента")) && (Printdialog.getTovar()!=null)){
			String clientName=Printdialog.getTovar().substring(0, Printdialog.getTovar().indexOf(" на сумму: "));
			int id_client;
			String Suma="";
			double sum=0;
			int id=0;
			boolean isOpt=true;
			double amountOfDiscount=0;
			if (JOptionPane.showConfirmDialog(null, String.format("Вы уверены что хотите напечатать\nдокументы %s? ", clientName), "Вы уверенны?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.NO_OPTION)
				return;
			PrintProcess question=new PrintProcess();
			if (question.ShowDialog(clientName)==PrintProcess.CANCEL_PRINT){
				return;
			}
			try{
				try{
					rs=DataSet.QueryExec("Select * from document where id_client in (select id_client from client where name='"+clientName+"')" +
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
				rs=DataSet.QueryExec("select type,id_client from client where name='"+clientName+"'", false);
				rs.next();
				id_client=rs.getInt(2);
				if (rs.getInt(1)==2)
					isOpt=false;
				
				if (isOpt && SALE)
					amountOfDiscount=CalcSale(id_client);
				if (amountOfDiscount<0)
					return;
				rs=DataSet.QueryExec(String.format("Select sum(document.sum*curs_now.curs) from document, curs_now where curs_now.id_val=document.id_val and (document.numb is NULL) and document.id_type_doc=2 and document.id_client=(select id_client from client where name='%s') ",clientName),false );
				rs.next();
				sum=rs.getDouble(1);
				Suma=formatter.format(sum)+" руб.";
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

			Suma=formatter.format(sum)+" руб.";
			String messageDiscount="Скидка по акции составила: "+formatter.format(amountOfDiscount)+" руб.";
			Vector<Vector<String>> OutData = new Vector<Vector<String>>(0);
			
			try {
				rs=DataSet.QueryExec("select nvl(max(numb),-1) from document where (to_number(to_char(day, 'YYYY'))=to_number(to_char(sysdate, 'YYYY'))) and (id_type_doc=2)", false) ;
				if ((!rs.next()) || (rs.getInt(1)==-1) ){
					DataSet.UpdateQuery1("drop sequence numb_real");
					DataSet.UpdateQuery1("CREATE SEQUENCE   numb_real  MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH 1 NOCACHE NOORDER");
					DataSet.commit1();
				}
				String SQL="Select id_doc, id_skl from document where (numb is NULL) and (id_client=(select id_client from client where name='"+clientName+"'))";
				rs=DataSet.QueryExec(SQL, false);
				int Doc_count=0;
				boolean first=true;
				int startRow=0;
				while (rs.next()){
					Doc_count++;
					id=rs.getInt(1);
					int skl=rs.getInt(2);
					boolean last=!rs.next();
					DataSet.UpdateQuery("update document set numb=numb_real.nextval, day=sysdate where id_doc="+id);
					if (isOpt)
						rs=DataSet.QueryExec("select trim(tovar.name), tovar.kol, sum(lines.kol), cost, disc, sum(lines.kol*cost*(1-disc/100)) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, tovar.kol, cost, disc order by tovar.name", false);
					else{
						String SQLr=(skl==2 | skl==3)?specialPrintForShop(id,skl):"select trim(tovar.name), sum(lines.kol*tovar.kol), cost/tovar.kol, sum(lines.kol*cost) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, cost/tovar.kol order by "+(skl!=8?"tovar.name":"substr(upper(trim(tovar.name)),instr(trim(tovar.name),' ')+1),to_number(substr(upper(trim(tovar.name)),1,instr(trim(tovar.name),' ')-1),'999999999.99')");
						rs=DataSet.QueryExec(SQLr, false);
					}
					for (int i=0; i<OutData.size();i++)
						OutData.get(i).clear();
					OutData.clear();
					int j=0;
					double SumWithoutDiscount=0.00;
					while (rs.next()){
						Vector<String> Row=new Vector<String>(0);
						j++;
						Row.add(j+"");
						if (isOpt){
							SumWithoutDiscount=SumWithoutDiscount+rs.getDouble(3)*rs.getDouble(4);
							Row.add(rs.getString(1));
							Row.add(rs.getString(3));
							Row.add(formatter.format(rs.getDouble(4)));
							Row.add(rs.getString(5));
							Row.add(formatter.format(rs.getDouble(4)*(1-rs.getDouble(5)/100)));
							Row.add(formatter.format(rs.getDouble(6)));
						}else{
							Row.add(rs.getString(1));
							Row.add(rs.getString(2));
							Row.add(formatter.format(rs.getDouble(3)));
							Row.add(formatter.format(rs.getDouble(4)));
						}
						OutData.add(Row);
					}
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
						OutputOO.OpenDoc("nakl_opt_new.ots",true,false);
						if (first){
							OutputOO.OpenDoc("nakl_sum_new.ots",true,true);
							OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 5,1,true);
							OutputOO.InsertOne("Получатель: "+clientName,11, true, 1,4,true);

						}
						OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 5,1,false);
						OutputOO.InsertOne("Получатель: "+clientName,11, true, 1,4,false);
						boolean repeat=false;
						do{
							OutputOO.InsertOne("Накладная №"+rs.getString(7)+pref, repeat?11:16, true, repeat?2:1, (repeat?startRow:0)+2,repeat);
							OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,(repeat?startRow+3+(first?3:0):6),repeat);
							OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,repeat?6:7,(repeat?startRow+4+(first?3:0):7),repeat);
							OutputOO.InsertOne("Валюта: "+rs.getString(4),7,false,1,(repeat?startRow+4+(first?3:0):7),repeat);
							OutputOO.InsertOne("ИТОГО:",10,false,2,(repeat?startRow+6+(first?3:0):9)+size,repeat);
							OutputOO.InsertOne(formatter.format(SumWithoutDiscount),10,false,7,(repeat?startRow+6+(first?3:0):9)+size,repeat);
							OutputOO.InsertOne("Скидка",10,false,2,(repeat?startRow+6+(first?3:0):9)+size+1,repeat);
//							OutputOO.InsertOne(formatter.format(),10,false,5,(repeat?startRow+6+(first?3:0):9)+size+1,repeat);
							OutputOO.InsertOne(formatter.format(SumWithoutDiscount-rs.getDouble(1)),10,false,7,(repeat?startRow+6+(first?3:0):9)+size+1,repeat);
							OutputOO.InsertOne("Итого со скидкой",10,false,2,(repeat?startRow+6+(first?3:0):9)+size+2,repeat);
							OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,7,(repeat?startRow+6+(first?3:0):9)+size+2,repeat);
							OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,(repeat?startRow+6+(first?3:0):9)+size+4,repeat);
							if (last) {
								OutputOO.InsertOne("Итого по всем накладным ("+Doc_count+" шт.): "+Suma,10,true,2,(repeat?startRow+6+(first?3:0):9)+size+6,repeat);
								if (SALE && amountOfDiscount>0)
									OutputOO.InsertOne(messageDiscount,10,true,2,(repeat?startRow+6+(first?3:0):9)+size+8,repeat);
							}
							repeat=!repeat;
						}while(repeat);
						}
					else
						{
						OutputOO.OpenDoc("nakl_roz.ots",true,false);
						OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 3,1,false);
						OutputOO.InsertOne("Накладная №"+rs.getString(7)+pref, 16, true, 1, 2,false);
						OutputOO.InsertOne("Получатель: "+clientName,11, true, 1,4,false);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6,false);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,5,7,false);
						OutputOO.InsertOne("Итого:",10,false,2,9+size,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,5,9+size,false);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+2,false);

						}
					OutputOO.Insert(1, 9, OutData,false);
					OutputOO.Insert(1, startRow+6+(first?3:0), OutData,true);
					startRow=startRow+size+(first?14:11);					
					if (isOpt){
						OutputOO.print(1,false);
						if (last){
							OutputOO.print(1,true);
							OutputOO.CloseDoc(true);
						}
					}
					else{
						OutputOO.print(3,false);
					}
					OutputOO.CloseDoc(false);
					
					rs=DataSet.QueryExec(SQL, false);
					first=false;
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
		this.repaint();
	}
	
	public void newPrintold(int numb, boolean view){
//		Vector<String> data =new Vector<String>(0);
		NumberFormat formatter = new DecimalFormat ( "0.00" );
		ResultSet rs=null;
			int id=0;
			boolean isOpt=true;
			String tovar="";
			
			try{
				rs=DataSet.QueryExec("select type,trim(name) from client where id_client=(select id_client from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')=to_char(sysdate,'YYYY'))", false);
				//rs=DataSet.QueryExec("select type,trim(name) from client where id_client=(select id_client from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')='2014')", false);
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
					rs=DataSet.QueryExec("select id_doc, id_skl, disc from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')=to_char(sysdate,'YYYY')", false);
					//rs=DataSet.QueryExec("select id_doc, id_skl from document where numb="+numb+" and id_type_doc=2 and to_char(day,'YYYY')='2014'", false);
					rs.next();
					if (rs.getInt(3)>0) {
						printold(numb, view);
						return;
					}
					id=rs.getInt(1);
					int skl=rs.getInt(2);
					if (isOpt)
						rs=DataSet.QueryExec("select trim(tovar.name), tovar.kol, sum(lines.kol), cost, disc, sum(lines.kol*cost*(1-disc/100)) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, tovar.kol, cost, disc order by tovar.name", false);
					else{
						String SQL=(skl==2|skl==3)?specialPrintForShop(id,skl):"select trim(tovar.name), sum(lines.kol*tovar.kol), cost/tovar.kol, sum(lines.kol*cost) from lines inner join tovar on lines.id_tovar=tovar.id_tovar where id_doc="+id+" group by tovar.name, cost/tovar.kol order by "+(skl!=8?"tovar.name":"substr(upper(trim(tovar.name)),instr(trim(tovar.name),' ')+1),to_number(substr(upper(trim(tovar.name)),1,instr(trim(tovar.name),' ')-1),'999999999')");
						rs=DataSet.QueryExec(SQL, false);
					}
					for (int i=0; i<OutData.size();i++)
						OutData.get(i).clear();
					OutData.clear();
					int j=0;
					double SumWithoutDiscount=0.00;
					while (rs.next()){
						Vector<String> Row=new Vector<String>(0);
						j++;
						Row.add(j+"");
						if (isOpt){
							SumWithoutDiscount=SumWithoutDiscount+rs.getDouble(3)*rs.getDouble(4);
							Row.add(rs.getString(1));
							Row.add(rs.getString(3));
							Row.add(formatter.format(rs.getDouble(4)));
							Row.add(rs.getString(5));
							Row.add(formatter.format(rs.getDouble(4)*(1-rs.getDouble(5)/100)));
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
					rs=DataSet.QueryExec("select sum, trim(note), disc, trim(val.name), trim(manager.name), trim(sklad.name), to_char(document.day,'DD.MM.YYYY') from ((document inner join val on document.id_val=val.id_val) inner join manager on document.id_manager=manager.id_manager) inner join " +
							"sklad on document.id_skl=sklad.id_skl where id_doc="+id, false);
					String pref="";
					rs.next();
					if (rs.getString(2).charAt(0)=='&')
						pref=" (АКЦИЯ)";
					GregorianCalendar now=new GregorianCalendar();
					
					
					
					int size=OutData.size();
					if (isOpt) 
						{
						OutputOO.OpenDoc("nakl_opt_new.ots",!view,false);
						OutputOO.InsertOne("\""+rs.getString(7).substring(0, 2)+"\" "+Month(new Integer(rs.getString(7).substring(3, 5))-1)+" "+rs.getString(7).substring(6, 10)+"г.", 10, true, 5,1,false);
						OutputOO.InsertOne("Накладная №"+numb+pref, 16, true, 1, 2,false);
						OutputOO.InsertOne("Получатель: "+tovar,11, true, 1,4,false);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6,false);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,7,7,false);
						OutputOO.InsertOne("Валюта: "+rs.getString(4),7,false,1,7,false);
						OutputOO.InsertOne("ИТОГО:",10,false,2,9+size,false);
						OutputOO.InsertOne(formatter.format(SumWithoutDiscount),10,false,7,9+size,false);
						OutputOO.InsertOne("Скидка",10,false,2,9+size+1,false);
						OutputOO.InsertOne(formatter.format(SumWithoutDiscount-rs.getDouble(1)),10,false,7,9+size+1,false);
						OutputOO.InsertOne("Итого со скидкой",10,false,2,9+size+2,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,7,9+size+2,false);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+4,false);
						}
					else
						{
						OutputOO.OpenDoc("nakl_roz.ots",!view,false);
						OutputOO.InsertOne("\""+rs.getString(7).substring(0, 2)+"\" "+Month(new Integer(rs.getString(7).substring(3, 5))-1)+" "+rs.getString(7).substring(6, 10)+"г.", 10, true, 3,1,false);
						OutputOO.InsertOne("Накладная №"+numb+pref, 16, true, 1, 2,false);
						OutputOO.InsertOne("Получатель: "+tovar,11, true, 1,4,false);
						OutputOO.InsertOne(rs.getString(2).substring(1),8,false,1,6,false);
						OutputOO.InsertOne("Склад: "+rs.getString(6),7,false,5,7,false);
						OutputOO.InsertOne("Итого:",10,false,2,9+size,false);
						OutputOO.InsertOne(formatter.format(rs.getDouble(1)),10,true,5,9+size,false);
						OutputOO.InsertOne("Документ оформил: "+rs.getString(5),8,false,2,9+size+2,false);

						}
					OutputOO.Insert(1, 9, OutData,false);
					if (!view){
						OutputOO.print(1,false);
						OutputOO.CloseDoc(false);
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
			this.repaint();
		}

}

