package com.example.testingdemo.employeeproj.repository;

import com.example.testingdemo.employeeproj.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
