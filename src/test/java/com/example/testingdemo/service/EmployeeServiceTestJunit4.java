package com.example.testingdemo.service;

import com.example.testingdemo.employeeproj.dto.EmployeeDtoResponse;
import com.example.testingdemo.employeeproj.model.Employee;
import com.example.testingdemo.employeeproj.repository.EmployeeRepository;
import com.example.testingdemo.employeeproj.service.EmployeeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTestJunit4 {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl serviceUnderTest;

    @Test
    public void getEmployee() {
        EmployeeDtoResponse employeeExpected = createEmployeeResponse();
        Employee employee = createEmployee();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
//        when(modelMapper.map(employee, EmployeeDtoResponse.class)).thenReturn(employeeExpected);

//        EmployeeDtoResponse response = serviceUnderTest.getEmployee(1, true);
        serviceUnderTest.getEmployee(1, true);

        verify(employeeRepository, only()).findById(1L);

//        assertEquals(response.getFirstName(), employeeExpected.getFirstName());
//        assertEquals(response.getLastName(), employeeExpected.getLastName());
    }

    @Test
    public void getEmployee_2() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(createEmployee()));

        serviceUnderTest.getEmployee(1, true);

        verify(employeeRepository, only()).findById(1L);
    }

    private EmployeeDtoResponse createEmployeeResponse() {
        EmployeeDtoResponse employeeDtoResponse = new EmployeeDtoResponse();
        employeeDtoResponse.setFirstName("FirstName");
        employeeDtoResponse.setLastName("LastName");

        return employeeDtoResponse;
    }

    private Employee createEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("FirstName");
        employee.setLastName("LastName");
        employee.setEmail("test@gm.com");

        return employee;
    }
}