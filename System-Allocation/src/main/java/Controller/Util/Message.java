package Controller.Util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Message 
{
	public void infoToClient(HttpServletResponse response)
	{
		PrintWriter out = null;
		
		try 
		{
			out = response.getWriter();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
		out.write("\n" + response.getStatus() + "");
		System.out.println(response.getStatus() + " - Request processed succesfully ");
	}
	
	public <T> void infoToClient(HttpServletRequest request, HttpServletResponse response, T obj)
	{
		String json = new Json().toJSON(obj);
		PrintWriter out = null;
		
		try 
		{
			out = response.getWriter();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		response.setContentType("application/json");
		out.write(json);
		response.setStatus(HttpServletResponse.SC_OK);
		System.out.println(response.getStatus() + " - Request processed succesfully ");
	}
	
}
