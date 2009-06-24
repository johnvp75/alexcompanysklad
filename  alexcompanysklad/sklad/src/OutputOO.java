import java.util.Vector;

import ooo.connector.BootstrapSocketConnector;

import com.sun.star.beans.PropertyValue;
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
import com.sun.star.text.*;


public class OutputOO {
	private static XComponentContext xRemouteContext = null;
	private static XMultiComponentFactory xRemouteServiceManager = null;
	private static XSpreadsheetDocument xSpreadsheetDocument;
	private static final String oooExeFolder="C:\\JavaProjects\\OpenOffice.org 3\\program\\";
	public OutputOO() {
		// TODO Auto-generated constructor stub
	}
	private static void connect(){
		if (xRemouteContext==null)
		try{
			xRemouteContext=BootstrapSocketConnector.bootstrap(oooExeFolder);
			System.out.println("соедин€емс€ с работающим Office...");
			xRemouteServiceManager=xRemouteContext.getServiceManager();
			}
		catch(Exception e){
			//при неудаче Ц как обычно
			e.printStackTrace();
			}
	}
	public static void OpenDoc(String DocPath) {
		try{
			connect();
			Object desktop=xRemouteServiceManager.createInstanceWithContext("com.sun.star.frame.Desktop", xRemouteContext);
			XComponentLoader xComponentLoader=(XComponentLoader)UnoRuntime.queryInterface(XComponentLoader.class, desktop);
			PropertyValue[] loadProps=new PropertyValue[1];
			loadProps[0] = new PropertyValue();
            loadProps[0].Name = "Hidden";
            loadProps[0].Value = new Boolean(false);
			XComponent xSpreadsheetComponent=xComponentLoader.loadComponentFromURL("file://localhost/C:/John/John/win/project/Sklad/scan/data/forms/"+DocPath, "_blank", 0, loadProps);
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
			com.sun.star.uno.Type elemType=xSpreadsheets.getElementType();
			System.out.println(elemType.getTypeName());
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
}
