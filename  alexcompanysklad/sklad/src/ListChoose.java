import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

class DataNode{
	private String Name;
	private int Index;
	public DataNode(String aName, int aIndex){
		Name=aName;
		Index=aIndex;
	}
	public Object getValue(){
		return Name; 
		}
	public int getIndex(){
		return Index;
	}
	public String toString(){
		return Name;
	}
}
class GroupTreeModel implements TreeModel{
	private DataNode root;
	private EventListenerList listenerList = new EventListenerList(); 
	public GroupTreeModel(){
		root=new DataNode ("Все группы",-1);
	}
	public Object getRoot(){
		return root;
	}
	public Object getChild(Object parent,int index){
		String SQL; 
		DataNode child=null;
		if (((DataNode)parent).getIndex()==-1){
			SQL="select trim(name),id_group from groupid where parent_group is NULL order by name";
		}else{
			SQL="select trim(name),id_group from groupid where parent_group = "+((DataNode)parent).getIndex()+" order by name";
		}
		ResultSet rs=DataSet.QueryExec(SQL,true);
		try{
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
		if (((DataNode)parent).getIndex()==-1){
			SQL="select count(*) from groupid where parent_group is NULL";
		}else{
			SQL="select count(*) from groupid where parent_group = "+((DataNode)parent).getIndex();
		}
		ResultSet rs=DataSet.QueryExec(SQL,true);
		try{
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
	public void valueForPathChanged(TreePath path,Object newValue){
		
	}
	public int getIndexOfChild(Object parent,Object child){
		int count=-1;
		String SQL; 
		if (((DataNode)parent).getIndex()==-1){
			SQL="select trim(name),id_group from groupid where parent_group is NULL order by name";
		}else{
			SQL="select trim(name),id_group from groupid where parent_group = "+((DataNode)parent).getIndex()+" order by name";
		}
		ResultSet rs=DataSet.QueryExec(SQL,true);
		try{
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
	
}

public class ListChoose extends JPanel {
	String Sklad;
	GroupTreeModel model;
	DefaultListModel modelList;
	JList nameList;
	JTree groupTree;
	private JButton okButton;
	private boolean ok;
	private JDialog dialog;
	public ListChoose() {
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		//panel.setLayout(null);
		model= new GroupTreeModel();
		groupTree=new JTree(model);
		groupTree.setRootVisible(false);
		groupTree.setShowsRootHandles(true);
		groupTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		groupTree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent event){
				int index;
				index=((DataNode)event.getPath().getLastPathComponent()).getIndex();
				initList(index);
			}
		});
		modelList=new DefaultListModel();
		nameList=new JList(modelList);
		nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nameList.setVisibleRowCount(22);
		nameList.setFixedCellWidth(300);
		nameList.setFixedCellHeight(16);
		JScrollPane listScroller = new JScrollPane(nameList);
		JScrollPane treeScroller = new JScrollPane(groupTree);
		nameList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event){
				if (event.getClickCount()==2){
					ok=true;
					dialog.setVisible(false);
				}
			}
		});
//		treeScroller.setBounds(5, 5, 180, 200);
		panel.add(treeScroller);
		panel.add(listScroller);
//		panel.setBounds(arg0, arg1, arg2, arg3)
//		setSize(400,300);
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
	

	public String getTovar(){
		return (String)nameList.getSelectedValue();
	}
	public boolean showDialog(Component parent, String title, String aSklad){
		
		Sklad=aSklad;
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
		dialog.pack();
		dialog.setLocation(400-dialog.getWidth()/2, 300-dialog.getHeight()/2);
		dialog.setVisible(true);
		return ok;
	}
	private void initList(int aIndex){
		modelList.clear();
		String Query="select trim(name) from (Select distinct tovar.name from kart inner join tovar on kart.id_tovar=tovar.id_tovar where (kart.id_group="+aIndex+") and (kart.id_skl=(Select id_skl from sklad where name='"+Sklad+"')) order by tovar.name)";
		ResultSet rs=DataSet.QueryExec(Query,true);
		try {
			while (rs.next())
				modelList.addElement(rs.getString(1));
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
