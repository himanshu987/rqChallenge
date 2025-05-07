package com.reliaquest.api.external;

import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployee;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeResponse;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployees;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeesResponse;
import static com.reliaquest.api.helper.EmployeeWebClientDefaults.buildJsonResponse;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.reliaquest.api.model.EmployeeRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class EmployeeWebClientTest {

    private static EmployeeWebClient employeeWebClient;
    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        employeeWebClient = new EmployeeWebClient("http://localhost:" + wireMockServer.port());
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void shouldCallAllEmployee() throws JsonProcessingException {
        wireMockServer.stubFor(get(urlEqualTo("/employee"))
                .willReturn(ok(buildJsonResponse(defaultEmployees()))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        var allEmployees = employeeWebClient.getAllEmployees();

        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/employee")));
        assertThat(allEmployees).isEqualTo(defaultEmployeesResponse());
    }

    @Test
    void shouldCallEmployeeById() throws JsonProcessingException {
        String employeeId = "1";
        wireMockServer.stubFor(get(urlEqualTo("/employee/" + employeeId))
                .willReturn(ok(buildJsonResponse(defaultEmployee()))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        var employee = employeeWebClient.getEmployeeById(employeeId);

        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/employee/" + employeeId)));
        assertThat(employee).isEqualTo(defaultEmployeeResponse());
    }

    @Test
    void shouldCreateEmployee() throws JsonProcessingException {
        final EmployeeRequest employeeRequest =
                new EmployeeRequest("John Doe", 50000, 30, "Software Engineer", "john.doe@example.com");
        wireMockServer.stubFor(post(urlEqualTo("/employee"))
                .willReturn(ok(buildJsonResponse(defaultEmployee()))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        var employee = employeeWebClient.createEmployee(employeeRequest);

        wireMockServer.verify(1, postRequestedFor(urlEqualTo("/employee")));
        assertThat(employee).isEqualTo(defaultEmployeeResponse());
    }

    @Test
    void shouldDeleteEmployeeById() {
        String employeeId = "1";
        wireMockServer.stubFor(delete(urlEqualTo("/employee/" + employeeId))
                .willReturn(ok("{\"data\":true,\"status\":\"success\"}")
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        employeeWebClient.deleteEmployeeById(employeeId);

        wireMockServer.verify(1, deleteRequestedFor(urlEqualTo("/employee/" + employeeId)));
    }
}
