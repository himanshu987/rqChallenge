package com.reliaquest.api.helper;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeResponse;

import java.util.List;

public class EmployeeDefaults {
    public static List<Employee> defaultEmployees(){
        return List.of(Employee.builder()
                .id("1")
                .name("John Doe")
                .salary(50000)
                .age(30)
                .title("Software Engineer")
                .email("john.doe@example.com")
                .build());
    }

    public static EmployeeResponse defaultEmployeeResponse(){
        return EmployeeResponse.builder()
                .data(defaultEmployees())
                .status("success")
                .build();
    }


}
