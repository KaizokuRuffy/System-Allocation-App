package Controller.Util;

import java.util.Calendar;
import java.util.Date;

public class DATE 
{
	public static Date getDate(int hh, int mm, int ss)
	{
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.HOUR_OF_DAY, hh);
		calender.set(Calendar.MINUTE, mm);
		calender.set(Calendar.SECOND, ss);
		
		return calender.getTime();
	}
}
