package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.EmployeeRole;
import com.revature.model.EmployeeToken;
import com.revature.util.ConnectionUtil;

public class EmployeeRepositoryJdbc implements EmployeeRepository {
	
	private static Logger logger = Logger.getLogger(EmployeeRepositoryJdbc.class);
	
	private static final EmployeeRepository employeeRepository = new EmployeeRepositoryJdbc();
	
	private EmployeeRepositoryJdbc() {}
	
	public static EmployeeRepository getInstance() {
		return employeeRepository;
	}

	@Override
	public boolean insert(Employee employee) {
		logger.trace("Inserting new employee, " + employee.toString() + " in to database");
		try (Connection connection = ConnectionUtil.getConnection()){
			int index = 0;
			String sql = "INSERT INTO USER_T(U_FIRSTNAME, U_LASTNAME, U_USERNAME, U_PASSWORD, U_EMAIL, UR_ID) VALUES(?,?,?,?,?,?)";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++index, employee.getFirstName());
			statement.setString(++index, employee.getLastName());
			statement.setString(++index, employee.getUsername());
			statement.setString(++index, employee.getPassword());
			statement.setString(++index, employee.getEmail());
			statement.setInt(++index, employee.getEmployeeRole().getId());
			
			if (statement.executeUpdate() > 0) {
				logger.trace("Employee, " + employee.toString() + " successfully added to database.");
				return true;
			}
		} catch (SQLException e) {
			logger.error("Employee, " + employee.toString() + " could not be inserted", e);
		}
		return false;
	}

	@Override
	public boolean update(Employee employee) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			int index = 0;
			String sql = "UPDATE USER_T SET U_LASTNAME=?, U_FIRSTNAME=?, U_EMAIL=? WHERE U_ID=?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(++index, employee.getLastName());
			statement.setString(++index, employee.getFirstName());
			statement.setString(++index, employee.getEmail());
			statement.setInt(++index, employee.getId());
			
			if (statement.executeUpdate() > 0) {
				logger.trace("Employee updated successfuly");
				return true;
			}
		} catch (SQLException e) {
			logger.error("Could not update employee", e);
		}
		return false;
	}

	@Override
	public Employee select(int employeeId) {
		logger.trace("Getting employee with ID, " + employeeId);
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT UT.U_ID AS EMPLID, UT.U_FIRSTNAME AS FIRSTNAME, UT.U_LASTNAME AS LASTNAME, UT.U_USERNAME AS USERNAME, UT.U_PASSWORD AS EPASSWORD, UT.U_EMAIL AS EMAIL, UT.UR_ID AS ROLE_ID, UR.UR_TYPE AS ROLE_TYPE FROM USER_T UT INNER JOIN USER_ROLE UR ON UT.UR_ID = UR.UR_ID WHERE UT.UR_ID = UR.UR_ID AND UT.U_ID = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, employeeId);
			
			logger.trace("Employee with ID " + employeeId + " found.");
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return new Employee(
							result.getInt("EMPLID"),
							result.getString("FIRSTNAME"),
							result.getString("LASTNAME"),
							result.getString("USERNAME"),
							result.getString("EPASSWORD"),
							result.getString("EMAIL"),
							new EmployeeRole(result.getInt("ROLE_ID"), result.getString("ROLE_TYPE"))
						);
			}
		} catch (SQLException e) {
			logger.error("Could not get employee with ID " + employeeId, e);
		}
		
		return null;
	}

	@Override
	public Employee select(String username) {
		logger.trace("Getting employee with username, " + username);
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT UT.U_ID AS EMPLID, UT.U_FIRSTNAME AS FIRSTNAME, UT.U_LASTNAME AS LASTNAME, UT.U_USERNAME AS USERNAME, UT.U_PASSWORD AS EPASSWORD, UT.U_EMAIL AS EMAIL, UT.UR_ID AS ROLE_ID, UR.UR_TYPE AS ROLE_TYPE FROM USER_T UT INNER JOIN USER_ROLE UR ON UT.UR_ID = UR.UR_ID WHERE UT.UR_ID = UR.UR_ID AND UT.U_USERNAME = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				logger.trace("Employee with username " + username + " found.");
				return new Employee(
							result.getInt("EMPLID"),
							result.getString("FIRSTNAME"),
							result.getString("LASTNAME"),
							result.getString("USERNAME"),
							result.getString("EPASSWORD"),
							result.getString("EMAIL"),
							new EmployeeRole(result.getInt("ROLE_ID"), result.getString("ROLE_TYPE"))
						);
			}
		} catch (SQLException e) {
			logger.error("Could not get employee with username " + username, e);
		}
		return null;
	}

	@Override
	public Set<Employee> selectAll() {
		logger.trace("Getting all employees");
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT UT.U_ID AS EMPLID, UT.U_FIRSTNAME AS FIRSTAME, UT.U_LASTNAME AS LASTNAME, UT.U_USERNAME AS USERNAME, UT.U_PASSWORD AS EPASSWORD, UT.U_EMAIL AS EMAIL, UT.UR_ID AS ROLE_ID, UR.UR_TYPE AS ROLE_TYPE FROM USER_T UT INNER JOIN USER_ROLE UR ON UT.UR_ID = UR.UR_ID WHERE UR.UR_ID = 1";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			Set<Employee> set = new TreeSet<>();  
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				set.add(new Employee(
							result.getInt("EMPLID"),
							result.getString("FIRSTNAME"),
							result.getString("LASTNAME"),
							result.getString("USERNAME"),
							result.getString("EPASSWORD"),
							result.getString("EMAIL"),
							new EmployeeRole(result.getInt("ROLE_ID"), result.getString("ROLE_TYPE"))
					));
			}
			
			return set;
			
		} catch (SQLException e) {
			logger.error("Getting all employees failed", e);
		}
		return null;
	}

	@Override
	public String getPasswordHash(Employee employee) {
		logger.trace("Getting password hash for, " + employee.toString());
		try(Connection connection = ConnectionUtil.getConnection()) {
			int statementIndex = 0;
			String command = "SELECT GET_HASH(?) AS HASH FROM DUAL";
			PreparedStatement statement = connection.prepareStatement(command);
			statement.setString(++statementIndex, employee.getPassword());
			ResultSet result = statement.executeQuery();

			if(result.next()) {
				return result.getString("HASH");
			}
		} catch (SQLException e) {
			logger.warn("Exception getting customer hash", e);
		} 
		return new String();
	}

	@Override
	public boolean insertEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EmployeeToken selectEmployeeToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
