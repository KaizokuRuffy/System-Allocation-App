package Controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Computer;
import Controller.Util.DATE;
import Controller.Util.Json;
import Controller.Util.Message;
import Controller.Util.SchedulerTask;
import Service.ComputerService;

@WebServlet(urlPatterns = {"/SystemController/*"})
public class ComputerController extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ComputerService computerService = new ComputerService();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
					throws ServletException, IOException {

		final String CRUD = request.getPathInfo();
		
		switch(CRUD)
		{
			case "/getAllSystems":
				
				System.out.println("\n-- Fetching all system data --");
				List<Computer> computerList = computerService.getAllSystems();
				
				if(computerList != null)
				{
					if(computerList.size() > 0)
					{
						System.out.println("All system details fetched successfully");
						new Message().infoToClient(response, computerList);
					}
					else
					{
						new Message().infoToClient(HttpServletResponse.SC_NOT_FOUND, 
								response, "No resources data in database. Add system details and then try fetching data.");	
//						response.sendError(500, "No resources data in database. Add system details and then try fetching data.");
						System.out.println("No resource data present in table");
					}
				}
				else
				{
					new Message().infoToClient(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
										response, "Database error");
					System.out.println("Database error");
					//response.sendError(500, "Database error");
				}
				
				break;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		final String CRUD = request.getPathInfo();
		
		switch(CRUD)
		{
			case "/updateStatus":
				
				doPut(request, response);
				
				break;
				
			case "/addSystem":
				
				System.out.println("\n-- Saving resource details to database --");
				Computer comp = new Json().toPojo(request, Computer.class);
				
				if(comp != null)
				{
					if(computerService.addSystem(comp))
					{
						new Message().infoToClient(HttpServletResponse.SC_CREATED, 
																response, "Resource details saved."); ;
						System.out.println("Resource details saved ");
					}
					else
					{
						new Message().infoToClient(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
										response, "Duplicate resource cannot be added. MAC address should be unique");
						System.out.println("Duplicate resource cannot be added.");
//						response.sendError(500, "Database error");
					}
				}
				
				break;
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		final String CRUD = request.getPathInfo();
		
		switch (CRUD)
		{
			case "/updateStatus":
				
				int comp_Id = 0;
				String colName = null;
				String status = null;
				boolean flag = false;
				
				try
				{
					comp_Id = Integer.parseInt(request.getParameter("comp_Id"));
					colName = request.getParameter("colName");
					status = request.getParameter("status");
				}
				catch(NumberFormatException e)
				{
					flag = true;
					comp_Id = (Integer)request.getAttribute("comp_Id");
					colName = (String) request.getAttribute("colName");
					status = (String) request.getAttribute("status");
				}
				
				System.out.println("\n-- Updating " + colName + " status of system --");
				
				if(flag)
				{
//					System.out.println(comp_Id + " " + colName + " " + status);
					if(computerService.updateStatus(comp_Id, colName, status))
					{

						//new Message().infoToClient("Status updated successfully",response);
						response.getWriter().append("Status updated successfully");
						
						System.out.println("Status of system '" + comp_Id + 
													"' is updated succesfully");
				
						final int id = comp_Id;
						final String cname = colName;
						
						Date date = null;
//						System.out.println(new Date());
//						System.out.println(DATE.getDate(8, 0, 0));
//						System.out.println(DATE.getDate(16, 0, 0));
//						System.out.println(DATE.getDate(23, 59, 59));
						
						if(DATE.getDate(8, 0, 0).toString().compareTo(new Date().toString()) >= 0)
							date = DATE.getDate(8, 0, 0);
						else if(DATE.getDate(16, 0, 0).toString().compareTo(new Date().toString()) >= 0)
							date = DATE.getDate(16, 0, 0);
						else if(DATE.getDate(23, 59, 59).toString().compareTo(new Date().toString()) >= 0)
							date = DATE.getDate(23, 59, 59);
						
						
						//System.out.println(date);
						
						SchedulerTask st = new SchedulerTask();
						st.schedule(new TimerTask() {
							@Override
							public void run() {
								if(computerService.updateStatus(id, cname, "Yes"))
								{
									System.err.println("Status of system '" + id + 
																"' is updated succesfully");
								}
							}
						}, date); //1000*60*60*8L);
					}
					else
					{
						new Message().infoToClient(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
											response, "System not present in database. Wrong id entered.");
						System.out.println("System not present in database. Wrong id entered.");
//						response.sendError(403, "System not present in database");
					}
				}
				else
				{
					if(computerService.updateStatus(comp_Id, colName, status))
					{
						new Message().infoToClient("Status updated successfully",response);
						System.out.println("System availability status set as '" + status + "'");
					}
					else 
					{
						new Message().infoToClient(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
											response, "System not present in database. Wrong id entered.");
						System.out.println("System not present in database. Wrong id entered.");
					}
					
//					request.getSession().invalidate();
				}
				
				break;
		}
	}
	
}
