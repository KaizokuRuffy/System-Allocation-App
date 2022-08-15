package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Beans.Employee;
import Beans.Session;
import Controller.Util.Counter;
import Controller.Util.Json;
import Controller.Util.Message;
import Controller.Util.SchedulerTask;
import Service.EmployeeService;
import Service.SessionService;

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
					if(request.getSession(false).getAttribute("status").equals("admin logged in"))
					{
						System.out.println("\nUser profile fetched successfully - from admin");
						List<Session> sessionList = new SessionService().getEmpSession(emp_Id);
						List<Object> list = new ArrayList<>();
						list.add(emp);
						list.add(sessionList);
						
//						String s1 = new Gson().toJson(list);
//						s1 = s1.substring(1, s1.length() - 1);
//						s1 = "{" + s1 + "}";
						new Message().infoToClient(request, response, list);
					}
					else if(request.getSession(false).getAttribute("status").equals("employee logged in"))
					{
						System.out.println("\nUser profile fetched successfully - from user");
						new Message().infoToClient(request, response, emp);
					}
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
				
				System.out.println("\n--- Authenticating user ---");
				
				int emp_Id = 0;
				String Password = null;
				Boolean auth = null;
				
				try {
					emp_Id = Integer.parseInt(request.getParameter("emp_Id"));//var
					Password = new String(request.getParameter("emp_Password"));//var
					auth = employeeService.Authenticate(emp_Id, Password); // DB call
					
				} 
				catch (NumberFormatException e) {
					out.write("Invalid username");
					response.sendError(403, "Invalid username / password");
					System.out.println("Username doesn't exist");
					break;
				}
				
				if(auth == true)
				{
					System.out.println("Logged in successfully");
					
					final int id = emp_Id;
					
					HttpSession sess = request.getSession(true);
					sess.setAttribute("status", "employee logged in");
//					System.out.println(request.getSession().getId());
					
					// Calling 'SessionController' servlet to add session data to database as the user logs in
					RequestDispatcher rd = request.getRequestDispatcher("/SessionController/addSession");
					rd.forward(request, response);
					
					SchedulerTask st = new SchedulerTask();
					st.schedule(new TimerTask() {
						@Override
						public void run() {
							boolean flag = true;
							try {
//								System.out.println(emp_Id + " In try block");
//								System.out.println(emp_Id + " " + sess.getAttribute("status"));
								sess.getAttribute("status");
							}
							catch(IllegalStateException e)
							{
//								System.out.println(emp_Id + " In catch block");
								flag = false;
							}
							if(flag)
							{
								System.err.println("Session of user '" + id + "' invalidated");
								sess.invalidate();
								Counter.getcounter().atLogout();
							}
							
						}
					}, 1000*60*60*24L); //*60*60*24L);
					
					Counter.getcounter().atLogin();
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
//				request.getSession().invalidate();

				
				// Calling 'SessionController' servlet to update session data to database as the user logs out
				RequestDispatcher rd = request.getRequestDispatcher("/SessionController/updateSession");
				rd.forward(request, response);
				
				request.getSession(false).invalidate();
				Counter.getcounter().atLogout();
				System.out.println("User logged out successfully");
				
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
