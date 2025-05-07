package com.reliaquest.api.model;

import com.reliaquest.api.exception.EmployeeAPIError;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRequest(
        @NotBlank String name,
        @NotBlank Integer salary,
        @NotBlank Integer age,
        @NotBlank String title,
        @NotBlank String email) {
    public void validate() {
        if (this.salary < 0) {
            throw new ValidationException(String.valueOf(EmployeeAPIError.MINIMUM_SALARY_ERROR));
        }
        if (this.age < 16) {
            throw new ValidationException(String.valueOf(EmployeeAPIError.MINIMUM_AGE_ERROR));
        } else if (this.age > 75) {
            throw new ValidationException(String.valueOf(EmployeeAPIError.MAXIMUM_AGE_ERROR));
        }
    }
}
