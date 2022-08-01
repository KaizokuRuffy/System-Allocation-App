package Service;

import java.util.List;
import java.util.Map;

import Beans.Employee;
import DAO.EmployeeDAO;
import Service.Util.Cipher;

public class EmployeeService 
{
	private EmployeeDAO employeeDAO= new EmployeeDAO();
	
	public Boolean Authenticate(int emp_Id, String Password)
	{
		String emp_Password = null;
		StringBuilder temp = null;
		
		try 
		{
			Map<String,String> map = employeeDAO.getPassword(emp_Id);

			emp_Password = map.get("password");
			
			Cipher cipher = new Cipher(Password, ((Integer)emp_Id).toString(), map.get("name"), true);
			Password = cipher.encrypt();
		}
		catch (NullPointerException e) 
		{
			return null;
		}
		
		if(Password.equals(emp_Password))
			return true;
		
		return false;
	}
	
	public boolean createUser(Employee emp)
	{
		
		Cipher cipher = new Cipher(emp.getEmp_Password(), 
				((Integer)emp.getEmp_Id()).toString(), emp.getEmp_Name(), true);

		emp.setEmp_Password(cipher.encrypt());
		
		if(employeeDAO.insertInto(emp) == 1)
			return true;
		
		return false;
	}
	
	public Employee getUser(int emp_Id)
	{
		Employee emp = null;
		
		emp = employeeDAO.selectRecord(emp_Id);
		
		Cipher cipher = new Cipher(emp.getEmp_Password(), 
				((Integer)emp.getEmp_Id()).toString(), emp.getEmp_Name());
		
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
					((Integer)emp.getEmp_Id()).toString(), emp.getEmp_Name());

			emp.setEmp_Password(cipher.decrypt());
		}
		
		return employeeList;
	}
}