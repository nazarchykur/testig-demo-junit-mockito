package com.example.testingdemo.service;

import com.example.testingdemo.dto.EmployeeDtoRequest;
import com.example.testingdemo.dto.EmployeeDtoResponse;
import com.example.testingdemo.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee);

    EmployeeDtoResponse create(EmployeeDtoRequest employeeDtoRequest);

    Employee getEmployee(Integer id);
    
    EmployeeDtoResponse getEmployee(Integer id, boolean withResponseEntity);
    EmployeeDtoResponse getEmployee(Integer id, boolean withResponseEntity, boolean handleException);

    List<Employee> getEmployees();
}
