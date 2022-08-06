package Controller.Util;

import java.util.TimerTask;

import DAO.Util.JDBC_Connection;

public class Counter
{
	private static Counter counter;
	
	static {
		counter = new Counter();
	}
	
	private long loginCount = 0l;
	
	public long getLoginCount() {
		return loginCount;
	}

	public static Counter getcounter() {
		
		return counter;
	}
	
	public void userNotPresent()
	{
		if(loginCount == 0)
			synchronized (counter) {
				if(loginCount == 0)
				{
					 SchedulerTask st = new SchedulerTask();
					 st.schedule(new TimerTask() {
						 
						 @Override
						  public void run() {
							  try 
							  {
								  if(Counter.getcounter().getLoginCount() == 0)
									  JDBC_Connection.close();
//									  else
//										  Counter.getcounter().userNotPresent();
							  }
							  catch (Exception ex) 
							  {
								 ex.printStackTrace();
							  }
						  }
					 }, 5000L);
				}
//					JDBC_Connection.close();
			}
	}
	
	public void atLogin()
	{
		synchronized (counter) {
			loginCount++;
//			System.out.println(loginCount);
		}
	}
	
	public void atLogout()
	{
		synchronized (counter) {
			loginCount--;
//			System.out.println(loginCount);
		}
		Counter.getcounter().userNotPresent();
	}

	@Override
	public String toString() {
		return "Counter [loginCount=" + loginCount + "]";
	}

}
