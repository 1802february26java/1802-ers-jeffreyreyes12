package com.revature.repository;

//import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.util.ConnectionUtil;

public class ReimbursementRepositoryJdbc implements ReimbursementRepository {
	
	private static Logger logger = Logger.getLogger(ReimbursementRepositoryJdbc.class);
	
	private static ReimbursementRepository reimbursementRepository = new ReimbursementRepositoryJdbc();
	
	private ReimbursementRepositoryJdbc() {}
	
	public static ReimbursementRepository getInstance() {
		return reimbursementRepository;
	}
	
	@Override
	public boolean insert(Reimbursement reimbursement) {
		logger.trace("Inserting reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			int index = 0;
			String sql = "INSERT INTO REIMBURSEMENT(R_ID, R_REQUESTED, R_AMOUNT, R_DESCRIPTION, EMPLOYEE_ID, RS_ID, RT_ID) VALUES(NULL, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setTimestamp(++index, Timestamp.valueOf(reimbursement.getRequested()));
			statement.setDouble(++index, reimbursement.getAmount());
			statement.setString(++index, reimbursement.getDescription());
			statement.setInt(++index, reimbursement.getRequester().getId());
			statement.setInt(++index, reimbursement.getStatus().getId());
			statement.setInt(++index, reimbursement.getType().getId());
			
			if (statement.executeUpdate() > 0) {
				logger.trace("Reimbursement insert successful");
				return true;
			}
		} catch (SQLException e) {
			logger.error("Reimbursement insert", e);
		}
		
		return false;
	}

	@Override
	public boolean update(Reimbursement reimbursement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Reimbursement select(int reimbursementId) {
		return null;
	}

	@Override
	public Set<Reimbursement> selectPending(int employeeId) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT"
							+ " RB.R_ID AS R_ID, RB.R_AMOUNT AS R_AMOUNT, RB.R_DESCRIPTION AS R_DESCRIPTION,"
							+ " UT.U_FIRSTNAME AS U_FIRSTNAME, UT.U_LASTNAME AS U_LASTNAME, RS.RS_STATUS AS RS_STATUS,"
							+ " RT.RT_TYPE AS RT_TYPE"
						+ " FROM REIMBURSEMENT RB INNER JOIN USER_T UT ON RB.EMPLOYEE_ID = UT.U_ID"
						+ " INNER JOIN REIMBURSEMENT_STATUS RS ON RB.RS_ID = RS.RS_ID"
						+ " INNER JOIN REIMBURSEMENT_TYPE RT ON RB.RT_ID = RT.RT_ID"
						+ " WHERE RS.RS_STATUS = 'PENDING' AND UT.U_ID = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, employeeId);
			
			Set<Reimbursement> set = new HashSet<>();
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				set.add(new Reimbursement(
						result.getInt("R_ID"), 
						null,
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						new Employee(0, result.getString("U_FIRSTNAME"), 
								result.getString("U_LASTNAME"), null, null,
								null, null),
						null,
						new ReimbursementStatus(result.getString("RS_STATUS")),
						new ReimbursementType(result.getString("RT_TYPE"))
						));
			}
			
			return set;
		} catch (SQLException e) {
			logger.error("Selecting pending", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectFinalized(int employeeId) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT"
							+ " RB.R_ID AS R_ID, RB.R_AMOUNT AS R_AMOUNT, RB.R_DESCRIPTION AS R_DESCRIPTION,"
							+ " UT.U_FIRSTNAME AS U_FIRSTNAME, UT.U_LASTNAME AS U_LASTNAME, RS.RS_STATUS AS RS_STATUS,"
							+ " RT.RT_TYPE AS RT_TYPE"
						+ " FROM REIMBURSEMENT RB INNER JOIN USER_T UT ON RB.EMPLOYEE_ID = UT.U_ID"
						+ " INNER JOIN REIMBURSEMENT_STATUS RS ON RB.RS_ID = RS.RS_ID"
						+ " INNER JOIN REIMBURSEMENT_TYPE RT ON RB.RT_ID = RT.RT_ID"
						+ " WHERE RS.RS_STATUS != 'PENDING' AND UT.U_ID = ?";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, employeeId);
			
			Set<Reimbursement> set = new HashSet<>();
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				set.add(new Reimbursement(
						result.getInt("R_ID"), 
						null,
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						new Employee(0, result.getString("U_FIRSTNAME"), 
								result.getString("U_LASTNAME"), null, null,
								null, null),
						null,
						new ReimbursementStatus(result.getString("RS_STATUS")),
						new ReimbursementType(result.getString("RT_TYPE"))
						));
			}
			
			return set;
		} catch (SQLException e) {
			logger.error("Selecting finalized", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllPending() {
		logger.trace("Getting all pending reimbursement");
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT"
							+ " RB.R_ID AS R_ID, RB.R_AMOUNT AS R_AMOUNT, RB.R_DESCRIPTION AS R_DESCRIPTION,"
							+ " UT.U_FIRSTNAME AS U_FIRSTNAME, UT.U_LASTNAME AS U_LASTNAME, RS.RS_STATUS AS RS_STATUS,"
							+ " RT.RT_TYPE AS RT_TYPE"
						+ " FROM REIMBURSEMENT RB INNER JOIN USER_T UT ON RB.EMPLOYEE_ID = UT.U_ID"
						+ " INNER JOIN REIMBURSEMENT_STATUS RS ON RB.RS_ID = RS.RS_ID"
						+ " INNER JOIN REIMBURSEMENT_TYPE RT ON RB.RT_ID = RT.RT_ID"
						+ " WHERE RS.RS_STATUS = 'PENDING'";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			Set<Reimbursement> set = new HashSet<>();
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				set.add(new Reimbursement(
						result.getInt("R_ID"), 
						null,
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						new Employee(0, result.getString("U_FIRSTNAME"), 
								result.getString("U_LASTNAME"), null, null,
								null, null),
						null,
						new ReimbursementStatus(result.getString("RS_STATUS")),
						new ReimbursementType(result.getString("RT_TYPE"))
						));
			}
			
			logger.trace(set);
			return set;
		} catch (SQLException e) {
			logger.error("Selecting all pending", e);
		}
		return null;
	}

	@Override
	public Set<Reimbursement> selectAllFinalized() {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT"
							+ " RB.R_ID AS R_ID, RB.R_AMOUNT AS R_AMOUNT, RB.R_DESCRIPTION AS R_DESCRIPTION,"
							+ " UT.U_FIRSTNAME AS U_FIRSTNAME, UT.U_LASTNAME AS U_LASTNAME, RS.RS_STATUS AS RS_STATUS,"
							+ " RT.RT_TYPE AS RT_TYPE"
						+ " FROM REIMBURSEMENT RB INNER JOIN USER_T UT ON RB.EMPLOYEE_ID = UT.U_ID"
						+ " INNER JOIN REIMBURSEMENT_STATUS RS ON RB.RS_ID = RS.RS_ID"
						+ " INNER JOIN REIMBURSEMENT_TYPE RT ON RB.RT_ID = RT.RT_ID"
						+ " WHERE RS.RS_STATUS != 'PENDING'";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			Set<Reimbursement> set = new HashSet<>();
			
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				set.add(new Reimbursement(
						result.getInt("R_ID"), 
						null,
						null,
						result.getDouble("R_AMOUNT"),
						result.getString("R_DESCRIPTION"),
						new Employee(0, result.getString("U_FIRSTNAME"), 
								result.getString("U_LASTNAME"), null, null,
								null, null),
						null,
						new ReimbursementStatus(result.getString("RS_STATUS")),
						new ReimbursementType(result.getString("RT_TYPE"))
						));
			}
			
			return set;
		} catch (SQLException e) {
			logger.error("Selecting all finalized", e);
		}
		
		return null;
	}

	@Override
	public Set<ReimbursementType> selectTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public ReimbursementType getReimbursementType(String type) {
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "SELECT RT_ID, RT_TYPE FROM REIMBURSEMENT_TYPE WHERE RT_TYPE = ?";
			
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, type);
			
			ResultSet result = statement.executeQuery();
			
			if(result.next()) {
				return new ReimbursementType(result.getInt("RT_ID"), result.getString("RT_TYPE"));
			}
		} catch (SQLException e) {
			logger.error("Getting reimbursement type id", e);
		}
		
		return null;
	}
}
