package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.model.Employee;
import com.revature.service.ConcreteEmployeeService;
import com.revature.ajax.ClientMessage;

public class LoginControllerAlpha implements LoginController {

	@Override
	public Object login(HttpServletRequest request) {
		if (request.getMethod().equals("GET")) {
			return "login.html";
		}
		
		Employee loggedEmployee = ConcreteEmployeeService.getInstance().authenticate(
				new Employee(request.getParameter("username"), 
						request.getParameter("password")));
		
		if (loggedEmployee == null) {
			return new ClientMessage("AUTHENTICATION FAILED");
		}
		
		request.getSession().setAttribute("loggedEmployee", loggedEmployee);
		
		return loggedEmployee;
	}

	@Override
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "login.html";
	}

}
