import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;


public class ChooserStreamIn implements Runnable{
	private volatile int idStream;
	private volatile String Sklad;
	private volatile String Price;
	private Object parent;
//	private SerialPortReader port=null;
	private CommPort commPort;
	public ChooserStreamIn(){
		
	}
	public void setIdStream(int idStream) {
		idStream = idStream;
	}
	public int getIdStream() {
		return idStream;
	}
	public void setSklad(String sklad) {
		Sklad = sklad;
	}
	public String getSklad() {
		return Sklad;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getPrice() {
		return Price;
	}
	public void StreamIn(String cod){
		switch (MainWindow.Scaner.getIdStream()){
		case 1:
			try {
					if (!(((NewSaleFrame)MainWindow.Scaner.parent).formInput==null) && ((NewSaleFrame)MainWindow.Scaner.parent).formInput.isVisible())
						((NewSaleFrame)MainWindow.Scaner.parent).formInput.closeDialog();
					((NewSaleFrame)MainWindow.Scaner.parent).Input(inputBarcode.newcod(cod, MainWindow.Scaner.getSklad(), MainWindow.Scaner.getPrice()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toolkit.getDefaultToolkit().beep();
					e.printStackTrace();
				}
			return;
		}
	}
	public void init(int aidStream, String aSklad, String aPrice, Object aparent){
		setIdStream(aidStream);
		setPrice(aPrice);
		setSklad(aSklad);
		setParent(aparent);
	}
	public void setParent(Object parent) {
		parent = parent;
	}
	public Object getParent() {
		return parent;
	}
	public void run(){
		try {
			connect("COM1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
                
                InputStream in = serialPort.getInputStream();
//                OutputStream out = serialPort.getOutputStream();
//                Th1 = new Thread(new SerialReader(in)); 
//                Th1.start();
//                (new Thread(new SerialWriter(out))).start();
                byte[] buffer = new byte[1024];
                int len = -1;
                String outStr="";
                try
                {
                    while ( ( len = in.read(buffer)) > -1 )
                    {
                        String str=new String(buffer,0,len);
                    	outStr=outStr+str;
                    	
//                        System.out.print(str);
                        if (outStr.endsWith("\r\n")){
                        	outStr=outStr.replace("\r\n", "");
                        	
                        	StreamIn(outStr);
                        	outStr="";
//                        	Thread.currentThread().wait(100);
//                        	wait(1000);
                        }
                    }
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }            

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
}
