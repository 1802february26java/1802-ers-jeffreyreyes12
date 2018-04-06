package com.revature.service;

import java.util.Set;

import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementType;
import com.revature.repository.ReimbursementRepositoryJdbc;

public class ConcreteReimbursementService implements ReimbursementService {
	
	private static ReimbursementService reimbursementService = new ConcreteReimbursementService();
	
	private ConcreteReimbursementService() {}
	
	public static ReimbursementService getInstance() {
		return reimbursementService;
	}

	@Override
	public boolean submitRequest(Reimbursement reimbursement) {
		return ReimbursementRepositoryJdbc.getInstance().insert(reimbursement);
	}

	@Override
	public boolean finalizeRequest(Reimbursement reimbursement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Reimbursement getSingleRequest(Reimbursement reimbursement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Reimbursement> getUserPendingRequests(Employee employee) {
		// TODO Auto-generated method stub
		return ReimbursementRepositoryJdbc.getInstance().selectPending(employee.getId());
	}

	@Override
	public Set<Reimbursement> getUserFinalizedRequests(Employee employee) {
		// TODO Auto-generated method stub
		return ReimbursementRepositoryJdbc.getInstance().selectFinalized(employee.getId());
	}

	@Override
	public Set<Reimbursement> getAllPendingRequests() {
		return ReimbursementRepositoryJdbc.getInstance().selectAllPending();
	}

	@Override
	public Set<Reimbursement> getAllResolvedRequests() {
		return ReimbursementRepositoryJdbc.getInstance().selectAllFinalized();
	}

	@Override
	public Set<ReimbursementType> getReimbursementTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
