import java.util.Vector;

public class IndividualDiscount {
	private int generalDiscount=0;
	private int id_client;
	private int id_skl;
	Vector<DiscountElement> discountElements;

	public IndividualDiscount(int aGeneralDiscount) {
		generalDiscount=aGeneralDiscount;
		discountElements=new Vector<DiscountElement>(0);
	}
	
	public void addDiscount(int id_group,int discount){
		discountElements.addElement(new DiscountElement(id_group, discount));
	}
	public int getDiscount(int id_group){
		int disc=0;
		boolean done=false;
		int i=0;
		while (!done&&i<discountElements.size()){
			if (discountElements.get(i).getId_group()==id_group){
				disc=discountElements.get(i).getDiscount();
				done=true;
			}
			i++;
		}
		return done?disc:generalDiscount;
	}

	public void setGeneralDiscount(int generalDiscount) {
		this.generalDiscount = generalDiscount;
	}	
}
