import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;


public class DataSet {
	private static Connection cn=null;
	private static Statement st = null;
	private static ResultSet rs = null;
	private static int retstr=0;
	public static ResultSet QueryExec(String Query, boolean commited) throws Exception{
		if (cn==null)
			try {
				Locale.setDefault(Locale.ENGLISH);
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Properties prop = new Properties();
				prop.put("user", "system");
				prop.put("password", "masterkey");
				prop.put("NLS_NCHAR_CHARACTERSET","AL32UTF8");
//				prop.put("NLS_LANG","AMERICAN_AMERICA.CL8MSWIN1251");
				cn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.2:1521:XE", prop);
//				cn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.2:1521:XE", "system", "masterkey");
//				cn = DriverManager.getConnection("jdbc:oracle:thin:sklad/sklad@194.187.149.33");
				cn.setAutoCommit(false);
			}
			catch (Exception e) {
				e.printStackTrace();
				throw e;}
//		Statement st = null;
//		ResultSet rs = null;
		if (!(rs==null)){
			try {
				rs.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		if (st==null){

				try {
					st = cn.createStatement();
				} catch (SQLException e) {
					throw e;
				}
		}

		try { 
//			st = cn.createStatement();
			rs = st.executeQuery(Query);
			if (commited) 
				cn.commit();
		}
		catch (SQLException e) { 
			rs=null;
			throw e;}
		
		return rs;
	}
	public static int UpdateQuery(String aValue) throws SQLException{
		retstr=0;
		if (cn==null)
			try {
				Locale.setDefault(Locale.ENGLISH);
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "sklad", "sklad");
				cn.setAutoCommit(false);
			}
			catch (Exception e) { e.printStackTrace();}
//			Statement st = null;
//			ResultSet rs = null;
			if (st==null){

			try {
				st = cn.createStatement();
			} catch (SQLException e) {
				throw e;
				// TODO Auto-generated catch block
			}
		}

		try { 
//			st = cn.createStatement();
			retstr=st.executeUpdate(aValue);
			
		}
		catch (SQLException e) { throw e;}
		return retstr;
	}
	public static void commit() throws SQLException{
		if (!(cn==null)){
			try {
				cn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		}
	}
	public static void rollback() throws SQLException{
		if (!(cn==null)){
			try {
				cn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		}
		
	}
}

