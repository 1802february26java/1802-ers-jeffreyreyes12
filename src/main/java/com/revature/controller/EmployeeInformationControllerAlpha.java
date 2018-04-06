package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.ajax.ClientMessage;
import com.revature.model.Employee;
import com.revature.service.ConcreteEmployeeService;

public class EmployeeInformationControllerAlpha implements EmployeeInformationController {

	@Override
	public Object registerEmployee(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object updateEmployee(HttpServletRequest request) {
		if (request.getSession(false) == null) { 
			return "login.do";
		}
		
		Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
		
		if (request.getMethod().equals("GET")) {
			
			if (loggedEmployee.getEmployeeRole().getType().equals("EMPLOYEE")) {
				return "edit-info.html";
			} else {
				return "404.html";
			}
		}
		
		loggedEmployee.setLastName(request.getParameter("lastname"));
		loggedEmployee.setFirstName(request.getParameter("firstname"));
		loggedEmployee.setEmail(request.getParameter("email"));
		
		if (ConcreteEmployeeService.getInstance().updateEmployeeInformation(loggedEmployee)) {
			return new ClientMessage("Edit success");
		}
		
		return new ClientMessage("Edit failure");
	}

	@Override
	public Object viewEmployeeInformation(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object viewAllEmployees(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object usernameExists(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
