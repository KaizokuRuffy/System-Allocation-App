package Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Beans.Session;
import DAO.SessionDAO;

public class SessionService 
{
	private SessionDAO sessionDAO = new SessionDAO();
	
	public boolean addSession(Session session)
	{
		
		session.setShift
		(
				((session.getLogIn_Time().compareTo("08:00") >= 0) && 
				(session.getLogIn_Time().compareTo("16:00") <= 0)) ? "Morning" : 
				((session.getLogIn_Time().compareTo("16:00") >= 0) && 
			    (session.getLogIn_Time().compareTo("00:00") <= 0)) ? "Evening" : "Night"
		);
		
		if(sessionDAO.insertInto(session) == 1)
			return true;
		
		return false;
	}
	
	public List<Session> getAllSessions()
	{
		List<Session> sessionList = null;
		
		sessionList = sessionDAO.selectAll();
		return sessionList;
	}
	
	public boolean updateSession(Session session)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-mm-dd HH:mm");
//		System.out.println(session);
		Date d1 = null;
		try 
		{
//			System.out.println(sdf.format(session.getLogIn_Date()) + " " + session.getLogIn_Time());
			d1 = SDF.parse(sdf.format(session.getLogIn_Date()) + " " + session.getLogIn_Time());
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
		
		Date d2 = null;
		try 
		{
//			System.out.println(sdf.format (session.getLogOut_Date()) + " " + session.getLogOut_Time());
			d2 = SDF.parse(sdf.format (session.getLogOut_Date()) + " " + session.getLogOut_Time());
		} 
		catch (ParseException e1) 
		{
			e1.printStackTrace();
		}
			
//		System.out.println(d1 + " --> " + d2);
		long diff = (d2.getTime() - d1.getTime());
//		System.out.println(diff);
		session.setTotal_Time((diff/(1000*60*60)) + " Hrs "+ 
				             ((diff%(1000*60*60))/(60000) + " mins"));
		
		if(sessionDAO.updateRecord(session) == 1)
			return true;
		
		return false;
	}
}
