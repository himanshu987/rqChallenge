package com.reliaquest.api.model;

import lombok.Builder;

@Builder
public record EmployeeResponse(Employee data, String status) {}
