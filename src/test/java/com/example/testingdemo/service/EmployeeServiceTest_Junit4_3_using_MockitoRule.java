package com.example.testingdemo.service;

import com.example.testingdemo.model.Employee;
import com.example.testingdemo.repository.EmployeeRepository;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
https://junit.org/junit4/javadoc/4.12/org/junit/rules/TestRule.html

org.junit.rules
    Interface TestRule
    
All Known Implementing Classes:
    DisableOnDebug, ErrorCollector, ExpectedException, ExternalResource, RuleChain, Stopwatch, TemporaryFolder, TestName, TestWatcher, Timeout, Verifier
    
    - ErrorCollector: collect multiple errors in one test method
    - ExpectedException: make flexible assertions about thrown exceptions
    - ExternalResource: start and stop a server, for example
    - TemporaryFolder: create fresh files, and delete after test
    - TestName: remember the test name for use during the method
    - TestWatcher: add logic at events during method execution
    - Timeout: cause test to fail after a set time
    - Verifier: fail test if object state ends up incorrect
    
    
    
    JUnit Rules allow you to write code to do some before and after work. 
    Thus, you don’t repeat to write the same code in various test classes. 
    They are very useful to add more functionalities to all test methods in a test class. 
    You can extend or reuse provided rules or write your own custom rules.
    

 */

public class EmployeeServiceTest_Junit4_3_using_MockitoRule {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

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