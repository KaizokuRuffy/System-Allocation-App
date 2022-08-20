package Service;

import java.io.File;
import java.util.List;
import java.util.Map;

import Beans.Employee;
import DAO.EmployeeDAO;
import Service.Util.Cipher;

public class EmployeeService 
{
	private EmployeeDAO employeeDAO= new EmployeeDAO();
	
	public boolean isPresent()
	{
		if(employeeDAO.noOfRows() != 0)
			return true;
		
		return false;	
	}
	
	public Boolean Authenticate(int emp_Id, String Password)
	{
		String emp_Password = null;
		//StringBuilder temp = null;
		
		try 
		{
			Map<String,String> map = employeeDAO.getPassword(emp_Id);

			emp_Password = map.get("password");
			
			Cipher cipher = new Cipher(emp_Password, ((Integer)emp_Id).toString(), map.get("name"), "user");
			emp_Password = cipher.decrypt();
		}
		catch (NullPointerException e) 
		{
			return null;
		}
		
//		System.out.println("Entered password -- " + Password);
//		System.out.println("Password in db -- " + emp_Password);
		
		if(Password.equals(emp_Password))
			return true;
		
		return false;
	}
	
	public boolean createUser(Employee emp)
	{
		
		if(emp.getEmp_Id() == -1)
			emp.setEmp_Id(employeeDAO.noOfRows() + 1);
		
		Cipher cipher = new Cipher(emp.getEmp_Password(), 
				((Integer)emp.getEmp_Id()).toString(), emp.getEmp_Name(), "user", true);

		emp.setEmp_Password(cipher.encrypt());
		
		if(employeeDAO.insertInto(emp) == 1)
			return true;
		
		new File(cipher.getKeysetFilename()).delete();
		//System.out.println("Json KeySet file deleted");
		
		return false;
	}
	
	public Employee getUser(int emp_Id)
	{
		Employee emp = null;
		
		emp = employeeDAO.selectRecord(emp_Id);
		
		Cipher cipher = new Cipher(emp.getEmp_Password(), 
				((Integer)emp.getEmp_Id()).toString(), emp.getEmp_Name(), "user");
		
		emp.setEmp_Password(cipher.decrypt());;
		
		return emp;
	}
	
	public List<Employee> getAllUser()
	{
		List<Employee> employeeList = null;
		
		employeeList = employeeDAO.selectAll();
		
		for(Employee emp : employeeList)
		{
			Cipher cipher = new Cipher(emp.getEmp_Password(), 
					((Integer)emp.getEmp_Id()).toString(), emp.getEmp_Name(), "user");

			emp.setEmp_Password(cipher.decrypt());
		}
		
		return employeeList;
	}
}
