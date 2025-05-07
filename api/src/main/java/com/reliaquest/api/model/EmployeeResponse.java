package com.reliaquest.api.model;

import java.util.List;
import lombok.Builder;

@Builder
public record EmployeeResponse(List<Employee> data, String status) {}
