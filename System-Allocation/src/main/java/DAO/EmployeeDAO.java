package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Beans.Employee;
import DAO.Util.JDBC_Connection;
import DAO.Util.TableName;


public class EmployeeDAO 
{
	static enum ColumnName 
	{
		ID, Name,Adhaar_ID("`Adhaar ID`"), Email_ID("`Email ID`"), Mobile_No("`Mobile No`"), 
						Password, Role, Department, Work_Location("`Work Location`");
		
		String value;
		
		ColumnName(String value) {
			this.value = value;
		}
	
		ColumnName() {
		}  
	}
	
	private static String createTable = "CREATE TABLE IF NOT EXISTS " + TableName.getEmployee() +
			" ( " + ColumnName.ID + " INT NOT NULL AUTO_INCREMENT, "
			  	  + ColumnName.Name + " varchar(255), "
			  	  + ColumnName.Adhaar_ID.value + " varchar(255), "
			  	  + ColumnName.Email_ID.value + " varchar(255), "
			  	  + ColumnName.Mobile_No.value + " varchar(255), "
			  	  + ColumnName.Password + " varchar(255), "
			  	  + ColumnName.Role + " varchar(255), "
			  	  + ColumnName.Department + " varchar(255), "
			  	  + ColumnName.Work_Location.value + " varchar(255), "
			    + " PRIMARY KEY (" + ColumnName.ID + "))" ;
	
	public int noOfRows()
	{
		String query = "select count(*)"
					 + "from " + TableName.getEmployee();
		
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
	
	public String getPassword(int emp_Id) {
		
//		System.out.println("Getting password of Employee " + emp_Id);
		
		String query = " SELECT " + ColumnName.Password
				 + " FROM " + TableName.getEmployee()
				 + " WHERE " + ColumnName.ID + " = " + emp_Id;
		
		try(Connection conn = JDBC_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery()) 
		{
			if(rs.next())
			{
				String emp_Password = rs.getString(ColumnName.Password.toString());
//				System.out.println(ColumnName.Password + " " + emp_Password);
				
				return emp_Password ;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return null;
	}
	
	public int insertInto(Employee emp) {
		
//		System.out.println("Creating Employee account");
		
		String query = " INSERT INTO " + TableName.getEmployee() + 
					   " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try(Connection conn = JDBC_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement(query))
		{

			if(emp.getEmp_Id() != 0)
				ps.setInt(1, emp.getEmp_Id());
			else
				ps.setString(1, null);
			
			ps.setString(2, emp.getEmp_Name());
			ps.setString(3, emp.getAdhaarId());
			ps.setString(4, emp.getEmp_Email());
			ps.setString(5, emp.getEmp_MobileNo());
			ps.setString(6, emp.getEmp_Password());
			ps.setString(7, emp.getEmp_Role());
			ps.setString(8, emp.getEmp_Dept());
			ps.setString(9, emp.getEmp_WorkLoc());
			
			int rs = ps.executeUpdate();
		
//			if(rs == 1)
//				System.out.println(emp.getEmp_Name() + ", your account "
//						+ "has been created successfully !!!");
			
			return rs;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return 0;
	}
	
	public Employee selectRecord(int emp_Id) {
		
//		System.out.println("Fetching User(Employee) details : ");
		
		String query = " SELECT * "
					 + " FROM " + TableName.getEmployee()
					 + " WHERE " + ColumnName.ID + " = " +emp_Id ;

		try(Connection conn = JDBC_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery())
		{

			if(rs.next())
			{
				Employee emp = new Employee();
				emp.setEmp_Id(rs.getInt(ColumnName.ID.toString()));;
				emp.setEmp_Name(rs.getString(ColumnName.Name.toString()));;
				emp.setAdhaarId(rs.getString(ColumnName.Adhaar_ID.value.replaceAll("`", "")));
				emp.setEmp_Email(rs.getString(ColumnName.Email_ID.value.replaceAll("`", "")));;
				emp.setEmp_MobileNo(rs.getString
										(ColumnName.Mobile_No.value.replaceAll("`", "")));;
				emp.setEmp_Password(rs.getString(ColumnName.Password.toString()));;
				emp.setEmp_Role(rs.getString(ColumnName.Role.toString()));;
				emp.setEmp_Dept(rs.getString(ColumnName.Department.toString()));;
				emp.setEmp_WorkLoc(rs.getString
										(ColumnName.Work_Location.value.replaceAll("`", "")));;
										
//				System.out.println(emp);
										
				return emp;
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Employee> selectAll() {
		
//		System.out.println("Fetching All(Employee) details ");
		
		String query = " SELECT * "
				 + " FROM " + TableName.getEmployee() ;

		try(Connection conn = JDBC_Connection.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery())
		{
			List<Employee> employeeList = new ArrayList<>();
			
			while(rs.next())
			{
				Employee emp = new Employee();
				emp.setEmp_Id(rs.getInt(ColumnName.ID.toString()));;
				emp.setEmp_Name(rs.getString(ColumnName.Name.toString()));;
				emp.setAdhaarId(rs.getString(ColumnName.Adhaar_ID.value.replaceAll("`", "")));
				emp.setEmp_Email(rs.getString(ColumnName.Email_ID.value.replaceAll("`", "")));;
				emp.setEmp_MobileNo(rs.getString
										(ColumnName.Mobile_No.value.replaceAll("`", "")));;
				emp.setEmp_Password(rs.getString(ColumnName.Password.toString()));;
				emp.setEmp_Role(rs.getString(ColumnName.Role.toString()));;
				emp.setEmp_Dept(rs.getString(ColumnName.Department.toString()));;
				emp.setEmp_WorkLoc(rs.getString
										(ColumnName.Work_Location.value.replaceAll("`", "")));;
				
				employeeList.add(emp);
			}
			
//			System.out.println(employeeList);
			return employeeList;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return null;
	}
	
	@Deprecated
	public void updateRecord() {
		String query = "UPDATE ?"
				 + "SET ? = ? "
				 + "WHERE ? = ? ";
	}
	
	@Deprecated
	public void deleteRecord(String emp_Id) {
		String query = "DELETE FROM ?"
				 + "WHERE ? = ? ";
	}
	

	public static String getCreateTable() {
		return createTable;
	}
	
	public static void setCreateTable(String createTable) {
		EmployeeDAO.createTable = createTable;
	}
	
	
}
