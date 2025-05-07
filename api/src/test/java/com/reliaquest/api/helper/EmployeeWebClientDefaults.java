package com.reliaquest.api.helper;

public class EmployeeWebClientDefaults {
    public static String defaultClientResponse(){
        return """
                {
                  "data": [
                    {
                      "id": "1",
                      "employee_name": "John Doe",
                      "employee_salary": 50000,
                      "employee_age": 30,
                      "employee_title": "Software Engineer",
                      "employee_email": "john.doe@example.com"
                    }
                  ],
                  "status": "success"
                }""";
    }
}
