import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;


public class inputBarcode {
	private static TovarChooser tovchoose=null;
	private static NewTovar newTovar;
	public static RetBarCode newcod(String cod, String sklad, String price) throws IOException{
		
		int count=0;
		if (cod==null)
			throw new IOException();
		ResultSet rs=null;
		if (cod.charAt(0)=='+'){
//		Новый товар
			if (newTovar==null)
				newTovar= new NewTovar();
			newTovar.setSklad(sklad);	
			newTovar.setPrice(price);
			newTovar.setTovar(cod.substring(1));
			if (newTovar.showDialog(null, "Новый товар"))
				return new RetBarCode(newTovar.getTovar(),1);
			else
				throw new IOException();
		}
		try {
			if (cod.charAt(0)=='*')
				rs=DataSet.QueryExec("select count(*) from tovar inner join kart on tovar.id_tovar=kart.id_tovar where lower(tovar.name) like '%"+cod.substring(1).toLowerCase()+"%' and kart.id_skl=(select id_skl from SKLAD where name='"+sklad+"')",true);
			else
				rs=DataSet.QueryExec("select count(*) from BAR_CODE where BAR_CODE='"+cod+"' and id_skl = (select id_skl from SKLAD where name='"+sklad+"')",true);
 
			rs.next();
			count=rs.getInt(1);
			rs.close();
		}
		catch (Exception e) {e.printStackTrace(); }
		if (count==0) throw new IOException();
		try {
			if (cod.charAt(0)=='*')
				rs=DataSet.QueryExec("select trim(name), 1 as count from (select distinct tovar.name from tovar inner join kart on tovar.id_tovar=kart.id_tovar where lower(tovar.name) like '%"+cod.substring(1).toLowerCase()+"%' and kart.id_skl=(select id_skl from SKLAD where name='"+sklad+"') order by tovar.name)",true);
			else
				rs=DataSet.QueryExec("select trim(name), nvl(count,1) from (select distinct tovar.name,bar_code.count from tovar inner join BAR_CODE on tovar.id_tovar=bar_code.id_tovar where bar_code.BAR_CODE='"+cod+"' and bar_code.id_skl = (select id_skl from SKLAD where name='"+sklad+"') order by tovar.name)",true);
			rs.next();
			if (count==1)
				return new RetBarCode(rs.getString(1),rs.getInt(2));
			else{
				Toolkit.getDefaultToolkit().beep();
				Vector<String> data =new Vector<String>(0); 
				if (tovchoose==null)
					tovchoose=new TovarChooser();
				while (!rs.isAfterLast()){
					String item=rs.getString(1);
					data.addElement(item);
					rs.next();
				}
				tovchoose.addTovar(data);
				if (tovchoose.showDialog(null, "Выбор товара")){
					String SQL="select nvl(b.count,1) from bar_code b,tovar t where t.name='"+tovchoose.getTovar().trim()+"'and t.id_tovar=b.id_tovar and b.bar_code='"+cod+"' and b.id_skl = (select id_skl from SKLAD where name='"+sklad+"')";
					rs=DataSet.QueryExec(SQL, false);
					int in =1;
					if (rs.next())
						in=rs.getInt(1);
					return new RetBarCode(tovchoose.getTovar(),in);
				}else throw new IOException();

			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
			}
		finally {
			try{
			rs.close();}
			catch (Exception e) {e.printStackTrace(); }
		}
	}
}
	class TovarChooser extends JPanel{
		private JList nameList;
		private JButton okButton;
		private boolean ok;
		private JDialog dialog;
		public TovarChooser() {
			setLayout(new BorderLayout());
			JPanel panel = new JPanel();
			nameList=new JList();
			nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//			JScrollPane listScroller = new JScrollPane(nameList);
			panel.add(new JScrollPane(nameList));
			nameList.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent event){
					if (event.getClickCount()==2){
						ok=true;
						dialog.setVisible(false);
					}
				}
			});
			add(panel,BorderLayout.CENTER);
			okButton = new JButton("Ok!");
			okButton.addActionListener(new 
					ActionListener()
					{
						public void actionPerformed(ActionEvent event)
						{
								ok=true;
								dialog.setVisible(false);
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
			buttonPanel.add(cancelButton);
			add(buttonPanel,BorderLayout.SOUTH);

		}
		public void addTovar(Vector<String> value){
			nameList.setListData(value);
			nameList.setSelectedIndex(0);
		}
		public String getTovar(){
			return (String)nameList.getSelectedValue();
		}
		public boolean showDialog(Component parent, String title){
			ok=false;
			Frame owner = null;
			if (parent instanceof Frame)
				owner = (Frame)parent;
			else
				owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
			if (dialog==null || dialog.getOwner()!=owner){
				dialog=new JDialog(owner,true);
				dialog.add(this);
				dialog.getRootPane().setDefaultButton(okButton);
				dialog.pack();
			}
			dialog.setTitle(title);
//			dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
			dialog.setLocationRelativeTo(parent);
			dialog.setVisible(true);
			return ok;
		}

	}


