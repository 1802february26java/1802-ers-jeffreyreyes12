package com.revature.controller;

import javax.servlet.http.HttpServletRequest;

import com.revature.model.Employee;

public class HomeControllerAlpha implements HomeController {

	@Override
	public String showEmployeeHome(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if (request.getMethod().equals("GET")) {
			//Check that user is logged.
			if (request.getSession(false) == null) { 
				return "login.do";
			}
			
			//Check employee role to determine homepage
			Employee loggedEmployee = (Employee) request.getSession().getAttribute("loggedEmployee");
			if (loggedEmployee.getEmployeeRole().getType().equals("EMPLOYEE")) {
				return "employee-home.html";
			} else {
				return "manager-home.html";
			}
		}
		
		return null;
	}

}
