package Service;

import java.util.List;

import Beans.Employee;
import DAO.EmployeeDAO;

public class EmployeeService 
{
	private EmployeeDAO employeeDAO= new EmployeeDAO();
	
	public Boolean Authenticate(int emp_Id, String Password)
	{
		String emp_Password = null;
		
		emp_Password = employeeDAO.getPassword(emp_Id);
		
		if(emp_Password == null)
			return null;
		
		if(Password.equals(emp_Password))
			return true;
		
		return false;
	}
	
	public boolean createUser(Employee emp)
	{
		if(employeeDAO.insertInto(emp) == 1)
			return true;
		
		return false;
	}
	
	public Employee getUser(int emp_Id)
	{
		Employee emp = null;
		
		emp = employeeDAO.selectRecord(emp_Id);
		return emp;
	}
	
	public List<Employee> getAllUser()
	{
		List<Employee> employeeList = null;
		
		employeeList = employeeDAO.selectAll();
		
		return employeeList;
	}
}
