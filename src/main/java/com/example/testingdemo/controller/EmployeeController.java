package com.example.testingdemo.controller;

import com.example.testingdemo.dto.EmployeeDtoRequest;
import com.example.testingdemo.dto.EmployeeDtoResponse;
import com.example.testingdemo.model.Employee;
import com.example.testingdemo.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    @PostMapping("/v1/test")
    public String create(@RequestBody List<String> headerIds) {
        log.info("headerIds=" + headerIds);
        List<String> collect = headerIds.stream().filter(item -> item.length() > 1).collect(Collectors.toList());

        String s = collect.toString();
        return s;
    }

    @PostMapping("/v1/employee")
    public Employee create(@RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    @PostMapping("/v2/employee")
    public ResponseEntity<EmployeeDtoResponse> create(@RequestBody EmployeeDtoRequest employeeDtoRequest) {
        return new ResponseEntity<>(employeeService.create(employeeDtoRequest), HttpStatus.OK);
    }

    /*
    curl --location --request GET 'localhost:9090/v1/employee/100'
    
    response:
        {
            "timestamp": "2022-08-05T12:02:49.548+00:00",
            "status": 500,
            "error": "Internal Server Error",
            "message": "There is no employee with such id=100",
            "path": "/v1/employee/100"
         }
     */
    @GetMapping("/v1/employee/{id}")
    public Employee getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployee(id);
    }

    @GetMapping("/v2/employee/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Integer id, 
                                         @RequestParam(defaultValue = "true") boolean withResponseEntity) {
        try {
            EmployeeDtoResponse employee = employeeService.getEmployee(id, withResponseEntity);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
    curl --location --request GET 'localhost:9090/v3/employee/100?withResponseEntity=true&handleException=true'
    
    response:
        {
            "timestamp": "2022-08-05T12:09:32.373+00:00",
            "status": 404,
            "error": "Not Found",
            "message": "The queryParameter withResponseEntity is false, but must be true",
            "path": "/v3/employee/100"
        }
     */
    @GetMapping("/v3/employee/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable Integer id,
                                         @RequestParam(defaultValue = "true") boolean withResponseEntity,
                                         @RequestParam(defaultValue = "true") boolean handleException) {
        return new ResponseEntity<>(employeeService.getEmployee(id, withResponseEntity, handleException), HttpStatus.OK);
    }
    
    @GetMapping("/v1/employee")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

}
