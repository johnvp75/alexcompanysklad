
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
	
	
}
