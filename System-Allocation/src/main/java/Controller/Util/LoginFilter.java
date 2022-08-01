package Controller.Util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.AdminService;

	/*
		urls to exclude from filtering : 
		1. /adminPresent
		2. /adminLogin
		3. /create  (only if admin is not present)
		4. /addAdmin (only if admin is not present)
		5. /userLogin
	*/

@WebFilter
(
		urlPatterns = "/*", 
		initParams = @WebInitParam
					(	name = "excludeURLs", 
						value = "/adminPresnt,/adminLogin,/create,/addAdmin,/userLogin") 
)
public class LoginFilter implements Filter
{	
	static private final Map<String, String> URLs = new HashMap<>();
	private static List<String> exludeURLs = new ArrayList<>();
	
	private static Consumer<String> exclude = (String urls) -> {
		String[] s = urls.split(",");
		
		synchronized (exludeURLs) 
		{
			for(String temp : s)
				exludeURLs.add(temp);
		}
		
	};
	
	static
	{
		System.out.println("Loading LoginFilter class");
		URLs.put("Admin not present", "/System-Allocation,/adminPresent,/adminLogin,/create,/addAdmin,/userLogin");
		URLs.put("Admin present","/System-Allocation,/adminPresent,/adminLogin,/userLogin");
		
		exclude.accept(new AdminService().isAdminPresent() 
				? URLs.get("Admin present") : URLs.get("Admin not present"));

		System.out.println(exludeURLs);
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
//		System.out.println("Inside doFilter()....");
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		/*
		System.out.println(request.getContextPath());
		System.out.println(request.getPathInfo());
		
		System.out.println(exludeURLs);
		System.out.println(exludeURLs.contains(((HttpServletRequest)req).getPathInfo()));
		
		
		System.out.println(request.getSession().getId());
		System.out.println(request.getSession().getAttribute("status"));
		*/
		
//		chain.doFilter(req, res);
		
		if( ((HttpServletRequest)req).getPathInfo() == null
				|| exludeURLs.contains(((HttpServletRequest)req).getPathInfo()) 
				|| request.getSession().getAttribute("status") != null )
			
			chain.doFilter(req, res);
		
		else 
			response.sendError(403, "User not logged in");
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		}
	
	@Override
	public void destroy() {
		
	}

	public static List<String> getExludeURLs() {
		return exludeURLs;
	}

	public static void setExludeURLs(List<String> exludeURLs) {
		LoginFilter.exludeURLs = exludeURLs;
	}

	public static Consumer<String> getExclude() {
		return exclude;
	}

	public static void setExclude(Consumer<String> exclude) {
		LoginFilter.exclude = exclude;
	}

	public static Map<String, String> getUrls() {
		return URLs;
	}

}


