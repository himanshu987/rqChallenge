package com.reliaquest.api.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.Employee;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeWebClientDefaults {
    public static String buildJsonResponse(List<Employee> employees) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();
        response.put(
                "data",
                employees.stream()
                        .map(EmployeeWebClientDefaults::buildEmployeeData)
                        .toList());
        response.put("status", "success");
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }

    public static String buildJsonResponse(Employee employee) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();
        response.put("data", buildEmployeeData(employee));
        response.put("status", "success");
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }

    private static Map<String, Object> buildEmployeeData(Employee employee) {
        return Map.of(
                "id", employee.id(),
                "employee_name", employee.name(),
                "employee_salary", employee.salary(),
                "employee_age", employee.age(),
                "employee_title", employee.title(),
                "employee_email", employee.email());
    }
}
