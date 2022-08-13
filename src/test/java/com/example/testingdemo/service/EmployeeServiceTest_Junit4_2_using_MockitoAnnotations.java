package com.example.testingdemo.service;

import com.example.testingdemo.employeeproj.model.Employee;
import com.example.testingdemo.employeeproj.repository.EmployeeRepository;
import com.example.testingdemo.employeeproj.service.EmployeeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest_Junit4_2_using_MockitoAnnotations {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl serviceUnderTest;

    @Before
    public void setUp() {
//        MockitoAnnotations.initMocks(this); // Deprecated

        MockitoAnnotations.openMocks(this);
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