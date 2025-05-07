package com.reliaquest.api.service;

import com.reliaquest.api.exception.EmployeeAPIError;
import com.reliaquest.api.exception.EmployeeApiException;
import com.reliaquest.api.external.EmployeeWebClient;
import com.reliaquest.api.model.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeResponse;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeResponseWithId;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeWithId;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployees;
import static com.reliaquest.api.helper.EmployeeDefaults.emptyEmployeeResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeWebClient employeeWebClient;

    @Test
    void shouldGetAllEmployees() {
        var mockResponse = defaultEmployeeResponse();
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
        when(employeeWebClient.getAllEmployees()).thenReturn(emptyEmployeeResponse());

        EmployeeApiException exception = assertThrows(EmployeeApiException.class, () -> employeeService.getAllEmployees());
        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NO_EMPLOYEES_FOUND);
    }

    @Test
    void shouldReturnFilteredEmployeesWhenNameMatches() {
        final String employeeName = "John Doe";
        var mockResponse = defaultEmployeeResponse();
        when(employeeWebClient.getAllEmployees()).thenReturn(mockResponse);

        var result = employeeService.getEmployeesByNameSearch(employeeName);

        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(employee -> employee.name().toLowerCase().contains(employeeName.toLowerCase()));
        verify(employeeWebClient, times(1)).getAllEmployees();
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeesMatchName() {
        var mockResponse = defaultEmployeeResponse();
        when(employeeWebClient.getAllEmployees()).thenReturn(mockResponse);

        EmployeeApiException exception = assertThrows(EmployeeApiException.class,
                () -> employeeService.getEmployeesByNameSearch("NonExistentName"));

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NO_EMPLOYEES_FOUND);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullOrEmpty() {
        EmployeeApiException exception = assertThrows(EmployeeApiException.class,
                () -> employeeService.getEmployeeById(""));

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.ID_CAN_NOT_BE_NULL);
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeeFoundWithId() {
        String employeeId = "123";
        when(employeeWebClient.getEmployeeById(employeeId)).thenReturn(emptyEmployeeResponse());

        EmployeeApiException exception = assertThrows(EmployeeApiException.class,
                () -> employeeService.getEmployeeById(employeeId));

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NO_EMPLOYEES_FOUND);
    }

    @Test
    void shouldReturnEmployeeWhenFoundById() {
        String employeeId = "123";
        when(employeeWebClient.getEmployeeById(employeeId)).thenReturn(defaultEmployeeResponseWithId(employeeId));

        Employee result = employeeService.getEmployeeById(employeeId);

        assertThat(result).isEqualTo(defaultEmployeeWithId(employeeId));
    }

    @Test
    void shouldReturnHighestSalaryWhenEmployeesExist() {
        when(employeeWebClient.getAllEmployees()).thenReturn(defaultEmployeeResponse());
        final int expectedHighestSalary = 80000;

        Integer highestSalary = employeeService.getEmployeeWithHighestSalary();

        assertThat(highestSalary).isEqualTo(expectedHighestSalary);
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeesFoundWhenFetchingForHighestSalary() {
        when(employeeWebClient.getAllEmployees()).thenReturn(emptyEmployeeResponse());

        EmployeeApiException exception = assertThrows(EmployeeApiException.class,
                () -> employeeService.getEmployeeWithHighestSalary());

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NO_EMPLOYEES_FOUND);
    }

    @Test
    void shouldReturnTopTenHighestEarningEmployees() {
        var employees = defaultEmployees();
        when(employeeWebClient.getAllEmployees()).thenReturn(defaultEmployeeResponse());

        List<String> topTenEmployees = employeeService.getTopTenHighestEarningEmployees();

        assertThat(topTenEmployees).hasSize(10);
        assertThat(topTenEmployees)
                .isSortedAccordingTo((e1, e2) -> Integer.compare(
                        employees.stream().filter(emp -> emp.name().equals(e1)).findFirst().get().salary(),
                        employees.stream().filter(emp -> emp.name().equals(e2)).findFirst().get().salary()
                ));
    }

    @Test
    void shouldThrowExceptionWhenLessThanTenEmployees() {
        when(employeeWebClient.getAllEmployees()).thenReturn(defaultEmployeeResponseWithId("1"));

        EmployeeApiException exception = assertThrows(EmployeeApiException.class,
                () -> employeeService.getTopTenHighestEarningEmployees());

        assertThat(exception.getError()).isEqualTo(EmployeeAPIError.NOT_SUFFICIENT_EMPLOYEES);
    }
}
