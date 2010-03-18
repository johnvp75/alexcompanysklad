import java.awt.event.MouseAdapter;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.sun.star.awt.MouseEvent;


public class SelectDoc extends MyPanel {
	private int id_doc;
	private int type_doc;
	private GregorianCalendar startDate=new GregorianCalendar();
	private GregorianCalendar endDate=new GregorianCalendar();;
	private boolean register;
	private JTable docTable;
	private Vector<Integer> Id_doc;
	public SelectDoc(int Type_doc, boolean Register) {
		setType_doc(Type_doc);
		setRegister(Register);
		setLayout(null);
		JLabel docLab=new JLabel("Выберите документ для редактирования");
		String SQL="select trim(client.name), document.sum, trim(val.name), trim(sklad.name), trim (document.note), document.id_doc from ((document inner join client on document.id_client=client.id_client) " +
				"inner join val on document.id_val=val.id_val) inner join sklad on document.id_skl=sklad.id_skl where id_type_doc="+getType_doc();
		if (!isRegister())
			SQL=SQL+" and (numb is null)";
		SQL=SQL+" order by UPPER(CLIENT.name), UPPER(Sklad.name), sum";
		Id_doc=new Vector<Integer>(0);
		Vector<Vector<String>> Rows=new Vector<Vector<String>>(0); 
		try {
			ResultSet rs=DataSet.QueryExec(SQL, false);
			while (rs.next()){
				Id_doc.add(rs.getInt(6));
				Vector<String> item = new Vector<String>(0);
				item.add(rs.getString(4));
				item.add(rs.getString(1));
				item.add(rs.getString(2));
				item.add(rs.getString(3));
				item.add(rs.getString(5));
				Rows.add(item);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Vector<String> Column=new Vector<String>(0);
		Column.add("Склад");
		Column.add("Клиент");
		Column.add("Сумма");
		Column.add("Валюта");
		Column.add("Примечание");
		docTable = new JTable(new DefaultTableModel(Rows,Column){
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		docTable.setCellSelectionEnabled(false);
		docTable.setColumnSelectionAllowed(false);
		docTable.setRowSelectionAllowed(true);
//		docTable.setSelectionMode(SINGLE_SELECTION);
		JScrollPane ScrollTable=new JScrollPane(docTable);
		docLab.setBounds(10,5,700,22);
		ScrollTable.setBounds(6, 89, 769, 335);
		add(docLab);
		add(ScrollTable);
		docTable.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.ClickCount==2 && docTable.getSelectedRow()>-1){
					
				}
			}
		});
	}
	public int getId_doc() {
		return id_doc;
	}
	public void setId_doc(int idDoc) {
		id_doc = idDoc;
	}
	public int getType_doc() {
		return type_doc;
	}
	public void setType_doc(int typeDoc) {
		type_doc = typeDoc;
	}
	public GregorianCalendar getStartDate() {
		return startDate;
	}
	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}
	public GregorianCalendar getEndDate() {
		return endDate;
	}
	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}
	public boolean isRegister() {
		return register;
	}
	public void setRegister(boolean register) {
		this.register = register;
	}
	

}
