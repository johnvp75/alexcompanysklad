import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class DataSet {
	private static Connection cn=null;
	public static ResultSet QueryExec(String Query){
		if (cn==null)
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "sklad", "sklad");
			}
			catch (Exception e) { e.printStackTrace();}
		Statement st = null;
		ResultSet rs = null;
		try { 
			st = cn.createStatement();
			rs = st.executeQuery(Query);
			
		}
		catch (Exception e) { e.printStackTrace();}
		return rs;
	}
}

