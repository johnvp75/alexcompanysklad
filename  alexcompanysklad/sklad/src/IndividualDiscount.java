import java.util.Vector;

public class IndividualDiscount {
//	private int generalDiscount=0;
	private int id_client;
//	private int id_skl;
	Vector<DiscountElement> discountElements;
	Vector<GeneralDiscountElement> generalDiscount;

	public IndividualDiscount() {
//		generalDiscount=aGeneralDiscount;
		discountElements=new Vector<DiscountElement>(0);
		generalDiscount=new Vector<GeneralDiscountElement>(0);
	}
	
	public void addDiscount(int id_group,int discount,int id_skl){
		discountElements.addElement(new DiscountElement(id_group, discount,id_skl));
	}
	public int getDiscount(int id_group,int id_skl){
		int disc=0;
		boolean done=false;
		int i=0;
		while (!done&&i<discountElements.size()){
			if (discountElements.get(i).getId_group()==id_group && discountElements.get(i).getId_skl()==id_skl){
				disc=discountElements.get(i).getDiscount();
				done=true;
			}
			i++;
		}
		if (done) 
			return disc;
		i=0;
		while (!done&&i<generalDiscount.size()){
			if (generalDiscount.get(i).getId_skl()==id_skl){
				disc=generalDiscount.get(i).getGeneralDiscount();
				done=true;
			}
			i++;
		}
		return done?disc:0;
	}

	public void addGeneralDiscount(int generalDiscount,int id_skl) {
		this.generalDiscount.add(new GeneralDiscountElement(generalDiscount, id_skl));
	}

	public int getId_client() {
		return id_client;
	}

	public void setId_client(int id_client) {
		this.id_client = id_client;
	}	
}
