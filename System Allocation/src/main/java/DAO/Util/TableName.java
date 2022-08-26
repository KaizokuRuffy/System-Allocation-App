package DAO.Util;

public class TableName {
	static final String Database = "Data"; // Database name

	// Table names
	static final String Employee = "Employee";
	static final String Admin = "Admin";
	static final String Computer = "`Resource Data`";
	static final String Session = "`Session data`";

	public static String getDatabase() {
		return Database;
	}

	public static String getEmployee() {
		return Employee;
	}

	public static String getAdmin() {
		return Admin;
	}

	public static String getComputer() {
		return Computer;
	}

	public static String getSession() {
		return Session;
	}

}
