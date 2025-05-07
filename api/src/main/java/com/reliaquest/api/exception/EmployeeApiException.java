package com.reliaquest.api.exception;

import lombok.Getter;

@Getter
public class EmployeeApiException extends RuntimeException {
    private final EmployeeAPIError error;

    public EmployeeApiException(EmployeeAPIError error){
        super(error.getMessage());
        this.error = error;
    }
}
