package Controller;

import java.io.IOException;
// import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Beans.Computer;
import Beans.Employee;
import Beans.Session;
import Controller.Util.Counter;
import Controller.Util.DATE;
import Controller.Util.Json;
import Controller.Util.Message;
import Controller.Util.SchedulerTask;
import Service.ComputerService;
import Service.EmployeeService;
import Service.SessionService;

@WebServlet(urlPatterns = { "/EmployeeController/*" })
public class EmployeeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EmployeeService employeeService = new EmployeeService();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final String CRUD = request.getPathInfo();

		switch (CRUD) {
			case "/getUser":
				System.out.println("\n-- Fetching User profile --");

				int emp_Id = Integer.parseInt(request.getParameter("emp_Id"));
				Employee emp = employeeService.getUser(emp_Id);

				if (emp == null) {
					new Message().infoToClient(HttpServletResponse.SC_NOT_FOUND,
							response, "Invalid username (or) database empty");
					// response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid username (or)
					// database empty");
					System.out.println("Invalid username (or) database empty");
				} else {
					if (request.getSession(false).getAttribute("status").equals("employee logged in")) {
						System.out.println("\nUser profile fetched successfully - from user");
						new Message().infoToClient(response, emp);
					} else if (request.getSession(false).getAttribute("status").equals("admin logged in")) {
						System.out.println("\nUser profile fetched successfully - from admin");
						List<Session> sessionList = new SessionService().getEmpSession(emp_Id);
						List<Object> list = new ArrayList<>();
						list.add(emp);
						list.add(sessionList);

						// String s1 = new Gson().toJson(list);
						// s1 = s1.substring(1, s1.length() - 1);
						// s1 = "{" + s1 + "}";
						new Message().infoToClient(response, list);
					}
				}

				break;

			case "/getAllUsers":

				System.out.println("\n-- Fetching all user data --");

				List<Employee> employeeList = employeeService.getAllUser();

				if (employeeList != null) {
					if (employeeList.size() > 0) {
						System.out.println("All user profiles fetched successfully");
						new Message().infoToClient(response, employeeList);
					} else {
						new Message().infoToClient(HttpServletResponse.SC_NOT_FOUND, response,
								"No users in database. Create users and then try fetching data.");
						// response.sendError(HttpServletResponse.SC_NOT_FOUND,
						// "No users in database. Create users and then try fetching data.");
						System.out.println("No users in database. reate users and then try fetching data.");
					}
				} else {
					new Message().infoToClient(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response,
							"Database error");
					System.out.println("Database error");
					// response.sendError(500, "Database is empty");
				}

				break;

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final String CRUD = request.getPathInfo();
		// PrintWriter out = response.getWriter();

		switch (CRUD) {
			case "/userLogin":

				System.out.println("\n--- Authenticating user ---");

				String username = null;
				String Password = null;

				username = request.getParameter("emp_Email");
				//emp_Id = Integer.parseInt(request.getParameter("emp_Id"));// var
				Password = new String(request.getParameter("emp_Password"));// var
				Boolean auth = null;
				
				int emp_Id = 0;
				
				try {
					emp_Id = employeeService.getUser(username).getEmp_Id();
					//System.out.println(emp_Id);
					auth = employeeService.Authenticate(emp_Id, Password); // DB call
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				

				if (auth == null) {
					new Message().infoToClient(HttpServletResponse.SC_FORBIDDEN, response,
							"Invalid username / password");
					// out.write("Invalid username");
					// response.sendError(403, "Invalid username / password");
					System.out.println("Invalid username!!! Enter correct username");

					break;
				}

				Session session = new Json().toPojo(request, Session.class);
				session.setEmp_Id(emp_Id);
				List<Session> sessionList = new SessionService().getEmpSession(emp_Id);

				Computer comp = null;

				if (!"-1".equals(session.getComp_Id()))
					comp = new ComputerService().getSystem(session.getComp_Id());

				String comp_Id = "-1";
				int temp_Id = -1;

				for (Session temp : sessionList) {
					
//					System.out.println(temp);
					
					if(!"-1".equals(session.getComp_Id() ) && temp.getLogIn_Date().equals(session.getLogIn_Date())
							&& !DATE.getShift(temp.getLogOut_Time()).equals(DATE.getShift())
							&& temp.getComp_Id() == session.getComp_Id()) {
						comp_Id = temp.getComp_Id();
						temp_Id = temp.getEmp_Id();
					}
					
//					System.out.println(DATE.getShift(temp.getLogOut_Time()));
//					System.out.println(DATE.getShift());
					if (temp.getLogIn_Date().equals(session.getLogIn_Date()) && 
												DATE.getShift(temp.getLogOut_Time()).equals(DATE.getShift())) {
						//System.out.println("Inside this if");
						// User already logged in (N)
						comp_Id = temp.getComp_Id();
						temp_Id = temp.getEmp_Id();

						if ("-1".equals(session.getComp_Id())) {
							comp = new ComputerService().getSystem(comp_Id);

							if (comp.getAvailable().equals("Yes") || (comp.getAvailable().equals("No") 
											&& emp_Id == temp.getEmp_Id()))
								new Message().infoToClient("comp_Id :" + comp_Id + "," + emp_Id, response);
							
							else
								new Message().infoToClient("Available - No, comp_Id :" + comp_Id, response);
						}
						break;
					}
				}

				// User logging in for first time (-1)
				if ("-1".equals(session.getComp_Id()) &&  "-1".equals(comp_Id)) {
					new Message().infoToClient("comp_Id :" + comp_Id + "," + emp_Id, response);
				} else if (!"-1".equals(session.getComp_Id())) {
					if (auth == true) {
						System.out.println(comp_Id);
						/*
						 * Session session = new Json().toPojo(request, Session.class); List<Session>
						 * sessionList = new SessionService().getEmpSession(emp_Id);
						 * for(Session temp : sessionList) {
						 * if(temp.getLogIn_Date().equals(session.getLogIn_Date())) comp_Id =
						 * temp.getComp_Id(); }
						 */
						System.out.println(session.getEmp_Id() + " " + temp_Id);
						if (comp != null && (comp.getAvailable().equals("Yes")) ||
								((comp.getAvailable().equals("No"))) && session.getEmp_Id() == temp_Id) {
							
							boolean flag = false;
							
							if("-1".equals(comp_Id))
							{
								List<Session> sl = new SessionService().getAllSessions();
								for(Session temp : sl)
									if(temp.getComp_Id() == comp.getComp_Id() 
									&& temp.getLogIn_Date().equals(session.getLogIn_Date())
									&& DATE.getShift(temp.getLogOut_Time())
									.equals(DATE.getShift(session.getLogIn_Time()))
									) 
									{
										new Message().infoToClient(HttpServletResponse.SC_BAD_REQUEST,
												response, "System already in use");
										// out.write("Cannot login to different system");
										// response.sendError(403, "Cannot login to different system");
										System.err.println("System already in use");
										
										flag = true;
										break;
									}
							}
							if (flag)
								break;
							
							if ("-1".equals(comp_Id)) {
								System.out.println("Logged in successfully");

								final int id = emp_Id;

								HttpSession sess = request.getSession(true);
								sess.setAttribute("status", "employee logged in");
								sess.setMaxInactiveInterval(60 * 60 * 8);
								// System.out.println(request.getSession().getId());

								request.setAttribute("Session", new Json().toJSON(session));
								// Calling 'SessionController' servlet to add session data to database as the
								// user logs in
								RequestDispatcher rd = request.getRequestDispatcher("/SessionController/addSession");
								response.setContentType("text/plain");
								response.setIntHeader("emp_Id", emp_Id);
//								System.out.println("write");
//								response.getWriter().write(emp_Id);
								rd.forward(request, response);

								SchedulerTask st = new SchedulerTask();
								st.schedule(new TimerTask() {
									@Override
									public void run() {
										boolean flag = true;
										try {
											// System.out.println(emp_Id + " In try block");
											// System.out.println(emp_Id + " " + sess.getAttribute("status"));
											sess.getAttribute("status");//Checking if session is already invalidated or not.
										} catch (IllegalStateException e) {
											// System.out.println(emp_Id + " In catch block");
											flag = false;
										}
										if (flag) {
											System.err.println("Session of user '" + id + "' invalidated");
											sess.invalidate();
											Counter.getcounter().atLogout();
										}

									}
								}, 1000 * 60 * 60 * 24L); // *60*60*24L);

								Counter.getcounter().atLogin();
							} else if (comp.getComp_Id().equals(comp_Id)) {
								System.out.println("Logged in successfully");

								final int id = emp_Id;

								HttpSession sess = request.getSession(true);
								sess.setAttribute("status", "employee logged in");
								sess.setMaxInactiveInterval(60 * 60 * 8);
								// System.out.println(request.getSession().getId());

								// Calling 'SessionController' servlet to add session data to database as the
								// user logs in
								// RequestDispatcher rd =
								// request.getRequestDispatcher("/SessionController/addSession");
								// rd.forward(request, response);

								RequestDispatcher rd = request.getRequestDispatcher("/SystemController/updateStatus");
								request.setAttribute("comp_Id", session.getComp_Id());
								request.setAttribute("colName", "available");
								request.setAttribute("status", "No");
								response.setIntHeader("emp_Id", emp_Id);
								response.setContentType("text/plain");
								response.getWriter().write("Logged in successfully");
								rd.forward(request, response);

								/*
								 * SchedulerTask st = new SchedulerTask(); st.schedule(new TimerTask() {
								 * 
								 * @Override public void run() { boolean flag = true; try { //
								 * System.out.println(emp_Id + " In try block"); // System.out.println(emp_Id +
								 * " " + sess.getAttribute("status")); sess.getAttribute("status"); } catch
								 * (IllegalStateException e) { // System.out.println(emp_Id +
								 * " In catch block"); flag = false; } if (flag) {
								 * System.err.println("Session of user '" + id + "' invalidated");
								 * sess.invalidate(); Counter.getcounter().atLogout(); }
								 * 
								 * } }, 1000 * 60 * 60 * 24L); // *60*60*24L);
								 */
								Counter.getcounter().atLogin();
							} else if (comp.getComp_Id() != comp_Id) {
								
								//System.out.println(comp_Id);
								
								new Message().infoToClient(HttpServletResponse.SC_BAD_REQUEST,
										response, "Cannot login to different system");
								// out.write("Cannot login to different system");
								// response.sendError(403, "Cannot login to different system");
								System.err.println("Cannot login to different system");
							}
						} else {
							new Message().infoToClient(HttpServletResponse.SC_CONFLICT, response,
									comp == null ? "System not present" : "System not available");

							// out.write(comp == null ? "System not present" : "System not available");
							// response.sendError(403, comp == null ? "System not present" : "System not
							// available");
							System.err.println(comp == null ? "System not present" : "System not available");
						}

					} else if (auth == false) {
						new Message().infoToClient(HttpServletResponse.SC_FORBIDDEN,
								response, "Invalid username / password");
						// out.write("Invalid username / password");
						// response.sendError(403, "Invalid username / password");
						System.out.println("Invalid password!!! Enter password again");
					}
				}

				break;

			case "/userLogout":

				System.out.println("\n-- User logging out of the web site --");
				// System.out.println(request.getSession().getId());
				// request.getSession().invalidate();

				// Calling 'SessionController' servlet to update session data to database as the
				// user logs out
				RequestDispatcher rd = request.getRequestDispatcher("/SessionController/updateSession");
				response.setContentType("text/plain");
				response.getWriter().write("Logged out successfully");
				rd.forward(request, response);

				request.getSession(false).invalidate();
				Counter.getcounter().atLogout();
				System.out.println("User logged out successfully");

				break;

			case "/addUser":

				System.out.println("--- User account creation operation ---");

				Employee emp = new Json().toPojo(request, Employee.class);

				// System.out.println(adminJson);
				// System.out.println(admin);

				if (emp != null) {
					if (employeeService.createUser(emp)) // DB call
					{
						new Message().infoToClient(HttpServletResponse.SC_CREATED,
								response, "Account created successfully");
						System.out.println(emp.getEmp_Name() + ", your account has been created succesfully");
					} else {
						new Message().infoToClient(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response,
								"Duplicate user not allowed. Adhaar ID, Email, Mobile no should be unique");
						// response.sendError(500, "Duplicate user not allowed");
						System.out.println("Duplicate user not allowed");
					}
				} else {
					new Message().infoToClient(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response,
							"JSON parse error");
					System.out.println("JSON parse error");
				}

				break;

		}
	}
}
