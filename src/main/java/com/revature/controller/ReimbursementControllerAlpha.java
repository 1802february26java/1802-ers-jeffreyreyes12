package com.revature.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.service.ConcreteReimbursementService;

public class ReimbursementControllerAlpha implements ReimbursementController {

	@Override
	public Object submitRequest(HttpServletRequest request) {
		if (request.getMethod().equals("GET")) {
			return "reimbursement.html";
		}
		
		if (request.getSession(false) == null) {
			return "login.do";
		}
		
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		int rb_id;
		
		String type = request.getParameter("type");
		if (type.equals("OTHER")) {
			rb_id = 1;
		} else if (type.equals("COURSE")) {
			rb_id = 2;
		} else if (type.equals("CERTIFICATION")) {
			rb_id = 3;
		} else if (type.equals("TRAVELING")) {
			rb_id = 4;
		} else {
			return new ClientMessage("Invalid type");
		}
		
		boolean saved = ConcreteReimbursementService.getInstance().submitRequest(
				new Reimbursement(0, 
						LocalDateTime.now(), 
						null, 
						Double.parseDouble(request.getParameter("amount")),
						request.getParameter("description"),
						loggedEmployee,
						null,
						new ReimbursementStatus(1, "PENDING"),
						new ReimbursementType(rb_id, type)));
		
		if (saved) {
			return new ClientMessage("Reimbursement submission successful");
		}
		
		return new ClientMessage("Reimbursement submission failed");
	}

	@Override
	public Object singleRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object multipleRequests(HttpServletRequest request) {
		if (request.getSession(false) == null) { 
			return "login.do";
		}
		
		String fetch = request.getParameter("fetch");
		
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		if (loggedEmployee.getEmployeeRole().getType().equals("EMPLOYEE")) {
			if (fetch.equals("pending")) { 
				return ConcreteReimbursementService.getInstance().getUserPendingRequests(loggedEmployee);
			} else {
				return ConcreteReimbursementService.getInstance().getUserFinalizedRequests(loggedEmployee);
			}
		} else {
			if (fetch.equals("pending")) { 
				return ConcreteReimbursementService.getInstance().getAllPendingRequests();
			} else {
				return ConcreteReimbursementService.getInstance().getAllResolvedRequests();
			}
		}
	}

	@Override
	public Object finalizeRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRequestTypes(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
