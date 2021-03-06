import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;


class naklTableModel extends AbstractTableModel{
	private dataCont nakl;
	private boolean Editable = false;
	private boolean isReal;
	private boolean changed=false;
	private boolean changeNakl=false;
	public naklTableModel(String aClient, String aSklad, IndividualDiscount aDiscount, boolean aIsReal ){
		nakl = new dataCont(aClient,aSklad,aDiscount);
		isReal=aIsReal;
	}
	public void setClient(String aValue){
		nakl.setNameClient(aValue);
		setChanged(true);
		fireTableChanged(new TableModelEvent(this));
	}
	public void setSklad(String aValue){
		nakl.setNameSklad(aValue);
		setChanged(true);
		fireTableStructureChanged();
	}
	public void setIndDiscount(IndividualDiscount aValue){
		nakl.setIndDiscount(aValue);
		setChanged(true);
		fireTableChanged(new TableModelEvent(this));
	}
	public boolean getEditable(){
		return Editable;
	}
	public void setEditable(boolean aValue){
		Editable=aValue;
	}
	public int getRowCount(){
		return nakl.getSize();
	}
	public int getColumnCount(){
		return 6;
	}
	public Object getValueAt(int row, int column){
		return nakl.getValueAt(row, column);
	}
	public String getColumnName(int column){
		switch (column){
		case 0:
			return "�";
		case 1:
			return "������������";
		case 2:
			return "���-��";
		case 3:
			return "����";
		case 4:
			return "�����";
		case 5:
			return "������";
		default:
			return "";
		}
	}	
	public void setValueAt(Object aValue, int row, int column){
		nakl.setValueAt(aValue, row, column);
		setChanged(true);
//		fireTableCellUpdated(row, column);
		fireTableRowsUpdated(row, row);
	}
	public boolean isCellEditable(int row, int column){
		if (row>nakl.getSize()){
			return false;
		}else 
			if (getEditable()){
				switch (column){
				case 0:
					return false;
				case 1:
					return false;
				case 2:
					return true;
				case 3:
					return true;
				case 4:
					return false;
				case 5:
					return !isReal;
				default:
					return false;
				}	
			}else{
				return false;
			}
	}
	public int present(String aName){
		return nakl.present(aName);	
	}
	public int getIndDiscount(int id_group){
		return nakl.getIndDiscount(id_group);
	}
	public int add(String aName, int aCount, double aCost, int aDiscount, int aAkcia, int aGroup){
		boolean b=false;
		if (aAkcia>0)
			b=true;
		setChanged(true);
		int ret=nakl.add(aName, aCount, aCost, aDiscount, b, isReal, aGroup);
		fireTableStructureChanged();
		return ret;
		
	}
	public void removeAll(){
		int all=nakl.getSize();
		for (int i=0;i<all;i++)
			nakl.remove(0);
		fireTableStructureChanged();
	}
	public void removeRow(int aRow){
		nakl.remove(aRow);
		setChanged(true);
		fireTableRowsUpdated(aRow, nakl.getSize());
	}
	public double summ(){
		NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
		double ret=0;
		for (int i=0;i<nakl.getSize();i++){
			int j=1;
			if (nakl.getAkcia(i))
				j=0;
			ret=ret+((Double)nakl.getValueAt(i, 4))*(1-((new Integer(nakl.getIndDiscount((Integer)nakl.getValueAt(i, 6)))).doubleValue()/100)*j)*(1-((Integer)nakl.getValueAt(i, 5)).doubleValue()/100);
		}
		String s = formatter.format(ret);
		return (new Double(s));
	}
	public double summvo(){
		NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
		double ret=0;
		for (int i=0;i<nakl.getSize();i++)
			ret=ret+((Double)nakl.getValueAt(i, 4));
		String s = formatter.format(ret);
		return (new Double(s));
	}
	public double summAkcia(){
		NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
		double ret=0;
		for (int i=0;i<nakl.getSize();i++){
			if (nakl.getAkcia(i))
				ret=ret+((Double)nakl.getValueAt(i, 4))*(1-((Integer)nakl.getValueAt(i, 5)).doubleValue()/100);
		}
		String s = formatter.format(ret);
		return (new Double(s));
	}
	public boolean getAkcia(int pos){
		return nakl.getAkcia(pos);
	}
	public boolean isChanged() {
		return changed;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	public String getCommaName(){
		return nakl.getCommaName();
	}
	public Double getSumByName(String name){
		return nakl.getSumByName(name);
	}
	public void setStateNakl(){
		if (getRowCount()>0 && !changeNakl){
			changeNakl=true;
			setStateInTable(true);
		}
		if (getRowCount()==0 && changeNakl){
			changeNakl=false;
			setStateInTable(false);
		}
	}
	public void setStateInTable(boolean increase){
		try{
			String sql=String.format("update client set editing=nvl(editing,0)%s where name='%s'",increase?"+1":"-1", nakl.getNameClient());
			DataSet.QueryExec1(sql, true);
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null, "������ ������ ��������� �������.", "������!", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		
	}
	public int getRowDiscount(int row){
		return nakl.getRowDiscount(row);
	}
}
class dataCont{
	private Vector name, count,cost,discount,akcia,group;
	private String nameClient, nameSklad;
	private IndividualDiscount inddiscount;
	public dataCont (String anameClient,String anameSklad, IndividualDiscount ainddiscount){
		name=new Vector<String>(0);
		count=new Vector<Integer>(0);
		cost=new Vector<Double>(0);
		discount=new Vector<Integer>(0);
		akcia=new Vector<Boolean>(0);
		nameClient=anameClient;
		nameSklad=anameSklad;
		inddiscount=ainddiscount;
		group=new Vector<Integer>(0);
	}
	public void setNameClient(String aValue){
		nameClient=aValue;
	}
	public void setNameSklad(String aValue){
		nameSklad=aValue;
	}
	public void setIndDiscount(IndividualDiscount aValue){
		inddiscount=aValue;
	}

	public String getNameClient(){
		return nameClient;
	}
	
	public int getIndDiscount(int id_group){
		return inddiscount.getDiscount(id_group);
	}
	
	public int getRowDiscount(int row){
		int ret=0;
		if (!getAkcia(row)){
			ret=100-((100-(Integer)getDiscount(row))*(100-getIndDiscount((Integer)getGroup(row)))/100);
		}else{
			ret=(Integer)getDiscount(row);
		}
		return ret;
	}
	
	public String getNameSklad(){
		return nameSklad;
	}
	private void newCount(int aCount){
		count.add(new Integer(aCount));
	}
	private void setCount(Object aCount, int pos){
		count.setElementAt(new Integer((String)aCount), pos);
	}
	private Object getCount(int pos){
		return count.elementAt(pos);
	}
	private void newName(String aName){
		name.add(aName);
	}
	private void setName(Object aName, int pos){
		name.setElementAt(aName, pos);
	}
	private String getName(int pos){
		return (String)name.elementAt(pos);
	}
	private void newCost(double aCost){
		cost.add(new Double(aCost));
	}
	private void setCost(Object aCost, int pos){
		cost.setElementAt(new Double ((String)aCost), pos);
	}
	private Object getCost(int pos){
		return cost.elementAt(pos);
	}
	private void newDiscount(int aDiscount){
		discount.add(new Integer(aDiscount));
	}
	private void setDiscount(Object aDiscount, int pos){
		discount.setElementAt(new Integer((String)aDiscount), pos);
	}
	private Object getDiscount(int pos){
		return discount.elementAt(pos);
	}
	private void newAkcia(boolean aAkcia){
		akcia.add(new Boolean(aAkcia));
	}
	public boolean getAkcia(int pos){
		return ((Boolean)akcia.elementAt(pos)).booleanValue();
	}
	private void newGroup(int aGroup){
		group.add(new Integer(aGroup));
	}
	private void setGroup(Object aGroup, int pos){
		group.setElementAt(new Integer((String)aGroup), pos);
	}
	private Object getGroup(int pos){
		return group.elementAt(pos);
	}
	public int add(String aName, int aCount, double aCost, int aDiscount, boolean aAkcia, boolean aReal, int aGroup){
		int pr;
		if (aReal)
			pr=present(aName,aCost);
		else
			pr=-1;
		if (pr==-1){
			newCount(aCount);
			newName(aName);
			newCost(aCost);
			newDiscount(aDiscount);
			newAkcia(aAkcia);
			newGroup(aGroup);
			return getSize()-1;
		}else{
			setCount((new Integer((Integer)getCount(pr) +aCount)).toString(), pr);
			return pr;
		}
	}
	public boolean remove(int pos){
		if (getSize()<pos){
			return false;
		}else{
			name.removeElementAt(pos);
			count.removeElementAt(pos);
			cost.removeElementAt(pos);
			discount.removeElementAt(pos);
			akcia.removeElementAt(pos);
			group.removeElementAt(pos);
			return true;
		}
	}
	public int getSize(){
		return name.size();
	}
	public Object getValueAt(int row, int column){
		NumberFormat formatter = new DecimalFormat ( "0.00" ) ;
		String s="";
		switch (column){
		case 0:
			return (Integer)row+1;
		case 1:
			return getName(row);
		case 2:
			return getCount(row);
		case 3:
//		    s = formatter.format ( (Double)(((Double)getCost(row)).doubleValue()*(1-((Integer)getDiscount(row)).doubleValue()/100)) ) ;
			s = formatter.format ( (Double)(((Double)getCost(row)).doubleValue()));
			return new Double(s);
		case 4:
//			s = formatter.format ( (Double)(((Integer)getCount(row)).intValue()*((Double)getCost(row)).doubleValue()*(1-((Integer)getDiscount(row)).doubleValue()/100)) ) ;
			s = formatter.format ( (Double)(((Integer)getCount(row)).intValue()*((Double)getCost(row)).doubleValue()) ) ;
			return new Double(s);
		case 5:
			return getDiscount(row);
		case 6:
			return getGroup(row);
		default:
			return null;
		}
	}
	public void setValueAt(Object aValue, int row, int column){
		switch (column){
		case 1:
			setName(aValue,row);
			break;
		case 2:
			setCount(aValue, row);
			break;
		case 3:
			setCost(aValue,row);
			break;
		case 5:
			setDiscount(aValue,row);
			break;
		}
	}
	public int present(String aName,double cost){
		int ret=-1;
		for (int i=0;i<=getSize()-1;i++)
			if ((getName(i)).equals(aName) && Math.abs(((Double)getCost(i)).doubleValue()-cost)<0.001)
				ret=i;
		return ret;	
	}
	public int present(String aName){
		int ret=-1;
		for (int i=0;i<=getSize()-1;i++)
			if ((getName(i)).equals(aName))
				ret=i;
		return ret;	
	}
	public String getCommaName(){
		String commaText="";
		for (int i=0;i<name.size();i++ ){
			if ((Integer)discount.get(i)==0)
				commaText=commaText+", '"+name.get(i)+"'";
		}
		if (commaText.length()>0) 
			commaText=commaText.substring(2);
		return commaText;
	}
	public Double getSumByName(String aname){
		return  (Double)(((Integer)getCount(name.indexOf(aname))).intValue()*((Double)getCost(name.indexOf(aname))).doubleValue()); 
	}
}