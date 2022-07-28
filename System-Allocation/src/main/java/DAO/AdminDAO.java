package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Beans.Admin;
import DAO.Util.JDBC_Connection;
import DAO.Util.TableName;

public class AdminDAO 
{	
	enum ColumnName
	{
		ID, Name, Email_ID("`Email ID`"), Contact_No("`Contact No`"), Password;
		
		String value;
		
		ColumnName(String value) {
			this.value = value;
		}

		ColumnName() {
		}  
	}
		
	private static String createTable = "CREATE TABLE IF NOT EXISTS " + TableName.getAdmin() +
			 " ( " + ColumnName.ID + " INT NOT NULL AUTO_INCREMENT, "
			 +  ColumnName.Name + " varchar(255), "
			 +  ColumnName.Email_ID.value + " varchar(255), "
			 +  ColumnName.Contact_No.value + " varchar(255), "
			 +  ColumnName.Password + " varchar(255),"
			+ " PRIMARY KEY (" + ColumnName.ID  +") )" ;
	
	public static int[] createTable()  
	{

		try (Connection conn = JDBC_Connection.getConnection();
			 Statement st = conn.createStatement())
		{
			int[] result = null;
			st.addBatch(AdminDAO.getCreateTable());
			st.addBatch(EmployeeDAO.getCreateTable());
			st.addBatch(ComputerDAO.getCreateTable());
			st.addBatch(SessionDAO.getCreateTable());
			
			result = st.executeBatch();
			
//			if(result != null)
//			{
//				System.out.println("Tables successfully created " + Arrays.toString(result));
//			}
			
			return result;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return null;
	}
	
	public static boolean isDatabaseEmpty()
	{
		String show = "SELECT count(*) "
				+ " FROM INFORMATION_SCHEMA.TABLES "
				+ " WHERE TABLE_SCHEMA = '" + TableName.getDatabase() +"'"  ;
//		System.out.println(show);
		try (Connection conn = JDBC_Connection.getConnection();
			 Statement st = conn.createStatement();
			 ResultSet rs = st.executeQuery(show))
		{
			
			int count = 0;
			
			if(rs.next())
				count = Integer.parseInt(rs.getString(1));

			if(count > 0)
				return false;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static int noOfRows()
	{
		String query = "select count(*)"
					 + "from " + TableName.getAdmin();
		
		try (Connection conn = JDBC_Connection.getConnection();
				 Statement st = conn.createStatement();
				 ResultSet rs = st.executeQuery(query))
			{
				int count = 0;
				
				if(rs.next())
				{
					count = Integer.parseInt(rs.getString(1));
					return count;
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		
		return 0;
	}
	
	public String getPassword(int admin_ID) {
		
//		System.out.println("Getting password of Admin " + admin_ID);
		
		String query = "SELECT " + ColumnName.Password
					 + " FROM " + TableName.getAdmin()
					 + " WHERE " + ColumnName.ID + " = " + admin_ID;

		try  (Connection conn = JDBC_Connection.getConnection();
			  PreparedStatement ps = conn.prepareStatement(query);
		      ResultSet rs = ps.executeQuery())
		{
			if(rs.next())
			{
				String admin_Password = rs.getString(ColumnName.Password.toString());
//				System.out.println(ColumnName.Password + " " +admin_Password);
				
				return admin_Password;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public int insertInto(Admin admin) {
		
		System.out.println("Creating Admin account");
		String query = "INSERT INTO " + TableName.getAdmin() + 
											" VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = JDBC_Connection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(query))
		{

			if(admin.getAdmin_Id() != 0)
				ps.setInt(1, admin.getAdmin_Id());
			else
				ps.setString(1, null);
			
			ps.setString(2, admin.getAdmin_Name());
			ps.setString(3, admin.getAdmin_Email());
			ps.setString(4, admin.getAdmin_ContactNo());
			ps.setString(5, admin.getAdmin_Password());

			int rs = ps.executeUpdate();
			
//			System.out.println("Insert into admin ");
//			System.out.println(rs + " insetInto(Admin admin)");
//			if(rs == 1)
//				System.out.println(admin.getAdmin_Name() + ", your account "
//									+ "has been created successfully !!!");
			
			return rs;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public Admin selectRecord(int admin_ID) {
		
//		System.out.println("Fetching User(Admin) details : ");
		
		String query = "SELECT * "
					 + " FROM " + TableName.getAdmin()
					 + " WHERE " + ColumnName.ID + " = " + admin_ID;
		
		try(Connection conn = JDBC_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery())
		{
			if(rs.next())
			{
				Admin admin = new Admin();
				admin.setAdmin_Id(rs.getInt(ColumnName.ID.toString()));
				admin.setAdmin_Name(rs.getString(ColumnName.Name.toString()));
				admin.setAdmin_Email(rs.getString(ColumnName.Email_ID.value.replaceAll("`", "")));
				admin.setAdmin_ContactNo(rs.getString(ColumnName.Contact_No.value.replaceAll("`", "")));
				admin.setAdmin_Password(rs.getString(ColumnName.Password.toString()));
//				System.out.println(admin);
				
				return admin;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	
		return null;
	}
	@Deprecated
	public void selectAll() {
		
	}
	
	@Deprecated
	public void updateRecord() {
		String query = "UPDATE ? "
				 + "SET ? = ? "
				 + "WHERE ? = ? ";
	}
	
	@Deprecated
	public static boolean createDatabase() throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try 
		{
			conn = JDBC_Connection.getConnection();
			ps = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS " + TableName.getDatabase());
			
			if(ps.executeUpdate() == 1)
				System.out.printf("Database - %s Created!!!", TableName.getDatabase());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			ps.close();
			conn.close();
			System.out.println("\nResource closed...");
		}
		return true;
	}

	
	public static String getCreateTable() {
		return createTable;
	}

	public static void setCreateTable(String createTable) {
		AdminDAO.createTable = createTable;
	}
	
}
