package DAO;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;

import Beans.Admin;
import Beans.Computer;
import Beans.Employee;
import Beans.Session;

public class Test 
{
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws SQLException, ParseException 
	{
		AdminDAO adminDAO = new AdminDAO();
		EmployeeDAO employeeDAO = new EmployeeDAO();
		ComputerDAO computerDAO = new ComputerDAO();
		SessionDAO sessionDAO = new SessionDAO();
		
	    //Admin operations
	  	
		//Creating Admin account
		AdminDAO.createTable();
		
		System.out.println("------- Admin Operations -------");
		
		adminDAO.insertInto(new Admin(1, "Admin1", 
				"admin1@zohocorp.com", "123214324", "4hifdskf"));
		adminDAO.insertInto(new Admin(2, "Admin2", 
				"admin2@zohocorp.com", "5423534534", "asdfsdg"));
		System.out.println();
		
		//Fetch all details of admin and password seperately
		adminDAO.selectRecord(1);
		adminDAO.getPassword(3);
		
		System.out.println();
		adminDAO.selectRecord(2);
		adminDAO.getPassword(2);
	
		
		System.out.println();
		System.out.println("------- Employee Operations -------");
		
		//Employee operations
		
		//Creating Employee account
		employeeDAO.insertInto(new Employee("Kishore", "404449399158", 
					"kishorekumarjain.d@zohotrainees.com", "9080971682", 
								"dfr32fwdf", "SDE","IT","Site 24x7"));
		employeeDAO.insertInto(new Employee("Kumar", "809438960546", 
				"kishorekumarjain23@gamil.com", "7598955385", 
							"asdas3qf", "SDE","IT","Site 24x7"));
		
		System.out.println();
		
		//Fetching all details of employee and password seperately
		employeeDAO.selectRecord(1);
		employeeDAO.getPassword(1);
		
		System.out.println();
		employeeDAO.selectRecord(2);
		employeeDAO.getPassword(2);
		
		System.out.println();
		
		//Fetching all details of all employee
		employeeDAO.selectAll();
		
		System.out.println();
		
		//Resource Operations
		System.out.println("------- Resource Operations -------");
		
		//Storing the computer data in database
		computerDAO.insertInto(new Computer("1", "10:65:30:33:58:cd", "sdfw4fsd", "Yes", "Yes", 
													"Site 24x7", "Latitude 7480", 2018));
		computerDAO.insertInto(new Computer("2", "23:55:78:45:15:re", "fg343c2dff", "Yes", "Yes", 
				"Site 24x7", "Latitude 2230", 2016));
		
		System.out.println();
		
		//Fetching all details of all computers
		computerDAO.selectAll();
		System.out.println();
		
		//Changing availability/working status of the computer
		computerDAO.updateStatus("1", ComputerDAO.ColumnName.Working.toString(), "No");
		computerDAO.updateStatus("2", ComputerDAO.ColumnName.Available.toString(), "No");
		System.out.println();
		 
		computerDAO.selectAll();
		System.out.println();
		
		System.out.println("------- Session Operations -------");
		
		//Session operations
		Session session = new Session(1, "1", Date.valueOf("2022-07-12"), "08:57");
		
		//Creating a session record in Session table
		sessionDAO.insertInto(session);
		
		//Fetching a single record
		sessionDAO.selectRecord(1, 1, Date.valueOf("2022-07-12"));
		
		//Updating session record after the user logs out of the app
		session.setLogOut_Date(Date.valueOf("2022-07-12"));
		session.setLogOut_Time("18:58");
		sessionDAO.updateRecord(session);
		System.out.println();
		
		session = new Session(1, "2", Date.valueOf("2022-07-13"), "08:50");
		sessionDAO.insertInto(session);
		sessionDAO.selectRecord(1, 2, Date.valueOf("2022-07-13"));
		session.setLogOut_Date(Date.valueOf("2022-07-13"));
		session.setLogOut_Time("18:29");
		sessionDAO.updateRecord(session);
		System.out.println();
		
		session = new Session(1, "3", Date.valueOf("2022-07-14"), "08:37");
		sessionDAO.insertInto(session);
		sessionDAO.selectRecord(1, 3, Date.valueOf("2022-07-14"));
		session.setLogOut_Date(Date.valueOf("2022-07-14"));
		session.setLogOut_Time("20:37");
		sessionDAO.updateRecord(session);
		System.out.println();
		sessionDAO.selectAll();
	}
}
