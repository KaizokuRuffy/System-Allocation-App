package Controller.Util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import Beans.Admin;

@SuppressWarnings("unused")
public class Json 
{	
	public <T> String toJSON(T obj)
	{
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		return json;
	}
	
	public <T>	String toJSON(List<T> list)
	{
		Gson gson = new Gson();
		String json = gson.toJson(list);
		return json;
	}
	
	public <T> T toPojo(HttpServletRequest request, Class<T> clazz)
	{
		String json = null;
		
		try 
		{
			json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
//		System.out.println(json);
		
		Gson gson = new Gson();
		T t = gson.fromJson(json, clazz);
		
		return t;
	}
	
	/*
	public static void main(String[] args) // throws JsonMappingException, JsonProcessingException 
	{
		String s = " { \"admin_Id\" : 1, "
				+ "\"admin_Name\": \"Kishore\", "
				+ "\"admin_Email\": \"kishorekumarjain23@gmail.com\","
				+ "\"admin_ContactNo\": \"9080971682\","
				+ "\"admin_Password\": \"sofdsfdl\" } ";
		
		JsonNode node = parse(" \"admin_Id\" : 1, "
				+ "\"admin_Name\": \"Kishore\", "
				+ "\"admin_Email\": \"kishorekumarjain23@gmail.com\","
				+ "\"admin_ContactNo\": \"9080971682\","
				+ "\"admin_Password\": \"sofdsfdl\" ");
		
		Admin admin = new Gson().fromJson(s, Admin.class);
		Admin admin = toPojo(s, Admin.class);
		System.out.println(admin);
		System.out.println();
		System.out.println(toJSON(admin));
	} */
}
