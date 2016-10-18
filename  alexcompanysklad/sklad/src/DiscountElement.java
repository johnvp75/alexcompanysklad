
public class DiscountElement {
	private int id_group;
	private int discount;
	private int id_skl;
	public DiscountElement(int aId_group,int aDiscount, int aId_skl){
		id_group=aId_group;
		discount=aDiscount;
		id_skl=aId_skl;
	}
	public int getId_group() {
		return id_group;
	}
	public int getDiscount() {
		return discount;
	}
	public int getId_skl() {
		return id_skl;
	}
	
	
}
