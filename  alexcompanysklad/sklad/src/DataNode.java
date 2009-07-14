
public class DataNode{
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