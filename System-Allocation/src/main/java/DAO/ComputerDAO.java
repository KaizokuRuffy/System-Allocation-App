package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Beans.Computer;
import DAO.Util.JDBC_Connection;
import DAO.Util.TableName;

public class ComputerDAO {
	static enum ColumnName {
		ID, MAC, Password, Available, Working, Location, Model, Year;
	}

	private static String createTable = "CREATE TABLE IF NOT EXISTS " + TableName.getComputer() +
			" ( " + ColumnName.ID + " varchar(255), "
			+ ColumnName.MAC + " varchar(255) UNIQUE, "
			+ ColumnName.Password + " varchar(255), "
			+ ColumnName.Available + " varchar(255), "
			+ ColumnName.Working + " varchar(255), "
			+ ColumnName.Location + " varchar(255), "
			+ ColumnName.Model + " varchar(255), "
			+ ColumnName.Year + " INT, "
			+ " PRIMARY KEY (" + ColumnName.ID + "))";

	public int noOfRows() {
		String query = "select count(*)"
				+ "from " + TableName.getComputer();

		Connection conn = JDBC_Connection.getConnection();

		try (Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			int count = 0;

			if (rs.next()) {
				count = Integer.parseInt(rs.getString(1));
				return count;
			}
		} catch (SQLException e) {
			JDBC_Connection.close();
			e.printStackTrace();
		}

		return 0;
	}

	public int insertInto(Computer comp) {

		String query = " INSERT INTO " + TableName.getComputer()
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		Connection conn = JDBC_Connection.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			// System.out.println("Adding computer details to database");

			ps.setString(1, comp.getComp_Id());
			ps.setString(2, comp.getMAC());
			ps.setString(3, comp.getComp_Password());
			ps.setString(4, comp.getAvailable());
			ps.setString(5, comp.getWorking());
			ps.setString(6, comp.getComp_Loc());
			ps.setString(7, comp.getModel());
			ps.setInt(8, comp.getYear());

			int rs = ps.executeUpdate();

			// if(rs == 1)
			// System.out.println("Details of computer '" + comp.getMAC()
			// + "' uploaded successfully ");

			return rs;
		} catch (SQLException e) {
			JDBC_Connection.close();
			e.printStackTrace();
		}
		return 0;
	}

	public List<Computer> selectAll() {

		System.out.println("Fetching All(Computer) details ");

		String query = "SELECT * "
				+ "FROM " + TableName.getComputer();

		Connection conn = JDBC_Connection.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			List<Computer> computerList = new ArrayList<>();

			while (rs.next()) {
				Computer comp = new Computer();
				comp.setComp_Id(rs.getString(ColumnName.ID.toString()));
				comp.setMAC(rs.getString(ColumnName.MAC.toString()));
				comp.setComp_Password(rs.getString(ColumnName.Password.toString()));
				comp.setAvailable(rs.getString(ColumnName.Available.toString()));
				comp.setWorking(rs.getString(ColumnName.Working.toString()));
				comp.setComp_Loc(rs.getString(ColumnName.Location.toString()));
				comp.setModel(rs.getString(ColumnName.Model.toString()));
				comp.setYear(rs.getInt(ColumnName.Year.toString()));

				// System.out.println(comp);
				computerList.add(comp);
			}
			return computerList;
		} catch (SQLException e) {
			JDBC_Connection.close();
			e.printStackTrace();
		}

		return null;
	}

	public int updateStatus(String comp_Id, String ColName, String value) {

		String yes = "Yes";
		String no = "No";

		String query = " UPDATE " + TableName.getComputer()
				+ " SET " + ColName + " = ? "
				+ " WHERE " + ColumnName.ID.toString() + " = '" + comp_Id + "'";

		if (ColName.equalsIgnoreCase(ColumnName.Working.toString()) && value.equalsIgnoreCase(no))
			query = " UPDATE " + TableName.getComputer()
					+ " SET " + ColName + " = ? "
					+ ", " + ColumnName.Available + " = '" + no + "' "
					+ " WHERE " + ColumnName.ID.toString() + " = '" + comp_Id + "'";

		else if (ColName.equalsIgnoreCase(ColumnName.Working.toString()) && value.equalsIgnoreCase(yes))
			query = " UPDATE " + TableName.getComputer()
					+ " SET " + ColName + " = ? "
					+ ", " + ColumnName.Available + " = '" + yes + "' "
					+ " WHERE " + ColumnName.ID.toString() + " = '" + comp_Id + "'";

		Connection conn = JDBC_Connection.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query);) {

			ps.setString(1, value);
			// System.out.println(ps);
			int rs = ps.executeUpdate();

			// if(rs == 1)
			// System.out.println(ColName + " Status changed");

			return rs;
		} catch (SQLException e) {
			JDBC_Connection.close();
			e.printStackTrace();
		}

		return 0;
	}

	public Computer selectRecord(String comp_Id) {
		String query = "SELECT *"
				+ "FROM " + TableName.getComputer()
				+ "WHERE " + ColumnName.ID + " =  '" + comp_Id + "'";

		Connection conn = JDBC_Connection.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				Computer comp = new Computer();
				comp.setComp_Id(rs.getString(ColumnName.ID.toString()));
				comp.setMAC(rs.getString(ColumnName.MAC.toString()));
				comp.setAvailable(rs.getString(ColumnName.Available.toString()));
				comp.setComp_Loc(rs.getString(ColumnName.Location.toString()));
				comp.setComp_Password(rs.getString(ColumnName.Password.toString()));
				comp.setModel(rs.getString(ColumnName.Model.toString()));
				comp.setWorking(rs.getString(ColumnName.Working.toString()));
				comp.setYear(rs.getInt(ColumnName.Year.toString()));

				return comp;
			}
		} catch (SQLException e) {
			JDBC_Connection.close();
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	public void deleteRecord(String comp_Id) {
		String query = "DELETE FROM ?"
				+ "WHERE ? = ? ";
	}

	public static String getCreateTable() {
		return createTable;
	}

	public static void setCreateTable(String createTable) {
		ComputerDAO.createTable = createTable;
	}

}
