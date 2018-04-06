package com.revature.service;

import java.util.Set;

import com.revature.model.Employee;
import com.revature.model.EmployeeToken;
import com.revature.repository.EmployeeRepositoryJdbc;

public class ConcreteEmployeeService implements EmployeeService {
	
	private static EmployeeService employeeService = new ConcreteEmployeeService();
	
	private ConcreteEmployeeService() {}
	
	public static EmployeeService getInstance() {
		return employeeService;
	}

	@Override
	public Employee authenticate(Employee employee) {
		Employee loggedEmployee = EmployeeRepositoryJdbc.getInstance().select(employee.getUsername());
		if (loggedEmployee == null) {
			return null;
		}
		
		if (loggedEmployee.getPassword().equals(EmployeeRepositoryJdbc.getInstance().getPasswordHash(employee))) {
			return loggedEmployee;
		}
		
		return null;
	}

	@Override
	public Employee getEmployeeInformation(Employee employee) {
		return EmployeeRepositoryJdbc.getInstance().select(employee.getUsername());
	}

	@Override
	public Set<Employee> getAllEmployeesInformation() {
		return EmployeeRepositoryJdbc.getInstance().selectAll();
	}

	@Override
	public boolean createEmployee(Employee employee) {
		return EmployeeRepositoryJdbc.getInstance().insert(employee);
	}

	@Override
	public boolean updateEmployeeInformation(Employee employee) {
		// TODO Auto-generated method stub
		return EmployeeRepositoryJdbc.getInstance().update(employee);
	}

	@Override
	public boolean updatePassword(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUsernameTaken(Employee employee) {
		Employee e = EmployeeRepositoryJdbc.getInstance().select(employee.getUsername());
		if (e == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean createPasswordToken(Employee employee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePasswordToken(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTokenExpired(EmployeeToken employeeToken) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
