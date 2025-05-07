package com.reliaquest.api.service;

import com.reliaquest.api.exception.EmployeeAPIError;
import com.reliaquest.api.exception.EmployeeApiException;
import com.reliaquest.api.external.EmployeeWebClient;
import com.reliaquest.api.model.Employee;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeWebClient employeeWebClient;

    public EmployeeService(EmployeeWebClient employeeWebClient) {
        this.employeeWebClient = employeeWebClient;
    }

    @Retry(name = "employeeFetchRetry", fallbackMethod = "com.reliaquest.api.fallback.EmployeeServiceFallback.getAllEmployeesFallback")
    public List<Employee> getAllEmployees() {
        log.info("Getting all employees from external service");
        final var employees = Optional.ofNullable(employeeWebClient.getAllEmployees().data())
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> {
                    log.debug("No employees found");
                    return new EmployeeApiException(EmployeeAPIError.NO_EMPLOYEES_FOUND);
                });
        log.info("Successfully fetched all employees: {}", employees);
        return employees;
    }
}
