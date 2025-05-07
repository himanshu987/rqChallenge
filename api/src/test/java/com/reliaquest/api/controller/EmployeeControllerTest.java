package com.reliaquest.api.controller;

import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployees;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.reliaquest.api.model.EmployeeRequest;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.ValidationException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {
    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Test
    void shouldGetAllEmployees() {
        when(employeeService.getAllEmployees()).thenReturn(defaultEmployees());

        var response = employeeController.getAllEmployees();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetEmployeesByNameSearch() {
        String searchString = "John";
        when(employeeService.getEmployeesByNameSearch(searchString)).thenReturn(defaultEmployees());

        var response = employeeController.getEmployeesByNameSearch(searchString);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetEmployeeByID() {
        String id = "1";
        when(employeeService.getEmployeeById(id)).thenReturn(defaultEmployees().get(0));

        var response = employeeController.getEmployeeById(id);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetHighestSalaryOfEmployees() {
        when(employeeService.getEmployeeWithHighestSalary()).thenReturn(100000);

        var response = employeeController.getHighestSalaryOfEmployees();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldGetTopTenHighestEarningEmployeeNames() {
        when(employeeService.getTopTenHighestEarningEmployees()).thenReturn(List.of("John Doe", "Jane Smith"));

        var response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldCreateEmployee() {
        var employeeRequest = new EmployeeRequest("John Doe", 50000, 17, "Engineer", "abc@gmail.com");
        when(employeeService.createEmployee(employeeRequest))
                .thenReturn(defaultEmployees().get(0));

        var response = employeeController.createEmployee(employeeRequest);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldThrowValidationErrorWhenCreatingEmployeeIfSalaryIsLessThan0() {
        var employeeRequest = new EmployeeRequest("John Doe", -1, 1, "Engineer", "abc@gmail.com");
        assertThrows(ValidationException.class, () -> employeeController.createEmployee(employeeRequest));
    }

    @Test
    void shouldThrowValidationErrorWhenCreatingEmployeeIfAgeIsLessThan16() {
        var employeeRequest = new EmployeeRequest("John Doe", 1000, 15, "Engineer", "abc@gmail.com");
        assertThrows(ValidationException.class, () -> employeeController.createEmployee(employeeRequest));
    }

    @Test
    void shouldThrowValidationErrorWhenCreatingEmployeeIfAgeIsGreaterThan75() {
        var employeeRequest = new EmployeeRequest("John Doe", 1000, 80, "Engineer", "abc@gmail.com");
        assertThrows(ValidationException.class, () -> employeeController.createEmployee(employeeRequest));
    }

    @Test
    void shouldDeleteEmployeeById() {
        String id = "1";
        when(employeeService.deleteEmployeeById(id)).thenReturn("Success");

        var response = employeeController.deleteEmployeeById(id);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Success");
    }
}
