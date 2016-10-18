import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;


public class DataSet {
	private static Connection cn=null;
	private static Connection cn1=null;
	private static Statement st = null;
	private static Statement st1 = null;
	private static ResultSet rs = null;
	private static ResultSet rs1 = null;
	private static int retstr=0;
	private static int retstr1=0;
	public static ResultSet QueryExec(String Query, boolean commited) throws Exception{
		if (cn==null)
			try {
				Locale.setDefault(Locale.ENGLISH);
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.2:1521:XE", "sklad", "sklad");
//				cn = DriverManager.getConnection("jdbc:oracle:thin:@194.187.149.33:1521:XE", "sklad", "sklad");
//				cn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "sklad", "sklad");				
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
				cn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.2:1521:XE", "sklad", "sklad");
//				cn = DriverManager.getConnection("jdbc:oracle:thin:@194.187.149.33:1521:XE", "sklad", "sklad");
//				cn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "sklad", "sklad");
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
				throw e;
			}
		}
	}
	public static void rollback() throws SQLException{
		if (!(cn==null)){
			try {
				cn.rollback();
			} catch (SQLException e) {
				throw e;
			}
		}
		
	}
	
	
	
	public static ResultSet QueryExec1(String Query, boolean commited) throws Exception{
		if (cn1==null)
			try {
				Locale.setDefault(Locale.ENGLISH);
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn1 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.2:1521:XE", "sklad", "sklad");
//				cn1 = DriverManager.getConnection("jdbc:oracle:thin:@194.187.149.33:1521:XE", "sklad", "sklad");
//				cn1 = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "sklad", "sklad");				
				cn1.setAutoCommit(false);
			}
			catch (Exception e) {
				e.printStackTrace();
				throw e;}
//		Statement st1 = null;
//		ResultSet rs1 = null;
		if (!(rs1==null)){
			try {
				rs1.close();
			} catch (SQLException e) {
				throw e;
			}
		}
		if (st1==null){

				try {
					st1 = cn1.createStatement();
				} catch (SQLException e) {
					throw e;
				}
		}

		try { 
//			st = cn.createStatement();
			rs1 = st1.executeQuery(Query);
			if (commited) 
				cn1.commit();
		}
		catch (SQLException e) { 
			rs1=null;
			throw e;}
		
		return rs1;
	}
	public static int UpdateQuery1(String aValue) throws SQLException{
		retstr1=0;
		if (cn1==null)
			try {
				Locale.setDefault(Locale.ENGLISH);
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn1 = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.2:1521:XE", "sklad", "sklad");
//				cn1 = DriverManager.getConnection("jdbc:oracle:thin:@194.187.149.33:1521:XE", "sklad", "sklad");
//				cn1 = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "sklad", "sklad");
				cn1.setAutoCommit(false);
			}
			catch (Exception e) { e.printStackTrace();}
//			Statement st1 = null;
//			ResultSet rs1 = null;
			if (st1==null){

			try {
				st1 = cn1.createStatement();
			} catch (SQLException e) {
				throw e;
			}
		}

		try { 
//			st1 = cn1.createStatement();
			retstr1=st1.executeUpdate(aValue);
			
		}
		catch (SQLException e) { throw e;}
		return retstr1;
	}
	public static void commit1() throws SQLException{
		if (!(cn1==null)){
			try {
				cn1.commit();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	public static void rollback1() throws SQLException{
		if (!(cn1==null)){
			try {
				cn1.rollback();
			} catch (SQLException e) {
				throw e;
			}
		}
		
	}

}

