import java.awt.Toolkit;
import java.io.IOException;


public class ChooserStreamIn implements Runnable{
	private static int idStream;
	private static String Sklad;
	private static String Price;
	private static Object parent;
	private static SerialPortReader port=null;
	public ChooserStreamIn(){
		
	}
	public static void setIdStream(int idStream) {
		ChooserStreamIn.idStream = idStream;
	}
	public static int getIdStream() {
		return idStream;
	}
	public static void setSklad(String sklad) {
		Sklad = sklad;
	}
	public static String getSklad() {
		return Sklad;
	}
	public static void setPrice(String price) {
		Price = price;
	}
	public static String getPrice() {
		return Price;
	}
	public static void StreamIn(String cod){
		switch (getIdStream()){
		case 1:
			try {
					if (!(((NewSaleFrame)parent).formInput==null) && ((NewSaleFrame)parent).formInput.isVisible())
						((NewSaleFrame)parent).formInput.closeDialog();
					((NewSaleFrame)parent).Input(inputBarcode.newcod(cod, getSklad(), getPrice()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toolkit.getDefaultToolkit().beep();
					e.printStackTrace();
				}
			return;
		}
	}
	public static void init(int aidStream, String aSklad, String aPrice, Object aparent){
		setIdStream(aidStream);
		setPrice(aPrice);
		setSklad(aSklad);
		setParent(aparent);
		if (port==null)
			port=new SerialPortReader();
		try {
			port.connect("COM1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void close(){
		setIdStream(0);
		port.disconnect();
		port=null;
	}
	public static void setParent(Object parent) {
		ChooserStreamIn.parent = parent;
	}
	public static Object getParent() {
		return parent;
	}
	public void run(){
		
	}
}