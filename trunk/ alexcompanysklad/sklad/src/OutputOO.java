import java.util.Vector;

import ooo.connector.BootstrapSocketConnector;

import com.sun.star.awt.FontWeight;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.DisposedException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;
import com.sun.star.view.XPrintable;
import com.sun.star.text.*;


public class OutputOO {
	private static XComponentContext xRemouteContext = null;
	private static XMultiComponentFactory xRemouteServiceManager = null;
	private static XSpreadsheetDocument xSpreadsheetDocument;
//	private static final String oooExeFolder="C:\\JavaProjects\\OpenOffice.org 3\\program"; //house
	private static final String oooExeFolder="C:\\Program Files\\OpenOffice.org 3\\program"; //server
	public OutputOO() {
		// TODO Auto-generated constructor stub
	}
	private static void connect() {
		if (xRemouteContext==null)
		try{
			xRemouteContext=BootstrapSocketConnector.bootstrap(oooExeFolder);
			xRemouteServiceManager=xRemouteContext.getServiceManager();
			}
		catch(Exception e){
			//при неудаче Ц как обычно
			e.printStackTrace();
			}
	}
	public static void OpenDoc(String DocPath, boolean hidden) {
		try{
			connect();
			Object desktop=xRemouteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xRemouteContext);
			XComponentLoader xComponentLoader=(XComponentLoader)UnoRuntime.queryInterface(XComponentLoader.class, desktop);
			PropertyValue[] loadProps=new PropertyValue[1];
			loadProps[0] = new PropertyValue();
            loadProps[0].Name = "Hidden";
            loadProps[0].Value = new Boolean(hidden);
			XComponent xSpreadsheetComponent=xComponentLoader.loadComponentFromURL("file://localhost/C:/sklad/Forms/"+DocPath, "_blank", 0, loadProps);
			xSpreadsheetDocument=(XSpreadsheetDocument)UnoRuntime.queryInterface(XSpreadsheetDocument.class, xSpreadsheetComponent);
		}
		catch(Exception e){
			e.printStackTrace();
			}
	}
	public static void CloseDoc () {
		XCloseable xCloseable =(XCloseable)UnoRuntime.queryInterface(XCloseable.class, xSpreadsheetDocument);
		try{
			xCloseable.close(false);
		}
		catch(Exception e){
			e.printStackTrace();
			}
	}
	public static void Insert(int X, int Y, Vector<Vector<String>> aValue){
		try{
			XSpreadsheets xSpreadsheets=xSpreadsheetDocument.getSheets();
			Object sheet=xSpreadsheets.getByName("Ћист1");
			XSpreadsheet xSpreadsheet=(XSpreadsheet)UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
			int width=aValue.get(0).size();
			int height=aValue.size();
			XCell xCell;
			XText xCellText; 
			for (int i=0; i<height; i++){
				for (int j=0; j<width; j++){
					xCell=xSpreadsheet.getCellByPosition(X+j-1,Y+i-1);
					xCellText = (XText)UnoRuntime.queryInterface(XText.class, xCell);
					xCellText.setString(aValue.get(i).get(j));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void InsertOne(String aValue, int aSize, boolean aBold, int X, int Y){
		try{
			XSpreadsheets xSpreadsheets=xSpreadsheetDocument.getSheets();
			Object sheet=xSpreadsheets.getByName("Ћист1");
			XSpreadsheet xSpreadsheet=(XSpreadsheet)UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
			XCell xCell;
			XText xCellText; 
			xCell=xSpreadsheet.getCellByPosition(X-1,Y-1);
			xCellText = (XText)UnoRuntime.queryInterface(XText.class, xCell);
			xCellText.setString(aValue);
			XPropertySet xCellProps=(XPropertySet)UnoRuntime.queryInterface(XPropertySet.class, xCell);
			if (aBold)
				xCellProps.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
			xCellProps.setPropertyValue("CharHeight", new Float(aSize));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public static void print(int copies){
		
		try{
			XPrintable xPrintable=(XPrintable)UnoRuntime.queryInterface(XPrintable.class, xSpreadsheetDocument);
			PropertyValue[] loadProps=new PropertyValue[1];
			loadProps[0] = new PropertyValue();
            loadProps[0].Name = "Wait";
            loadProps[0].Value = new Boolean(true);
            for (int i=0; i<copies;i++)
            	xPrintable.print(loadProps);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
