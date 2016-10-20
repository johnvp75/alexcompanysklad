import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;




public class SelectDoc extends MyPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id_doc;
	private int type_doc;
	private GregorianCalendar startDate=new GregorianCalendar();
	private GregorianCalendar endDate=new GregorianCalendar();;
	private boolean register;
	private JTable docTable;
	private Vector<Integer> Id_doc;
	private NewSaleFrame saleFrame;
	private JPopupMenu popup;
	private int p;
	public SelectDoc(int Type_doc, boolean Register,NewSaleFrame saleframe) {
		saleFrame=saleframe;
		setType_doc(Type_doc);
		setRegister(Register);
		setLayout(null);
		JLabel docLab=new JLabel("Выберите документ для редактирования");
		docTable = new JTable();
		docTable.setCellSelectionEnabled(false);
		docTable.setColumnSelectionAllowed(false);
		docTable.setRowSelectionAllowed(true);
//		docTable.setSelectionMode(SINGLE_SELECTION);
		popup=new JPopupMenu();
		JMenuItem edit = new JMenuItem("Редактировать накладную");
		JMenuItem delete = new JMenuItem("Удалить накладную");
		popup.add(edit);
		popup.add(delete);
		JScrollPane ScrollTable=new JScrollPane(docTable);
		docLab.setBounds(10,5,700,22);
		ScrollTable.setBounds(6, 89, 769, 335);
		add(docLab);
		add(ScrollTable);
		docTable.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount()==2 && docTable.getSelectedRow()>-1){
					saleFrame.showform(Id_doc.elementAt(docTable.getSelectedRow()));
				}
			}
			public void mousePressed(MouseEvent event){
				if (event.getButton()==MouseEvent.BUTTON3){
					popup.show(docTable, event.getX(), event.getY());
					p=event.getY();
				}
			}
		});
		delete.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent event){
				int row=p/docTable.getRowHeight();
				try{
					int id_doc=Id_doc.get(row);
					String SQL=String.format("Select d.*,l.* from document d,lines l where d.id_doc=%s and l.id_doc=d.id_doc for update nowait", id_doc);
					try{
						DataSet.QueryExec(SQL, false);
					}catch(Exception e){
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Документ заблокирован другим пользователем! Попробуйте позже.", "Ошибка блокировки", JOptionPane.ERROR_MESSAGE);
						return;
					}
					SQL=String.format("Select trim(c.name), d.sum from client c, document d where c.id_client=d.id_client and d.id_doc=%s " , id_doc);
					ResultSet rs=DataSet.QueryExec(SQL, false);
					rs.next();
					String message=String.format("Вы уверенны, что хотите удалить накладную %s на сумму: %s", rs.getString(1),rs.getString(2));
					if (JOptionPane.showConfirmDialog(null, message,"Удаление",JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION){
						DataSet.rollback();
						return;
					}
					SQL=String.format("delete from lines where id_doc=%s", id_doc);
					DataSet.UpdateQuery(SQL);
					SQL=String.format("delete from document where id_doc=%s", id_doc);
					DataSet.UpdateQuery(SQL);
					DataSet.commit();
					initform();
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "Ошибка удаления! Повторите попытку.", "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					try{
						DataSet.rollback();
						return;
					}catch(Exception exp){
						exp.printStackTrace();
						JOptionPane.showMessageDialog(null, "Критическая ошибка. Обратитесь к администратору!/n(ERROR-03)", "Ошибка", JOptionPane.ERROR_MESSAGE);
						return;
					}

				}
			}
		});
		edit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int id_doc=Id_doc.get(p/docTable.getRowHeight());
				saleFrame.showform(id_doc);
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
	public void initform(){
		String SQL="select trim(client.name), document.sum, trim(val.name), trim(sklad.name), trim(document.note), document.id_doc from ((document inner join client on document.id_client=client.id_client) " +
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
				item.add(rs.getString(5).substring(1));
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
		docTable.setModel(new DefaultTableModel(Rows,Column){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column){
				return false;
			}
		});

	}
	

}
