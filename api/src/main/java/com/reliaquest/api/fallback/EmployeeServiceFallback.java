package com.reliaquest.api.fallback;

import com.reliaquest.api.exception.EmployeeAPIError;
import com.reliaquest.api.exception.EmployeeApiException;
import com.reliaquest.api.model.Employee;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class EmployeeServiceFallback {
    public static List<Employee> getAllEmployeesFallback(RuntimeException e) throws EmployeeApiException {
        log.error("Fallback method triggered due to exception: {}", e.getMessage());
        throw new EmployeeApiException(EmployeeAPIError.RETRY_ERROR);
    }
}
