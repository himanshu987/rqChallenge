package com.reliaquest.api.external;

import com.reliaquest.api.model.EmployeeResponse;
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

    public EmployeeResponse getAllEmployees() {
        return webClient
                .get()
                .uri(EMPLOYEE_URI)
                .retrieve()
                .bodyToMono(EmployeeResponse.class)
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
}
