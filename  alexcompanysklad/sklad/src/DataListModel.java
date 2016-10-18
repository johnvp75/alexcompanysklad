import java.util.Vector;
import javax.swing.AbstractListModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Жека
 */
public class DataListModel extends AbstractListModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<String> data;
    public DataListModel(){
        data = new Vector<String>(0);
    }
    public Object getElementAt(int pos){
        if (pos<0 && pos==getSize())
            return -1;
        else
            return data.get(pos);
    }
    public int getSize(){
        return data.size();
    }
    public void add(String aValue){
        data.add(aValue);
        fireContentsChanged(this, 0, getSize()-1);

    }
    public void remove(int pos){
        data.remove(pos);
        fireContentsChanged(this, 0, getSize()-1);
    }
    public int pos(String aValue){
        return data.indexOf(aValue);
    }
    public void removeAll(){
        data.clear();
        fireContentsChanged(this, 0, getSize()-1);
    }

}