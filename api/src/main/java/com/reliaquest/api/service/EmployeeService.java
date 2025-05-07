package com.reliaquest.api.service;

import com.reliaquest.api.external.EmployeeWebClient;
import com.reliaquest.api.model.Employee;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeWebClient employeeWebClient;

    public EmployeeService(EmployeeWebClient employeeWebClient) {
        this.employeeWebClient = employeeWebClient;
    }

    @Retry(name = "employeeFetchRetry", fallbackMethod = "com.reliaquest.api.fallback.EmployeeServiceFallback.getAllEmployeesFallback")
    public List<Employee> getAllEmployees() {
        log.info("Getting all employees");
        return employeeWebClient.getAllEmployees().data();
    }
}
