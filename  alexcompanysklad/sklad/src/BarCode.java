import java.sql.ResultSet;


public class BarCode {
	protected String code;
	protected int idTovar;
	protected int idSklad;
	protected int countTovarForOneCode;
	protected Boolean barcodeForShops;

	public BarCode(String code, int idTovar, int idSklad,
			int countTovarForOneCode, Boolean barcodeForShops) {
		super();
		this.code = code;
		this.idTovar = idTovar;
		this.idSklad = idSklad;
		this.countTovarForOneCode = countTovarForOneCode;
		this.barcodeForShops = barcodeForShops;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getIdTovar() {
		return idTovar;
	}
	public void setIdTovar(int idTovar) {
		this.idTovar = idTovar;
	}
	public int getIdSklad() {
		return idSklad;
	}
	public void setIdSklad(int idSklad) {
		this.idSklad = idSklad;
	}
	public int getCountTovarForOneCode() {
		return countTovarForOneCode;
	}
	public void setCountTovarForOneCode(int countTovarForOneCode) {
		countTovarForOneCode = countTovarForOneCode;
	}
	public Boolean getBarcodeForShops() {
		return barcodeForShops;
	}
	public void setBarcodeForShops(Boolean barcodeForShops) {
		this.barcodeForShops = barcodeForShops;
	}
	public static String GenerateBarCode(int group, boolean special) throws Exception{
		int num=1;
		String SQL=String.format("select max(substr(bar_code,%s,5)) from bar_code where bar_code like '%s%s'", (new Integer(group)).toString().length()+1,group,"%");
		if (special){
			SQL=String.format("select max(substr(barcode,%s,5)) from glassforshop where barcode like '%s%s'", (new Integer(group)).toString().length()+1,group,"%");
		}	
		ResultSet rs=DataSet.QueryExec1(SQL, false);
		if (rs.next())
			num=rs.getInt(1);
        String code=String.format("%s%05d", group,num+1);
        String code_sum=String.format("%07d%05d", group,num+1);
        Integer sum=new Integer(0);
        for (int i=2;i<13;i=i+2)
            sum=sum+(Integer.valueOf(code_sum.substring(i-1, i)));
        sum=sum*3;
        for (int i=1;i<12;i=i+2)
            sum=sum+(Integer.valueOf(code_sum.substring(i-1, i)));
        sum=10-((Double)((((sum.doubleValue()/10)-sum/10)*10)+0.1)).intValue();
        code=code+sum.toString().substring(sum.toString().length()-1);
        return code;

	}
	
}
