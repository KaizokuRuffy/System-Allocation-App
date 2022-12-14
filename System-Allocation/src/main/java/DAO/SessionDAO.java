package DAO;	

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Beans.Session;
import DAO.Util.JDBC_Connection;
import DAO.Util.TableName;

public class SessionDAO 
{
	static enum ColumnName 
	{
		Emp_ID("`Employee ID`"), Comp_ID("`Computer ID`"), LogIn_Date("`LogIn Date`"), 
					LogOut_Date("`LogOut Date`"),LogIn_Time("`LogIn Time`"), 
					LogOut_Time("`LogOut Time`"), Total_Time("`Total Time`"), Shift;
		
		String value;
		
		ColumnName(String value) {
			this.value = value;
		}
	
		ColumnName() {
		}  
	}
	
	private static String createTable = "CREATE TABLE IF NOT EXISTS " + TableName.getSession()
		+ " ( " + ColumnName.Emp_ID.value + " INT NOT NULL, "
				+ ColumnName.Comp_ID.value + " INT NOT NULL, "
				+ ColumnName.LogIn_Date.value + " DATE NOT NULL, "
				+ ColumnName.LogOut_Date.value + " DATE, "
				+ ColumnName.LogIn_Time.value + " varchar(255), "
				+ ColumnName.LogOut_Time.value + " varchar(255), "
				+ ColumnName.Total_Time.value + " varchar(255), "
				+ ColumnName.Shift + " varchar(255), "
		 		+ " PRIMARY KEY (" + ColumnName.Emp_ID.value + "," + ColumnName.Comp_ID.value+ 
		 									"," + ColumnName.LogIn_Date.value+ "," + ColumnName.LogIn_Time.value + ") )" ;
	
	public static int noOfRows()
	{
		String query = "select count(*)"
					 + "from " + TableName.getSession();
		
		try (Connection conn = JDBC_Connection.getConnection();
				 Statement st = conn.createStatement();
				 ResultSet rs = st.executeQuery(query))
			{
				int count = 0;
				
				if(rs.next())
				{
					count = Integer.parseInt(rs.getString(0));
					return count;
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		
		return 0;
	}
	
	public int insertInto(Session session) {
//		System.out.println("Current session data stored in database");
		String query = "INSERT INTO " + TableName.getSession() + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try(Connection conn = JDBC_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement(query))
		{

			ps.setInt(1, session.getEmp_Id());
			ps.setInt(2, session.getComp_Id());
			ps.setObject(3, session.getLogIn_Date());
			ps.setDate(4, null);
			ps.setString(5, session.getLogIn_Time());
			ps.setString(6, null);
			ps.setString(7, null);
			ps.setString(8, session.getShift());

			int rs = ps.executeUpdate();
			
			return rs;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public List<Session> selectAll() {
		
		System.out.println("Fetching all(Session) details");
		
		String query = 
				   "SELECT " + TableName.getSession() + ".*, "
						 + TableName.getEmployee() + "." + EmployeeDAO.ColumnName.Name.toString()
				 + " FROM " + TableName.getSession() + " , " + TableName.getEmployee()
			     + " WHERE " + TableName.getSession() + "." + ColumnName.Emp_ID.value 
			     + " = " + TableName.getEmployee() + "." + EmployeeDAO.ColumnName.ID.toString();

		
		try(Connection conn = JDBC_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery())
		{		
			List<Session> sessionList = new ArrayList<>();
			
			while(rs.next())
			{
				Session session = new Session();
				session.setEmp_Id(rs.getInt(ColumnName.Emp_ID.value.replaceAll("`", "")));
				session.setEmp_Name(rs.getString(EmployeeDAO.ColumnName.Name.toString()));
				session.setComp_Id(rs.getInt(ColumnName.Comp_ID.value.replaceAll("`", "")));
				session.setLogIn_Date(rs.getDate(ColumnName.LogIn_Date.value.replaceAll("`", "")));
				session.setLogOut_Date(rs.getDate(ColumnName.LogOut_Date.value.replaceAll("`", "")));
				session.setLogIn_Time(rs.getString(ColumnName.LogIn_Time.value.replaceAll("`", "")));
				session.setLogOut_Time(rs.getString(ColumnName.LogOut_Time.value.replaceAll("`", "")));
				session.setTotal_Time(rs.getString(ColumnName.Total_Time.value.replaceAll("`", "")));
				session.setShift(rs.getString(ColumnName.Shift.toString()));
				
				sessionList.add(session);
//				System.out.println(session);
			}
			
			return sessionList;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int updateRecord(Session session) {
		
		String query = "UPDATE " + TableName.getSession()
				 + "SET " + ColumnName.LogOut_Date.value + " = ? , " 
				  + ColumnName.LogOut_Time.value + " = ?, " + ColumnName.Total_Time.value + " = ? "
				 + " WHERE " + ColumnName.Emp_ID.value + " = ? "
		    	 + " AND " + ColumnName.Comp_ID.value + " = ? "
			     + " AND " + ColumnName.LogIn_Date.value + " = ? "
			     + " AND " + ColumnName.LogIn_Time.value + " = ? ";

		try(Connection conn = JDBC_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);)
		{

//			ps.setString(1, TableName.getSession());
//			ps.setString(2, ColumnName.LogOut_Date.value);
			ps.setObject(1, session.getLogOut_Date());
//			ps.setString(4, ColumnName.LogOut_Time.value);
			ps.setString(2, session.getLogOut_Time());
//			ps.setString(6, ColumnName.Total_Time.value);
			ps.setString(3, session.getTotal_Time());
//			ps.setString(8, ColumnName.Emp_ID.value);
			ps.setInt(4, session.getEmp_Id());
//			ps.setString(10, ColumnName.Comp_ID.value);
			ps.setInt(5, session.getComp_Id());
//			ps.setString(12, ColumnName.LogIn_Date.value);
			ps.setObject(6, session.getLogIn_Date());
			ps.setObject(7, session.getLogIn_Time());
			
			int rs = ps.executeUpdate();
			
//			if(rs == 1)
//				System.out.println("Current session data is updated successfully");
			System.out.println(rs);
			return rs;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return 0;
	}
	
	@Deprecated
	public void selectRecord(int emp_Id, int comp_Id, Date logIn_Date) throws SQLException {
		
		System.out.println("Fetching " + comp_Id + " " + emp_Id + " " 
														+ logIn_Date +" (Session) details : ");
		
		String query = 
				   "SELECT " + TableName.getSession() + ".*, "
						 + TableName.getEmployee() + "." + EmployeeDAO.ColumnName.Name.toString()
				 + " FROM " + TableName.getSession() + " , " + TableName.getEmployee()
			     + " WHERE " + TableName.getSession() + "." + ColumnName.Emp_ID.value 
			     + " = " + TableName.getEmployee() + "." + EmployeeDAO.ColumnName.ID.toString()
			     + " AND " + ColumnName.Emp_ID.value + " = " + emp_Id 
			     + " AND " + ColumnName.Comp_ID.value + " = " + comp_Id
			     + " AND " + ColumnName.LogIn_Date.value + " = '" + logIn_Date + "'";
		
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Session session = new Session();
		try 
		{
			conn = JDBC_Connection.getConnection();
			ps = conn.prepareStatement(query);
			
//			ps.setString(1, TableName.getSession());
//			ps.setString(2, TableName.getEmployee());
//			ps.setString(3, EmployeeDAO.ColumnName.Name.toString());
//			ps.setString(4, TableName.getSession());
//			ps.setString(5, TableName.getEmployee());
//			ps.setString(6, TableName.getSession());
//			ps.setString(7, ColumnName.Emp_ID.value);
//			ps.setString(8, TableName.getEmployee());
//			ps.setString(9, EmployeeDAO.ColumnName.ID.toString());
			
			rs = ps.executeQuery();
			
			if(rs.next())
			{
				session.setEmp_Id(rs.getInt(ColumnName.Emp_ID.value.replaceAll("`", "")));
				session.setEmp_Name(rs.getString(EmployeeDAO.ColumnName.Name.toString()));
				session.setComp_Id(rs.getInt(ColumnName.Comp_ID.value.replaceAll("`", "")));
				session.setLogIn_Date(rs.getDate(ColumnName.LogIn_Date.value.replaceAll("`", "")));
				session.setLogOut_Date(rs.getDate(ColumnName.LogOut_Date.value.replaceAll("`", "")));
				session.setLogIn_Time(rs.getString(ColumnName.LogIn_Time.value.replaceAll("`", "")));
				session.setLogOut_Time(rs.getString(ColumnName.LogOut_Time.value.replaceAll("`", "")));
				session.setTotal_Time(rs.getString(ColumnName.Total_Time.value.replaceAll("`", "")));
				session.setShift(rs.getString(ColumnName.Shift.toString()));
				
//				System.out.println(session);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			rs.close();
			ps.close();
			conn.close();
		}
	}

	public static String getCreateTable() {
		return createTable;
	}

	public static void setCreateTable(String createTable) {
		SessionDAO.createTable = createTable;
	}
}
