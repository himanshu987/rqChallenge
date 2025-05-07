package com.reliaquest.api.exception;

import lombok.Getter;

@Getter
public enum EmployeeAPIError {
    RETRY_ERROR("E-1", "Failed to get response"),
    NO_EMPLOYEES_FOUND("E-2", "No Employees found"),
    ID_CAN_NOT_BE_NULL("E-3", "ID can not be null"),
    NOT_SUFFICIENT_EMPLOYEES("E-4", "Not sufficient employees found"),
    MINIMUM_SALARY_ERROR("E-5", "Salary cannot be less than zero"),
    MINIMUM_AGE_ERROR("E-6", "Age cannot be less than 16"),
    MAXIMUM_AGE_ERROR("E-7", "Age cannot be greater than 75"),
    CREATION_FAILED("E-8", "Employee creation failed"),
    DELETION_FAILED("E-9", "Employee deletion failed");

    private final String code;
    private final String message;

    EmployeeAPIError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
