import java.sql.ResultSet;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class SelectDoc extends MyPanel {
	private int id_doc;
	private int type_doc;
	private GregorianCalendar startDate=new GregorianCalendar();
	private GregorianCalendar endDate=new GregorianCalendar();;
	private boolean register;
	private JTable docTable;
	public SelectDoc(int Type_doc, boolean Register) {
		setType_doc(Type_doc);
		setRegister(Register);
		setLayout(null);
		JLabel docLab=new JLabel("Выберите документ для редактирования");
		String SQL="select trim(client.name), document.sum, trim(val.name), trim(sklad.name) from ((document inner join client on document.id_client=client.id_client) " +
				"inner join val on document.id_val=val.id_val) inner join sklad on document.id_skl=sklad.id_skl where id_type_doc="+getType_doc();
		if (!isRegister())
			SQL=SQL+" and (numb is null)";
		SQL=SQL+" order by UPPER(CLIENT.name), UPPER(Sklad.name), sum";
		Vector<Vector<String>> Rows=new Vector<Vector<String>>(0); 
		try {
			ResultSet rs=DataSet.QueryExec(SQL, false);
			while (rs.next()){
				Vector<String> item = new Vector<String>(0);
				item.add(rs.getString(4));
				item.add(rs.getString(1));
				item.add(rs.getString(2));
				item.add(rs.getString(3));
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
		docTable = new JTable(new DefaultTableModel(Rows,Column){
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		JScrollPane ScrollTable=new JScrollPane(docTable);
		docLab.setBounds(10,5,700,22);
		ScrollTable.setBounds(6, 89, 769, 335);
		add(docLab);
		add(ScrollTable);
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
