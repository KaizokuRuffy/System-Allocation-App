package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Admin;
import Controller.Util.Counter;
import Controller.Util.Json;
//import Controller.Util.LoginFilter;
import Controller.Util.Message;
import Service.AdminService;

@WebServlet(urlPatterns = {"/AdminController/*"})
public class AdminController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    private AdminService adminService = new AdminService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
		final String CRUD = request.getPathInfo();
		
		switch(CRUD)
		{
			case "/adminPresent" :
				
				System.out.println("\n-- Checking if there is admin is already present --");
				
				if(!adminService.isEmpty())
				{
					if(adminService.isAdminPresent())
					{
						response.setContentType("application/text");
						request.setAttribute("status", "yes");
						response.getWriter().write("Yes ");
						new Message().infoToClient(response);
					}
					else
					{
						response.setContentType("application/text");
						request.setAttribute("status", "no");
						response.getWriter().write("No - Admin");
						new Message().infoToClient(response);
					}
				}
				else
				{
					response.setContentType("application/text");
					request.setAttribute("status", "no");
					response.getWriter().write("No - DB empty");
					new Message().infoToClient(response);
				}
				Counter.getcounter().userNotPresent();
				
				break;
			
				
			case "/adminLogout" :	
				
				System.out.println("\n-- Admin logging out of the web site --");
				request.getSession().invalidate();
				new Message().infoToClient(response);
				
				Counter.getcounter().atLogout();
				
				break;
				
				
			case "/getAdmin":
				System.out.println("\n-- Fetching admin profile --");
				
				int admin_Id = Integer.parseInt(request.getParameter("admin_Id"));
				Admin admin = adminService.getUser(admin_Id);
				
				if(admin == null)
				{
					response.sendError(403, "Invalid username (or) database empty");
					System.out.println("Invalid username (or) database empty");
				}
				else
				{
					System.out.println("Admin fetched successfully --> " + admin);
					new Message().infoToClient(request, response, admin);
				}
				
				break;
				
			case "/create":
			
				System.out.println("\n-- Admin registration operation --");
				
				if(adminService.isEmpty())
				{
					if(AdminService.createDatabase()) // DB call
					{
						new Message().infoToClient(response);
						AdminService.setEmpty(false);
					}
					else
					{
						response.sendError(403, "Database problem");
						System.out.println("Database connection problem");
					}

					Counter.getcounter().userNotPresent();
				}
				else
				{
					System.out.println("Database already created");
					new Message().infoToClient(response);
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
			case "/adminLogin" :
				
				System.out.println("\n-- Authenticating admin --");
				
				int admin_id = Integer.parseInt(request.getParameter("admin_Id"));//var
				String Password = new String(request.getParameter("admin_Password"));//var
						
				Boolean auth = adminService.Authenticate(admin_id, Password); // DB call
				
				if(auth == null)
				{
					out.write("Invalid username");
					response.sendError(403, "Invalid username / password");
					System.out.println("Username doesn't exist");
				}
				else if(auth == true)
				{
					System.out.println("Logged in successfully");
					request.getSession().setAttribute("status", "logged in");
					
					Counter.getcounter().atLogin();
					
//					System.out.println(request.getSession().getId());
//					System.out.println(request.getSession().getAttribute("status"));
					new Message().infoToClient(response);
					
				}
				else if(auth == false)			
				{
					out.write("Invalid password");
					response.sendError(403, "Invalid username / password");
					System.out.println("Invalid password!!! Enter password again");
				}
				
				break;
				
			case "/addAdmin":
				
				System.out.println("\n -- Admin account creation operation --");
	
				Admin admin = new Json().toPojo(request, Admin.class);
		
//				System.out.println(admin);
				boolean before = adminService.isAdminPresent(); 
				
				if(admin != null)
				{
					if(adminService.createUser(admin)) // DB call
					{	
						System.out.println(admin.getAdmin_Name() + ", your account has been created succesfully");
						new Message().infoToClient(response);
					}
					else
					{
						response.sendError(500, "Database error");
						System.out.println("Database connection error");
						break;
					}
				}
				
				boolean after = adminService.isAdminPresent(); 
				
				if(before ^ after)
				{
					AdminService.setAdminPresent(true);
//					LoginFilter.getExclude().accept(LoginFilter.getUrls().get("Admin present"));
				}
					
				break;
				
		}
	}

}
