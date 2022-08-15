package Controller.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.servlet.http.HttpSession;

import Service.AdminService;

/*
	urls to exclude from filtering : 
	1. /adminPresent
	2. /adminLogin
	3. /create  (only if admin is not present)
	4. /addAdmin (only if admin is not present)
	5. /userLogin
*/

@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "excludeURLs", value = "/adminPresent,/adminLogin,/create,/addAdmin,/userLogin"))
public class LoginFilter implements Filter {
	//static final String resources = "/index.html,/JavaScript/Event/index.js";
	//static final String redirect = "/HTML/Admin.html,/HTML/User.html,/HTML/User-Reg.html,/HTML/Admin-Reg.html,/HTML/System-Reg.html";
	static private final Map<String, String> URLs = new HashMap<>();
	private static List<String> exludeURLs = new ArrayList<>();
	private static List<String> commonURLs = Arrays.asList(new String[] { "/getUser", "/getAllSession" });
	private static List<String> adminURLs = Arrays
			.asList(new String[] { "/adminLogout", "/getAdmin", "/create", "/addAdmin", "/getAllUsers", "/addUser",
					"/getAllSystems", "/addSystem", "/updateStatus", });
	private static List<String> employeeURLs = Arrays.asList(new String[] { "/userLogout" });

	private static Consumer<String> exclude = (String urls) -> {
		String[] s = urls.split(",");

		synchronized (exludeURLs) {
			exludeURLs.clear();
			for (String temp : s)
				exludeURLs.add(temp);
		}
	};

	static {
		System.out.println("Loading LoginFilter class");
		// URLs.put("Admin not present",
		// "/System-Allocation,/adminPresent,/adminLogin,/create,/addAdmin,/userLogin");
		// URLs.put("Admin
		// present","/System-Allocation,/adminPresent,/adminLogin,/userLogin");
		
		URLs.put("Admin not present", "/adminPresent,/adminLogin,/create,/addAdmin,/userLogin");
		URLs.put("Admin present", "/adminPresent,/adminLogin,/userLogin");

		exclude.accept(new AdminService().isAdminPresent()
				? URLs.get("Admin present")
				: URLs.get("Admin not present"));
		System.out.println(exludeURLs);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		// System.out.println("Inside doFilter()....");

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession sess = request.getSession(false);
		
		/*
		 * System.err.println("----------------------------");
		 * System.out.println(request.getRequestURI());
		 * System.out.println(request.getContextPath());
		 * System.out.println(request.getPathInfo());
		 * System.out.println(request.getPathTranslated());
		 * System.out.println(request.getServletPath());
		 * System.err.println("----------------------------");
		 */
		// System.out.println(request.getRequestURI() + " " + (sess != null ?
		// sess.getId() : null));

		/*
		 * System.out.println(request.getContextPath());
		 * System.out.println(request.getPathInfo());
		 * 
		 * System.out.println(exludeURLs);
		 * System.out.println(exludeURLs.contains(((HttpServletRequest)req).getPathInfo(
		 * )));
		 * 
		 * 
		 * System.out.println(request.getSession().getId());
		 * System.out.println(request.getSession().getAttribute("status"));
		 */

		// chain.doFilter(req, res);
		
//		System.out.println(request.getPathInfo());

		// System.out.println(request.getContextPath() + " " + request.getPathInfo());

//		System.out.println(request.getServletPath());
		
//		if("http://127.0.0.1:8887".equals(request.getHeader("Origin")))
//		{
//			response.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8887");
//			response.addHeader("Access-Control-Allow-Headers", "*");
//			response.addHeader("Access-Control-Allow-Methods",
//                    "GET, OPTIONS, HEAD, PUT, POST, DELETE");
//			response.getWriter().write("HTTP/1.1 200 ok");
//			;
//		}
		
//		System.out.println(resources);
//		System.out.println(request.getServletPath());
//		System.out.println(resources.contains(request.getServletPath()));
		
	
		if (request.getPathInfo() == null || exludeURLs.contains(request.getPathInfo()))
			chain.doFilter(req, res);

		else if (commonURLs.contains(request.getPathInfo())) {
			// System.out.println("in common url");
			if (sess != null && sess.getAttribute("status") != null)

				chain.doFilter(req, res);

			else
				response.sendError(403, "Cannot perform unauthorized operation.");
		}

		else if (adminURLs.contains(request.getPathInfo())) {
			// System.out.println("in admin url");
			if (sess != null && sess.getAttribute("status") != null
					&& sess.getAttribute("status").equals("admin logged in")) {
				// System.out.println(request.getPathInfo());
				chain.doFilter(req, res);
			} else
				response.sendError(403, "Cannot perform unauthorized operation.");
		}

		else if (employeeURLs.contains(request.getPathInfo())) {
			// System.out.println("in employee url");
			if (sess != null && sess.getAttribute("status") != null &&
					sess.getAttribute("status").equals("employee logged in"))

				chain.doFilter(req, res);

			else
				response.sendError(403, "Cannot perform unauthorized operation.");
		} 
		
		//Authenticating request for html files made by client
		else if(request.getServletPath() != null && request.getServletPath().contains(".html")) {
			
			System.out.println("URL : "+request.getRequestURL());
			System.out.println("URI : "+request.getRequestURI());
			System.out.println("Path : "+request.getPathInfo());
			
			if(request.getServletPath().contains("index.html"))
				chain.doFilter(request, response);
			
			if(request.getServletPath().contains("Admin-Reg.html") && !new AdminService().isAdminPresent())
				chain.doFilter(request, response);
			
			if((request.getServletPath().contains("Admin.html") || request.getServletPath().contains("User-Reg.html")
					|| request.getServletPath().contains("System-Reg.html") || request.getServletPath().contains("User-Reg.html")
					|| request.getServletPath().contains("Admin-Reg.html")) && sess != null 
					&& sess.getAttribute("status").equals("admin logged in"))
				chain.doFilter(request, response);
			
			if(request.getServletPath().contains("User.html") && sess != null &&
											sess.getAttribute("status").equals("employee logged in"))
				chain.doFilter(request, response);
		}
		
		//Authenticating request for JavaScript files made by client
		else if(request.getServletPath() != null && request.getServletPath().contains(".js")) {
			chain.doFilter(request, response);
		}
		
		else {
			System.err.println("----------------------------------------------------------------------------");
			System.out.println("Request URI : "+request.getRequestURI());
		    System.out.println("Context Path : "+request.getContextPath());
		    System.out.println("Servlet path : "+request.getServletPath());
		    System.out.println("Path info : "+request.getPathInfo());
		    System.out.println("Path translated : "+request.getPathTranslated());
		    
		    System.out.println("Request URI : "+request.getRequestURI());
			System.out.println("Referer : "+request.getHeader("Referer"));
			System.out.println(sess != null ? sess.getId() : "No session");
			System.err.println("Cannot perform unauthorized operation");
			System.err.println("----------------------------------------------------------------------------");
			response.sendError(403, "Cannot perform unauthorized operation.");
		}

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

	public static Consumer<String> getExclude() {
		return exclude;
	}

	public static Map<String, String> getUrls() {
		return URLs;
	}

}
