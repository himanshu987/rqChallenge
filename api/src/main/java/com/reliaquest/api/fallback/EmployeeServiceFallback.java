package com.reliaquest.api.fallback;

import com.reliaquest.api.exception.EmployeeAPIError;
import com.reliaquest.api.exception.EmployeeApiException;
import com.reliaquest.api.model.Employee;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeServiceFallback {
    public static List<Employee> employeeAPIFallback(RuntimeException e) throws EmployeeApiException {
        log.error("Fallback method triggered due to exception: {}", e.getMessage());
        throw new EmployeeApiException(EmployeeAPIError.RETRY_ERROR);
    }
}
