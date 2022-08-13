package com.example.testingdemo.employeeproj.service;

import com.example.testingdemo.employeeproj.dto.EmployeeDtoRequest;
import com.example.testingdemo.employeeproj.dto.EmployeeDtoResponse;
import com.example.testingdemo.employeeproj.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);

    EmployeeDtoResponse create(EmployeeDtoRequest employeeDtoRequest);

    Employee getEmployee(Integer id);
    
    EmployeeDtoResponse getEmployee(Integer id, boolean withResponseEntity);
    EmployeeDtoResponse getEmployee(Integer id, boolean withResponseEntity, boolean handleException);

    List<Employee> getEmployees();
}
