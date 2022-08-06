package com.example.testingdemo;

import com.example.testingdemo.model.Employee;
import com.example.testingdemo.repository.EmployeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestingDemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(TestingDemoApplication.class, args);
		
		// to add new employee when app starts
		
//		EmployeeRepository employeeRepository = context.getBean(EmployeeRepository.class);
//		
//		Employee employee = new Employee();
//		employee.setFileName("April");
//		employee.setLastName("White");
//
//		employeeRepository.save(employee);
	}

}
