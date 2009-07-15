import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


class MainFrame extends JFrame
{
	private JMenu saleMenu;
	private JMenu editMenu;
	private JMenu doceditMenu;
	private JMenu printMenu;
	private NewSaleFrame newFrame;
	private ManagerChooser dialog=null;
	private TovarChooser Printdialog=null;
	private String UserName ="";
	public MainFrame(){
		setTitle("Склад 4.0");
		setSize(800, 600);
		JMenuBar menuBar= new JMenuBar();
		setJMenuBar(menuBar);
		saleMenu=new JMenu("Продажа");
		menuBar.add(saleMenu);
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
		menuBar.add(printMenu);
		printMenu.add(printWorkDoc);
		printMenu.add(printOldDoc);
		JMenu windowMenu = new JMenu("Окно");
		menuBar.add(windowMenu);
		JMenuItem windowcloseItem = new JMenuItem("Закрыть текущее окно");
		JMenuItem windowcloseallItem = new JMenuItem("Закрыть все окна");
		windowMenu.add(windowcloseItem);
		windowMenu.add(windowcloseallItem);
		newFrame = new NewSaleFrame();
		newFrame.parent=this;
		newFrame.setVisible(false);
		add(newFrame);
//		NewSaleAction NewSale=new NewSaleAction();
		saleMenu.addMenuListener(new NewSaleAction());
		printWorkDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				print();
			}
		});
	}
	private class NewSaleAction implements MenuListener{
		public void menuSelected(MenuEvent event)
		{
			
			if (dialog==null)
				dialog=new ManagerChooser();
				dialog.setRul(";2;");
			if (dialog.showDialog(MainFrame.this, "Вход в систему")){
//				newFrame.setVisible(true);
//				newFrame.requestFocus();
				newFrame.showform();
//				newFrame.setFocusable(true);
//				newFrame.skladCombo.requestFocus();
				saleMenu.setSelected(false);
				saleMenu.setEnabled(false);
				editMenu.setEnabled(false);
				printMenu.setEnabled(false);
				doceditMenu.setEnabled(false);
				newFrame.requestFocus();
				SetUserName(dialog.GetManager());

			}else{
					saleMenu.setSelected(false);
//					saleMenu.set
				}
		}
		public void menuDeselected(MenuEvent event){
			
		}
		public void menuCanceled(MenuEvent event){
			
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
		NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
		ResultSet rs=null;
		try{
			rs=DataSet.QueryExec("Select trim(client.name), sum(document.sum*curs_now.curs) from (document inner join client on client.id_client=document.id_client) inner join curs_now on curs_now.id_val=document.id_val where numb is NULL group by trim(name),document.id_client",true );
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

			int numb=0;
			int id=0;
			boolean isOpt=true;
			
			try{
				DataSet.UpdateQuery("lock table document in exclusive mode");
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
				rs=DataSet.QueryExec("select max(numb) from document where (to_number(to_char(day, 'YYYY'))=to_number(to_char(sysdate, 'YYYY'))) and (id_type_doc=2)", false) ;
				if (rs.next())
					numb=rs.getInt(1);
				String SQL="Select id_doc from document where (numb is NULL) and (id_client=(select id_client from client where name='"+tovar+"'))";
				rs=DataSet.QueryExec(SQL, false);
				while (rs.next()){
					numb++;
					id=rs.getInt(1);
					DataSet.UpdateQuery("update document set numb="+numb+", day=sysdate where id_doc="+id);
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
					rs=DataSet.QueryExec("select sum, trim(note), disc, trim(val.name), trim(manager.name), trim(sklad.name) from ((document inner join val on document.id_val=val.id_val) inner join manager on document.id_manager=manager.id_manager) inner join " +
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
						OutputOO.OpenDoc("nakl_roz.ots",true);
						OutputOO.InsertOne("\""+now.get(Calendar.DAY_OF_MONTH)+"\" "+Month(now.get(Calendar.MONTH))+" "+now.get(Calendar.YEAR)+"г.", 10, true, 3,1);
						OutputOO.InsertOne("Накладная №"+numb+pref, 16, true, 1, 2);
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
	
}

