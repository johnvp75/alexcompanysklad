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
	public static ResultSet QueryExec(String Query){
		if (cn==null)
			try {
				Locale.setDefault(Locale.ENGLISH);
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn = DriverManager.getConnection("jdbc:oracle:thin:@91.210.177.35:1521:XE", "sklad", "sklad");
			}
			catch (Exception e) { e.printStackTrace();}
//		Statement st = null;
//		ResultSet rs = null;
		if (!(rs==null)){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
			rs = st.executeQuery(Query);
			
		}
		catch (Exception e) { e.printStackTrace();}
		return rs;
	}
}

