package com.example.testingdemo.service;

import com.example.testingdemo.model.Employee;
import com.example.testingdemo.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest_Junit4_1_using_manualMock {
    private ModelMapper modelMapper;
    private EmployeeRepository employeeRepository;
    private EmployeeServiceImpl serviceUnderTest;

    @Before
    public void setUp() {
        modelMapper = mock(ModelMapper.class);
        employeeRepository = mock(EmployeeRepository.class);

        serviceUnderTest = new EmployeeServiceImpl(employeeRepository, modelMapper);
    }
    
    @Test
    public void getEmployee_2() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(createEmployee()));

        serviceUnderTest.getEmployee(1, true);

        verify(employeeRepository, only()).findById(1L);
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