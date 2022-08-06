package com.example.testingdemo.service;

import com.example.testingdemo.model.Employee;
import com.example.testingdemo.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest_Junit4_4_using_MockitoJUnitRunner {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl serviceUnderTest;

    
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