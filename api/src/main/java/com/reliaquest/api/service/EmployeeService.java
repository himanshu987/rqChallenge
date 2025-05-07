package com.reliaquest.api.service;

import com.reliaquest.api.external.EmployeeWebClient;
import com.reliaquest.api.model.Employee;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeWebClient employeeWebClient;

    public EmployeeService(EmployeeWebClient employeeWebClient) {
        this.employeeWebClient = employeeWebClient;
    }

    public List<Employee> getAllEmployees() {
        log.info("Getting all employees");
        return employeeWebClient.getAllEmployees().data();
    }
}
