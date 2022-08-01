package Service;

import java.util.Map;

import Beans.Admin;
import DAO.AdminDAO;
import Service.Util.Cipher;

public class AdminService 
{
	private AdminDAO adminDAO = new AdminDAO();	
    private static  boolean isAdminPresent;
    private static boolean isEmpty = true;
	
	static {
    	isAdminPresent = new AdminService().isAdminPresent();
    	isEmpty = new AdminService().isEmpty();
    	System.out.println(isAdminPresent + " " + isEmpty);
    }

    public static void setAdminPresent(boolean isAdminPresent) {
		AdminService.isAdminPresent = isAdminPresent;
	}

	public static void setEmpty(boolean isEmpty) {
		AdminService.isEmpty = isEmpty;
	}

	
	public static boolean createDatabase()
	{
		if(AdminDAO.createTable() != null)
			return true;
		
		return true;
	}
	
	public boolean isEmpty()
	{
		if(!isEmpty)
			return false;
		
		if(adminDAO.isDatabaseEmpty())
			return true;
		
		return false;
	}
	
	public boolean isAdminPresent()
	{
		if(isAdminPresent)
			return true;
		
//		if(adminDAO.isDatabaseEmpty() || adminDAO.noOfRows() == 0)
//			return false;
		
//		System.out.println("isEmpty -> "+isEmpty);
//		System.out.println("adminDAO.isDatabaseEmpty() -> " + adminDAO.isDatabaseEmpty());
//		System.out.println("isEmpty() -> " +isEmpty());
		
		if(isEmpty())
			return false;
		else if(adminDAO.noOfRows() == 0)
			return false;
		
		return true;
	}
	
	public Boolean Authenticate(int admin_Id, String Password)
	{
		String admin_Password = null;
		StringBuilder temp = null;
		
		try 
		{
			Map<String,String> map = adminDAO.getPassword(admin_Id);

			admin_Password = map.get("password");
			
			Cipher cipher = new Cipher(admin_Password, ((Integer)admin_Id).toString(), map.get("name"));
			admin_Password = cipher.decrypt();
		}
		catch (NullPointerException e) 
		{
			return null;
		}
		
		if(Password.equals(admin_Password))
			return true;
		
		return false;
	}
	
	public boolean createUser(Admin admin)
	{
		Cipher cipher = new Cipher(admin.getAdmin_Password(), 
										((Integer)admin.getAdmin_Id()).toString(), admin.getAdmin_Name(), true);
		
		admin.setAdmin_Password(cipher.encrypt());
		
		if(adminDAO.insertInto(admin) == 1)
			return true;
		
		return false;
	}
	
	public Admin getUser(int admin_Id)
	{
		Admin admin = null;
		
		admin = adminDAO.selectRecord(admin_Id);
		
		Cipher cipher = new Cipher(admin.getAdmin_Password(), 
				((Integer)admin.getAdmin_Id()).toString(), admin.getAdmin_Name());
		
		admin.setAdmin_Password(cipher.decrypt());
		
		return admin;
	}
}