package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Employee;
import Controller.Util.Counter;
import Controller.Util.Json;
import Controller.Util.Message;
import Service.EmployeeService;

@WebServlet(urlPatterns = {"/EmployeeController/*"})
public class EmployeeController extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	private EmployeeService employeeService = new EmployeeService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		final String CRUD = request.getPathInfo();
		
		switch(CRUD)
		{		
			case "/getUser":
				System.out.println("\n-- Fetching User profile --");
				
				int emp_Id = Integer.parseInt(request.getParameter("emp_Id"));
				Employee emp = employeeService.getUser(emp_Id);
				
				if(emp == null)
				{
					response.sendError(403, "Invalid username (or) database empty");
					System.out.println("Invalid username (or) database empty");
				}
				else
				{
					System.out.println("\nUser profile fetched successfully --> " + emp);
					new Message().infoToClient(request, response, emp);
				}
				
				break;
				
			case "/getAllUsers" :
				
				System.out.println("\n-- Fetching all user data --");
				
				List<Employee> employeeList = employeeService.getAllUser();
				
				if(employeeList != null)
				{
					if(employeeList.size() > 0)
					{
						System.out.println("All user profiles fetched successfully");
						new Message().infoToClient(request, response, employeeList);
					}
					else
					{
						response.sendError(500, "No users in database. Create users and then try fetching data.");
						System.out.println("No users in database. Create users and then try fetching data.");
					}
				}
				else
				{
					System.out.println("Database is empty");
					response.sendError(500, "Database is empty");
				}
				
				break;
				
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
													throws ServletException, IOException {
	
		final String CRUD = request.getPathInfo();
		PrintWriter out = response.getWriter();
		
		switch(CRUD)
		{		
			case "/userLogin" :
				
				System.out.println("--- Authenticating user ---");
				
				int emp_Id = Integer.parseInt(request.getParameter("emp_Id"));//var
				String Password = new String(request.getParameter("emp_Password"));//var
						
				Boolean auth = employeeService.Authenticate(emp_Id, Password); // DB call
				
				if(auth == null)
				{
					out.write("Invalid username");
					response.sendError(403, "Invalid username / password");
					System.out.println("Username doesn't exist");
				}
				else if(auth == true)
				{
					System.out.println("Logged in successfully");
					System.out.println("After login");
					
					request.getSession(true).setAttribute("status", "employee logged in");
//					System.out.println(request.getSession().getId());
					Counter.getcounter().atLogin();
					
					// Calling 'SessionController' servlet to add session data to database as the user logs in
					RequestDispatcher rd = request.getRequestDispatcher("/SessionController/addSession");
					rd.forward(request, response);
				}
				else if(auth == false)			
				{
					out.write("Invalid password");
					response.sendError(403, "Invalid username / password");
					System.out.println("Invalid password!!! Enter password again");
				}
				
				break;
				
			case "/userLogout" :	
				
				System.out.println("\n-- User logging out of the web site --");
//				System.out.println(request.getSession().getId());
				request.getSession().invalidate();

				Counter.getcounter().atLogout();
				
				// Calling 'SessionController' servlet to update session data to database as the user logs out
				RequestDispatcher rd = request.getRequestDispatcher("/SessionController/updateSession");
				rd.forward(request, response);
				
				break;
			
			case "/addUser":
				
				System.out.println("--- User account creation operation ---");
	
				Employee emp = new Json().toPojo(request, Employee.class);
				
//				System.out.println(adminJson);
//				System.out.println(admin);
				
				if(emp != null)
				{
					if(employeeService.createUser(emp)) // DB call
					{	
						System.out.println(emp.getEmp_Name() + ", your account has been created succesfully");
						new Message().infoToClient(response);
					}
					else
					{
						System.out.println("Duplicate user not allowed");
						response.sendError(500, "Duplicate user not allowed");
					}
				}
				
				break;
				
		}
	}
}
