package com.reliaquest.api.service;

import com.reliaquest.api.external.EmployeeWebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeResponse;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployees;
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
}
