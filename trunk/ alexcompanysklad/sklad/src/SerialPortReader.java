import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;


import java.io.IOException;
import java.io.InputStream;


public class SerialPortReader
{
    private Thread Th1;
    private CommPort commPort;
	public SerialPortReader()
    {
        super();
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
                Th1 = new Thread(new SerialReader(in)); 
                Th1.start();
//                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    @SuppressWarnings("deprecation")
	public void disconnect(){
    	if (!(Th1==null)){
    		commPort.close();
    		Th1.stop();
    	}
    }
    /** */
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            String outStr="";
            try
            {
                while (true)
                	if ( ( len = this.in.read(buffer)) > -1 )
                {
                    String str=new String(buffer,0,len);
                	outStr=outStr+str;
                	
//                    System.out.print(str);
                    if (outStr.endsWith("\r\n")){
                    	outStr=outStr.replace("\r\n", "");
                    	
                    	ChooserStreamIn.StreamIn(outStr);
                    	outStr="";
                    }
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
}

