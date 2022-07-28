package Service;

import Beans.Admin;
import DAO.AdminDAO;

public class AdminService 
{
	private AdminDAO adminDAO = new AdminDAO();
	
	public static boolean createDatabase()
	{
		if(AdminDAO.createTable() != null)
			return true;
		
		return true;
	}
	
	public static boolean isEmpty()
	{
		if(AdminDAO.isDatabaseEmpty())
			return true;
		
		return false;
	}
	
	public static boolean isAdminPresent()
	{
		if(AdminDAO.isDatabaseEmpty() || (!AdminDAO.isDatabaseEmpty() && AdminDAO.noOfRows() == 0))
			return false;
					
		return true;
	}
	
	public Boolean Authenticate(int admin_Id, String Password)
	{
		String admin_Password = null;
		
		admin_Password = adminDAO.getPassword(admin_Id);
		
		if(admin_Password == null)
			return null;
		
		if(Password.equals(admin_Password))
			return true;
		
		return false;
	}
	
	public boolean createUser(Admin admin)
	{
		if(adminDAO.insertInto(admin) == 1)
			return true;
		
		return false;
	}
	
	public Admin getUser(int admin_Id)
	{
		Admin admin = null;
		
		admin = adminDAO.selectRecord(admin_Id);
		
		return admin;
	}
}