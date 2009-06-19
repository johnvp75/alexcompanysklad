import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;


public class DataSet {
	private static Connection cn=null;
	private static Statement st = null;
	private static ResultSet rs = null;
	private static int retstr=0;
	public static ResultSet QueryExec(String Query){
		if (cn==null)
			try {
				Locale.setDefault(Locale.ENGLISH);
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "sklad", "sklad");
				cn.setAutoCommit(false);
			}
			catch (Exception e) { e.printStackTrace();}
//		Statement st = null;
//		ResultSet rs = null;
		if (!(rs==null)){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (!(st==null)){

				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		try { 
			st = cn.createStatement();
			rs = st.executeQuery(Query);
			cn.commit();
		}
		catch (Exception e) { e.printStackTrace();}
		
		return rs;
	}
	public static int UpdateQuery(String aValue){
		if (cn==null)
			try {
				Locale.setDefault(Locale.ENGLISH);
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn = DriverManager.getConnection("jdbc:oracle:thin:@91.210.177.35:1521:XE", "sklad", "sklad");
				cn.setAutoCommit(false);
			}
			catch (Exception e) { e.printStackTrace();}
		if (!(st==null)){

			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try { 
			st = cn.createStatement();
			retstr=st.executeUpdate(aValue);
			
		}
		catch (Exception e) { e.printStackTrace();}
		return retstr;
	}
	public static void commit(){
		if (!(cn==null)){
			try {
				cn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void rollback(){
		if (!(cn==null)){
			try {
				cn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}

