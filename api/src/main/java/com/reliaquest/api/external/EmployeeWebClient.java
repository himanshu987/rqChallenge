package com.reliaquest.api.external;

import com.reliaquest.api.model.EmployeeDeleteResponse;
import com.reliaquest.api.model.EmployeeRequest;
import com.reliaquest.api.model.EmployeeResponse;
import com.reliaquest.api.model.EmployeesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EmployeeWebClient {

    public static final String EMPLOYEE_URI = "/employee";
    private final WebClient webClient;

    public EmployeeWebClient(@Value("${employee.api.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public EmployeesResponse getAllEmployees() {
        return webClient
                .get()
                .uri(EMPLOYEE_URI)
                .retrieve()
                .bodyToMono(EmployeesResponse.class)
                .block();
    }

    public EmployeeResponse getEmployeeById(String id) {
        return webClient
                .get()
                .uri(EMPLOYEE_URI + "/" + id)
                .retrieve()
                .bodyToMono(EmployeeResponse.class)
                .block();
    }

    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        return webClient
                .post()
                .uri(EMPLOYEE_URI)
                .bodyValue(employeeRequest)
                .retrieve()
                .bodyToMono(EmployeeResponse.class)
                .block();
    }

    //    Not able to send request body in delete method, according HTTP 1.1 specification request body not allowed in
    // delete method
    //    So changing path for delete method to /employee/{id}
    public EmployeeDeleteResponse deleteEmployeeById(String id) {
        return webClient
                .delete()
                .uri(EMPLOYEE_URI + "/" + id)
                .retrieve()
                .bodyToMono(EmployeeDeleteResponse.class)
                .block();
    }
}
