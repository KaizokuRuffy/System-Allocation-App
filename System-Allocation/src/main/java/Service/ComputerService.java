package Service;

import java.util.List;

import Beans.Computer;
import DAO.ComputerDAO;

public class ComputerService 
{
	private ComputerDAO computerDAO = new ComputerDAO();
	
	public boolean addSystem(Computer comp)
	{	
<<<<<<< HEAD
=======
//		System.out.println(comp);

>>>>>>> refs/remotes/origin/Form-Validation
		if(comp.getWorking().equals(""))
			comp.setWorking("Yes");
		if(comp.getWorking().equals("Yes"))
			comp.setAvailable("Yes");
		if(comp.getWorking().equals("No"))
			comp.setAvailable("No");
		
		if(computerDAO.insertInto(comp) == 1)
			return true;
		
		return false;
	}
	
	public List<Computer> getAllSystems()
	{
		List<Computer> computerList = null;
		
		computerList = computerDAO.selectAll();
		return computerList;
	}
	
	public boolean updateStatus(String comp_Id, String colName, String value)
	{
		
		if(computerDAO.updateStatus(comp_Id, colName, value) == 1)
			return true;
		
		return false;
	}
	
	public Computer getSystem(String comp_Id)
	{
		return computerDAO.selectRecord(comp_Id);
	}
}
