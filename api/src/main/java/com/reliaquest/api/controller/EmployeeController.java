package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeRequest;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class EmployeeController implements IEmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Fetching all employees");
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        log.info("Fetching all employees whose name contains : {}", searchString);
        return new ResponseEntity<>(employeeService.getEmployeesByNameSearch(searchString), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        log.info("Fetching employee with id : {}", id);
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("Fetching highest salary among all employees ");
        return new ResponseEntity<>(employeeService.getEmployeeWithHighestSalary(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("Fetching top ten highest salaried employees");
        return new ResponseEntity<>(employeeService.getTopTenHighestEarningEmployees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@NonNull @RequestBody Object employeeRequest) {
        log.info("Creating employee");
        ObjectMapper objectMapper = new ObjectMapper();
        EmployeeRequest request = objectMapper.convertValue(employeeRequest, EmployeeRequest.class);
        request.validate();
        return new ResponseEntity<>(employeeService.createEmployee(request), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(@NotBlank String id) {
        log.info("Deleting employee with id : {}", id);
        final String status = employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
