import java.sql.ResultSet;
import java.util.EventListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

class GroupTreeModel implements TreeModel{
	private DataNode root;
	private EventListenerList listenerList = new EventListenerList(); 
	public GroupTreeModel(){
		root=new DataNode ("Все группы",-2);
	}
	public void setRoot(){
		fireTreeStructureChanged(root);
	}
	public Object getRoot(){
		return root;
	}
	public Object getChild(Object parent,int index){
		String SQL; 
		DataNode child=null;
		if (((DataNode)parent).getIndex()==-2){
			SQL="select trim(name),id_group from groupid where parent_group is NULL order by upper(name)";
		}else{
			SQL="select trim(name),id_group from groupid where parent_group = "+((DataNode)parent).getIndex()+" order by upper(name)";
		}
		
		try{
			ResultSet rs=DataSet.QueryExec(SQL,true);
			for(int i=0;i<=index;i++)
				rs.next();
//			rs.absolute(index+1);
			child=new DataNode(rs.getString(1),rs.getInt(2));
			rs.close();
		}catch(Exception e) {e.printStackTrace(); }
		return child;
	}
	public int getChildCount(Object parent){
		int count=0;
		String SQL; 
		if (((DataNode)parent).getIndex()==-2){
			SQL="select count(*) from groupid where parent_group is NULL";
		}else{
			SQL="select count(*) from groupid where parent_group = "+((DataNode)parent).getIndex();
		}
		
		try{
			ResultSet rs=DataSet.QueryExec(SQL,true);
			if (!(rs==null)){ 
				rs.next();
				count=rs.getInt(1);
				rs.close();
			}
		}catch(Exception e) {e.printStackTrace(); }
		return count;
	}
	public boolean isLeaf(Object node){
		return false;
	}
	public int getIndexOfChild(Object parent,Object child){
		int count=-1;
		String SQL; 
		if (((DataNode)parent).getIndex()==-2){
			SQL="select trim(name),id_group from groupid where parent_group is NULL order by upper(name)";
		}else{
			SQL="select trim(name),id_group from groupid where parent_group = "+((DataNode)parent).getIndex()+" order by upper(name)";
		}
		
		try{
			ResultSet rs=DataSet.QueryExec(SQL,true);
			int i=-1;
			while (rs.next()){
				i++;
				if ((rs.getString(1).equals(((DataNode)child).toString()))&& (rs.getInt(2)==((DataNode)child).getIndex())){
					count=i;
					break;
				}
			}
			rs.close();
		}catch(Exception e) {e.printStackTrace(); }
		return count;
	}
	public void addTreeModelListener(TreeModelListener l){
		listenerList.add(TreeModelListener.class, l);
	}
	public void removeTreeModelListener(TreeModelListener l){
		listenerList.remove(TreeModelListener.class, l);
	}
	public void update(){
		
	}
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}
	protected void fireTreeStructureChanged(Object oldRoot){
		TreeModelEvent event = new TreeModelEvent(this, new Object [] {oldRoot});
		EventListener[] listeners= listenerList.getListeners(TreeModelListener.class);
		for (int i=0; i<listeners.length; i++)
			((TreeModelListener)listeners[i]).treeStructureChanged(event);
	}
	
}
