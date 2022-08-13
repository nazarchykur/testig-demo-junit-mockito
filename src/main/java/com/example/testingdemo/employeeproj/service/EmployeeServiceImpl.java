package com.example.testingdemo.employeeproj.service;

import com.example.testingdemo.employeeproj.dto.EmployeeDtoRequest;
import com.example.testingdemo.employeeproj.dto.EmployeeDtoResponse;
import com.example.testingdemo.employeeproj.exception.NoSuchElementFoundException;
import com.example.testingdemo.employeeproj.model.Employee;
import com.example.testingdemo.employeeproj.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    @Override
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public EmployeeDtoResponse create(EmployeeDtoRequest employeeDtoRequest) {
        Employee employee = modelMapper.map(employeeDtoRequest, Employee.class);
        Employee saveEmployee = employeeRepository.save(employee);
        return modelMapper.map(saveEmployee, EmployeeDtoResponse.class);
    }

    @Override
    public Employee getEmployee(Integer id) {
        return employeeRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("There is no employee with such id=" + id));
    }

    @Override
    public EmployeeDtoResponse getEmployee(Integer id, boolean withResponseEntity) {
        if (!withResponseEntity) {
            throw new RuntimeException("The queryParameter withResponseEntity is false, but must be true");
        }
        Employee employee = employeeRepository.findById(Long.valueOf(id)).orElseThrow(() -> new RuntimeException("There is no employee with such id=" + id));
        return modelMapper.map(employee, EmployeeDtoResponse.class);
    }

    @Override
    public EmployeeDtoResponse getEmployee(Integer id, boolean withResponseEntity, boolean handleException) {
        if (!withResponseEntity) {
            throw new NoSuchElementFoundException("The queryParameter withResponseEntity is false, but must be true");
        }
        if (!handleException) {
            throw new NoSuchElementFoundException("The queryParameter handleException is false, but must be true");
        }

        Employee employee = employeeRepository.findById(Long.valueOf(id)).orElseThrow(() -> new NoSuchElementFoundException("There is no employee with such id=" + id));
        return modelMapper.map(employee, EmployeeDtoResponse.class);
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

}
