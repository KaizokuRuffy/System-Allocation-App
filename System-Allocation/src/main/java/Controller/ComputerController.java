package Controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Beans.Computer;
import Controller.Util.Json;
import Controller.Util.Message;
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
						new Message().infoToClient(request, response, computerList);
					}
					else
					{
						response.sendError(500, "No resources data in database. Add system details and then try fetching data.");
						System.out.println("No resource data present in table");
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
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		final String CRUD = request.getPathInfo();
		
		switch(CRUD)
		{
			case "/addSystem":
				
				System.out.println("\n-- Saving resource details to database --");
				Computer comp = new Json().toPojo(request, Computer.class);
				
				if(comp != null)
				{
					if(computerService.addSystem(comp))
					{
						System.out.println("Resource details saved ");
						new Message().infoToClient(response);
					}
					else
					{
						response.sendError(500, "Database error");
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
				
				int comp_Id = Integer.parseInt(request.getParameter("comp_Id"));
				String colName = request.getParameter("colName");
				String status = request.getParameter("status");
				
				System.out.println("\n-- Updating " + colName + " status of system --");
				
				if(computerService.updateStatus(comp_Id, colName, status))
				{
					System.out.println("Status of system '" + comp_Id + 
												"' is updated succesfully");
					new Message().infoToClient(response);
				}
				else
				{
					System.out.println("System not present in database. Wrong id entered.");
					response.sendError(403, "System not present in database");
				}
				
				break;
		}
	}
	
}
