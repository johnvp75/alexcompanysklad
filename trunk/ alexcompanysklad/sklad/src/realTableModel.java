import java.util.Vector;
import javax.swing.table.AbstractTableModel;


class realTableModel extends AbstractTableModel{
	public realTableModel(String aClient, String aSklad, int aDiscount){
		nakl = new dataCont(aClient,aSklad,aDiscount);
	}
	dataCont nakl;
	private boolean Editable = false;
	public void setClient(String aValue){
		nakl.setNameClient(aValue);
	}
	public void setSklad(String aValue){
		nakl.setNameSklad(aValue);
	}
	public void setIndDiscount(int aValue){
		nakl.setIndDiscount(aValue);
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
					return true;
				default:
					return false;
				}	
			}else{
				return false;
			}
	}
}
class dataCont{
	private Vector name, count,cost,discount;
	private String nameClient, nameSklad;
	private int inddiscount;
	public dataCont (String anameClient,String anameSklad, int ainddiscount){
		name=new Vector(0);
		count=new Vector(0);
		cost=new Vector(0);
		discount=new Vector(0);
		nameClient=anameClient;
		nameSklad=anameSklad;
		inddiscount=ainddiscount;
	}
	public void setNameClient(String aValue){
		nameClient=aValue;
	}
	public void setNameSklad(String aValue){
		nameSklad=aValue;
	}
	public void setIndDiscount(int aValue){
		inddiscount=aValue;
	}
	public String getNameClient(){
		return nameClient;
	}
	public int getIndDiscount(){
		return inddiscount;
	}
	public String getNameSklad(){
		return nameSklad;
	}
	private void newCount(int aCount){
		count.add(new Integer(aCount));
	}
	private void setCount(Object aCount, int pos){
		count.setElementAt(aCount, pos);
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
		cost.setElementAt(aCost, pos);
	}
	private Object getCost(int pos){
		return cost.elementAt(pos);
	}
	private void newDiscount(double aDiscount){
		discount.add(new Double(aDiscount));
	}
	private void setDiscount(Object aDiscount, int pos){
		discount.setElementAt(aDiscount, pos);
	}
	private Object getDiscount(int pos){
		return discount.elementAt(pos);
	}
	public void add(String aName, int aCount, double aCost, double aDiscount){
		newCount(aCount);
		newName(aName);
		newCost(aCost);
		newDiscount(aDiscount);
	}
	/*
	public Boolean set(String aName, int aCount, double aCost, double aDiscount,int pos){
		if (getSize()<pos){
			return false;
		}else{
			setCount(aCount, pos);
			setName(aName, pos);
			setCost(aCost,pos);
			setDiscount(aDiscount, pos);
			return true;
		}
	}
	*/
	public boolean remove(int pos){
		if (getSize()<pos){
			return false;
		}else{
			name.removeElementAt(pos);
			count.removeElementAt(pos);
			cost.removeElementAt(pos);
			discount.removeElementAt(pos);
			return true;
		}
	}
	public int getSize(){
		return name.size();
	}
	public Object getValueAt(int row, int column){
		switch (column){
		case 0:
			return (Integer)row;
		case 1:
			return getName(row);
		case 2:
			return getCount(row);
		case 3:
			return getCost(row);
		case 4:
			return (Double)(((Integer)getCount(row)).intValue()*((Double)getCost(row)).doubleValue()*(1-((Double)getDiscount(row)).doubleValue()/100));
		case 5:
			return getDiscount(row);
		default:
			return null;
		}
	}
	public void setValueAt(Object aValue, int row, int column){
		switch (column){
		case 1:
			setName(aValue,row);
		case 2:
			setCount(aValue, row);
		case 3:
			setCost(aValue,row);
		case 4:
			setDiscount(aValue,row);
		}
	}
}