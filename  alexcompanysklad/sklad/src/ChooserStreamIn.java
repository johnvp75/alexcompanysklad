import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;


public class ChooserStreamIn implements Runnable{
	private volatile static int idStream;
	private volatile static String Sklad;
	private volatile static String Price;
	private InputStream in;
	private static Object parent;
//	private SerialPortReader port=null;
	private CommPort commPort;
	public ChooserStreamIn(){
		try {
			connect("COM1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setIdStream(int aidStream) {
		idStream = aidStream;
	}
	public static int getIdStream() {
		return idStream;
	}
	public void setSklad(String sklad) {
		Sklad = sklad;
	}
	public static String getSklad() {
		return Sklad;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public static String getPrice() {
		return Price;
	}
	public void init(int aidStream, String aSklad, String aPrice, Object aparent){
		setIdStream(aidStream);
		setPrice(aPrice);
		setSklad(aSklad);
		setParent(aparent);
	}
	public void setParent(Object aparent) {
		parent = aparent;
	}
	public static Object getParent() {
		return parent;
	}
	public void run(){
        byte[] buffer = new byte[1024];
        int len = -1;
        String outStr="";
        try
        {
            while ( ( len = in.read(buffer)) > -1 )
            {
                String str=new String(buffer,0,len);
            	outStr=outStr+str;
            	
//                System.out.print(str);
                if (outStr.endsWith("\r\n")){
                	outStr=outStr.replace("\r\n", "");
                	
                	(new Thread( new StreamIn(outStr))).start();
                	outStr="";
//                	Thread.currentThread().wait(100);
//                	wait(1000);
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }            

	}
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                in = serialPort.getInputStream();
//                OutputStream out = serialPort.getOutputStream();
//                Th1 = new Thread(new SerialReader(in)); 
//                Th1.start();
//                (new Thread(new SerialWriter(out))).start();

            }
        }     
    }
}
	class StreamIn implements Runnable{
		private String cod;
		public StreamIn(String acod){
			cod=acod;
		}
		public void run(){ 
			switch (ChooserStreamIn.getIdStream()){
			case 1:
				try {
					if (!(((NewSaleFrame)ChooserStreamIn.getParent()).formInput==null) && ((NewSaleFrame)ChooserStreamIn.getParent()).formInput.isVisible())
						((NewSaleFrame)ChooserStreamIn.getParent()).formInput.closeDialog();
					((NewSaleFrame)ChooserStreamIn.getParent()).Input(inputBarcode.newcod(cod, ChooserStreamIn.getSklad(), ChooserStreamIn.getPrice()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toolkit.getDefaultToolkit().beep();
					e.printStackTrace();
				}
				return;
			}
		}
	}
