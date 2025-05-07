package com.reliaquest.api.exception;

import lombok.Getter;

@Getter
public enum EmployeeAPIError {
    RETRY_ERROR("E-1","Failed to get response");

    private final String code;
    private final String message;

    EmployeeAPIError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
