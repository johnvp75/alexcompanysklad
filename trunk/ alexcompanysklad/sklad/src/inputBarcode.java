import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
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
import javax.swing.SwingUtilities;


public class inputBarcode {
	private static TovarChooser tovchoose=null;
	public static String newcod(String cod, String sklad) throws IOException{
		int count=0;
		ResultSet rs;
		if (cod.charAt(0)=='*')

			rs=DataSet.QueryExec("select count(*) from tovar inner join kart on tovar.id_tovar=kart.id_tovar where lower(tovar.name) like '%"+cod.substring(1).toLowerCase()+"%' and kart.id_skl=(select id_skl from SKLAD where name='"+sklad+"')");
		else
			rs=DataSet.QueryExec("select count(*) from BAR_CODE where BAR_CODE='"+cod+"' and id_skl = (select id_skl from SKLAD where name='"+sklad+"')");
		try { 
			rs.next();
			count=rs.getInt(1);
			rs.close();
		}
		catch (Exception e) { }
		if (count==0) throw new IOException();
		if (cod.charAt(0)=='*')
			rs=DataSet.QueryExec("select tovar.name from tovar inner join kart on tovar.id_tovar=kart.id_tovar where lower(tovar.name) like '%"+cod.substring(1).toLowerCase()+"%' and kart.id_skl=(select id_skl from SKLAD where name='"+sklad+"') order by tovar.name");
		else
			rs=DataSet.QueryExec("select tovar.name from tovar inner join BAR_CODE on tovar.id_tovar=bar_code.id_tovar where bar_code.BAR_CODE='"+cod+"' and bar_code.id_skl = (select id_skl from SKLAD where name='"+sklad+"') order by tovar.name");
		try {
			rs.next();
			if (count==1)
				return rs.getString(1);
			else{
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
					return tovchoose.getTovar();
				}else throw new IOException();

			}
			
		}
		catch (Exception e) {throw new IOException(); }
		finally {
			try{
			rs.close();}
			catch (Exception e) { }
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
			JScrollPane listScroller = new JScrollPane(nameList);
			panel.add(listScroller);
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
			dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
			dialog.setVisible(true);
			return ok;
		}

	}


