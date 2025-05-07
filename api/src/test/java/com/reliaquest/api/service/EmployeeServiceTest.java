package com.reliaquest.api.service;

import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployee;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeResponse;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeResponseWithId;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeWithId;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployees;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeesResponse;
import static com.reliaquest.api.helper.EmployeeDefaults.emptyEmployeeResponse;
import static com.reliaquest.api.helper.EmployeeDefaults.emptyEmployeesResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.reliaquest.api.exception.EmployeeAPIError;
import com.reliaquest.api.exception.EmployeeApiException;
import com.reliaquest.api.external.EmployeeWebClient;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeDeleteResponse;
import com.reliaquest.api.model.EmployeeRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeWebClient employeeWebClient;

    @Test
    void shouldGetAllEmployees() {
        var mockResponse = defaultEmployeesResponse();
        when(employeeWebClient.getAllEmployees()).thenReturn(mockResponse);

        var result = employeeService.getAllEmployees();

        assertThat(result).isEqualTo(defaultEmployees());
        verify(employeeWebClient, times(1)).getAllEmployees();
    }

    @Test
    void shouldFallbackWhenExceptionOccurs() {
        when(employeeWebClient.getAllEmployees()).thenThrow(new RuntimeException("API error"));

        assertThrows(RuntimeException.class, () -> employeeService.getAllEmployees());
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeesFound() {
        when(employeeWebClient.getAllEmployees()).thenReturn(emptyEmployeesResponse());

        EmployeeApiException exception =
                assertThrows(EmployeeApiException.class, () -> employeeService.getAllEmployees());
        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NO_EMPLOYEES_FOUND);
    }

    @Test
    void shouldReturnFilteredEmployeesWhenNameMatches() {
        final String employeeName = "John Doe";
        var mockResponse = defaultEmployeesResponse();
        when(employeeWebClient.getAllEmployees()).thenReturn(mockResponse);

        var result = employeeService.getEmployeesByNameSearch(employeeName);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(employee -> employee.name().toLowerCase().contains(employeeName.toLowerCase()));
        verify(employeeWebClient, times(1)).getAllEmployees();
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeesMatchName() {
        var mockResponse = defaultEmployeesResponse();
        when(employeeWebClient.getAllEmployees()).thenReturn(mockResponse);

        EmployeeApiException exception = assertThrows(
                EmployeeApiException.class, () -> employeeService.getEmployeesByNameSearch("NonExistentName"));

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NO_EMPLOYEES_FOUND);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullOrEmpty() {
        EmployeeApiException exception =
                assertThrows(EmployeeApiException.class, () -> employeeService.getEmployeeById(""));

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.ID_CAN_NOT_BE_NULL);
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeeFoundWithId() {
        String employeeId = "123";
        when(employeeWebClient.getEmployeeById(employeeId)).thenReturn(emptyEmployeeResponse());

        EmployeeApiException exception =
                assertThrows(EmployeeApiException.class, () -> employeeService.getEmployeeById(employeeId));

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NO_EMPLOYEES_FOUND);
    }

    @Test
    void shouldReturnEmployeeWhenFoundById() {
        String employeeId = "1";
        when(employeeWebClient.getEmployeeById(employeeId)).thenReturn(defaultEmployeeResponse());

        Employee result = employeeService.getEmployeeById(employeeId);

        assertThat(result).isEqualTo(defaultEmployeeWithId(employeeId));
    }

    @Test
    void shouldReturnHighestSalaryWhenEmployeesExist() {
        when(employeeWebClient.getAllEmployees()).thenReturn(defaultEmployeesResponse());
        final int expectedHighestSalary = 80000;

        Integer highestSalary = employeeService.getEmployeeWithHighestSalary();

        assertThat(highestSalary).isEqualTo(expectedHighestSalary);
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeesFoundWhenFetchingForHighestSalary() {
        when(employeeWebClient.getAllEmployees()).thenReturn(emptyEmployeesResponse());

        EmployeeApiException exception =
                assertThrows(EmployeeApiException.class, () -> employeeService.getEmployeeWithHighestSalary());

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NO_EMPLOYEES_FOUND);
    }

    @Test
    void shouldReturnTopTenHighestEarningEmployees() {
        final String[] expectedTopTenEmployees = {
            "Diana Evans",
            "Jack Wilson",
            "Charlie Davis",
            "John Doe",
            "George King",
            "Jane Smith",
            "Alice Johnson",
            "Karen White",
            "Fiona Green",
            "John Doe"
        };
        when(employeeWebClient.getAllEmployees()).thenReturn(defaultEmployeesResponse());

        List<String> topTenEmployees = employeeService.getTopTenHighestEarningEmployees();

        assertThat(topTenEmployees).hasSize(10);
        assertThat(topTenEmployees).containsExactlyInAnyOrder(expectedTopTenEmployees);
    }

    @Test
    void shouldThrowExceptionWhenLessThanTenEmployees() {
        when(employeeWebClient.getAllEmployees()).thenReturn(defaultEmployeeResponseWithId("1"));

        EmployeeApiException exception =
                assertThrows(EmployeeApiException.class, () -> employeeService.getTopTenHighestEarningEmployees());

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NOT_SUFFICIENT_EMPLOYEES);
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {
        EmployeeRequest employeeRequest =
                new EmployeeRequest("John Doe", 50000, 30, "Engineer", "john.doe@example.com");
        Employee expectedEmployee = defaultEmployee();
        when(employeeWebClient.createEmployee(employeeRequest)).thenReturn(defaultEmployeeResponse());

        Employee result = employeeService.createEmployee(employeeRequest);

        assertThat(result).isEqualTo(expectedEmployee);
        verify(employeeWebClient, times(1)).createEmployee(employeeRequest);
    }

    @Test
    void shouldDeleteEmployeeByIdSuccessfully() {
        String employeeId = "123";
        String expectedStatus = "success";
        when(employeeWebClient.deleteEmployeeById(employeeId)).thenReturn(new EmployeeDeleteResponse(true, "success"));

        String result = employeeService.deleteEmployeeById(employeeId);

        assertThat(result).isEqualTo(expectedStatus);
        verify(employeeWebClient, times(1)).deleteEmployeeById(employeeId);
    }

    @Test
    void shouldThrowExceptionWhenDeleteEmployeeByIdFails() {
        String employeeId = "123";
        when(employeeWebClient.deleteEmployeeById(employeeId)).thenThrow(new RuntimeException("API error"));

        assertThrows(RuntimeException.class, () -> employeeService.deleteEmployeeById(employeeId));
    }
}
