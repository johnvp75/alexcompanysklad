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
        }
    }
}

