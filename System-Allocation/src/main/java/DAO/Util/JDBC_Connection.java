package DAO.Util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_Connection 
{
	private static Connection conn = null;
	
	public static Connection getConnection()
	{
		try {
			if(conn == null ||  conn.isClosed())
			{
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://localhost:3306/" + 
									TableName.getDatabase() + "?createDatabaseIfNotExist=true";
				String username = "Admin";
				String password = "PCrEq9KaMbAcut";
				
				try 
				{
					Class.forName(driver);
					conn = DriverManager.getConnection(url,username,password);
					
					System.out.println("Connected Successfully");
				} 
				catch (Exception e) 
				{
					System.out.println(e);
				}
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return conn;
	}
}