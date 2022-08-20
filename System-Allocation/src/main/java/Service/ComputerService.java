package Service;

import java.util.List;

import Beans.Computer;
import DAO.ComputerDAO;

public class ComputerService 
{
	private ComputerDAO computerDAO = new ComputerDAO();
	
	public boolean addSystem(Computer comp)
	{
		if(comp.getComp_Id() == -1)
			comp.setComp_Id(computerDAO.noOfRows() + 1);
		
		comp.setAvailable("Yes");
		comp.setWorking("Yes");
		
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
	
	public boolean updateStatus(int comp_Id, String colName, String value)
	{
		
		if(computerDAO.updateStatus(comp_Id, colName, value) == 1)
			return true;
		
		return false;
	}
	
	public Computer getSystem(int comp_Id)
	{
		return computerDAO.selectRecord(comp_Id);
	}
}
