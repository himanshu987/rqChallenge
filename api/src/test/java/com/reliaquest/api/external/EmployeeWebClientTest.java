package com.reliaquest.api.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployeeResponse;
import static com.reliaquest.api.helper.EmployeeDefaults.defaultEmployees;
import static com.reliaquest.api.helper.EmployeeWebClientDefaults.buildJsonResponse;
import static org.assertj.core.api.Assertions.assertThat;

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
        wireMockServer.stubFor(
                get(urlEqualTo("/employee"))
                        .willReturn(ok(buildJsonResponse(defaultEmployees()))
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        );

        var allEmployees = employeeWebClient.getAllEmployees();

        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/employee")));
        assertThat(allEmployees).isEqualTo(defaultEmployeeResponse());
    }
}