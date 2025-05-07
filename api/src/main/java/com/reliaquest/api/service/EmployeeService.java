package com.reliaquest.api.service;

import com.reliaquest.api.exception.EmployeeAPIError;
import com.reliaquest.api.exception.EmployeeApiException;
import com.reliaquest.api.external.EmployeeWebClient;
import com.reliaquest.api.model.Employee;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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


    public List<Employee> getEmployeesByNameSearch(String employeeName) {
        var filteredEmployees = employeeWebClient.getAllEmployees().data()
                .stream()
                .filter(employee -> employee.name().toLowerCase().contains(employeeName.toLowerCase()))
                .toList();
        if (filteredEmployees.isEmpty()) {
            log.debug("No employees found with name: {}", employeeName);
            throw new EmployeeApiException(EmployeeAPIError.NO_EMPLOYEES_FOUND);
        }
        log.info("Successfully fetched employees with name: {}: {}", employeeName, filteredEmployees);
        return filteredEmployees;
    }


    public Employee getEmployeeById(String id) {
        if (StringUtils.isEmpty(id)) {
            log.debug("Employee ID cannot be null or empty");
            throw new EmployeeApiException(EmployeeAPIError.ID_CAN_NOT_BE_NULL);
        }

        return employeeWebClient.getEmployeeById(id).data()
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                    log.debug("No employee found with ID: {}", id);
                    return new EmployeeApiException(EmployeeAPIError.NO_EMPLOYEES_FOUND);
                });
    }

    public Integer getEmployeeWithHighestSalary() {
        return employeeWebClient.getAllEmployees().data()
                .stream()
                .map(Employee::salary)
                .max(Integer::compareTo)
                .orElseThrow(() -> {
                    log.debug("No employees found");
                    return new EmployeeApiException(EmployeeAPIError.NO_EMPLOYEES_FOUND);
                });
    }

    public List<String> getTopTenHighestEarningEmployees() {
        final List<Employee> employees = employeeWebClient.getAllEmployees().data();
        if(employees.size() < 10) {
            log.debug("Less than 10 employees found");
            throw new EmployeeApiException(EmployeeAPIError.NOT_SUFFICIENT_EMPLOYEES);
        }
        log.info("Successfully fetched top 10 highest earning employees");
        return employees
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.salary(), e1.salary()))
                .limit(10)
                .map(Employee::name)
                .toList();
    }
}
