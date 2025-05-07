package com.reliaquest.api.helper;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeResponse;

import java.util.List;

public class EmployeeDefaults {
    public static List<Employee> defaultEmployees() {
        return List.of(
                Employee.builder()
                        .id("1")
                        .name("John Doe")
                        .salary(50000)
                        .age(30)
                        .title("Software Engineer")
                        .email("john.doe@example.com")
                        .build(),
                Employee.builder()
                        .id("2")
                        .name("Jane Smith")
                        .salary(60000)
                        .age(28)
                        .title("Data Scientist")
                        .email("jane@ex.com")
                        .build(),
                Employee.builder()
                        .id("3")
                        .name("Alice Johnson")
                        .salary(55000)
                        .age(32)
                        .title("Product Manager")
                        .email("alice.johnson@example.com")
                        .build(),
                Employee.builder()
                        .id("4")
                        .name("Bob Brown")
                        .salary(45000)
                        .age(25)
                        .title("QA Engineer")
                        .email("bob.brown@example.com")
                        .build(),
                Employee.builder()
                        .id("5")
                        .name("Charlie Davis")
                        .salary(70000)
                        .age(35)
                        .title("DevOps Engineer")
                        .email("charlie.davis@example.com")
                        .build(),
                Employee.builder()
                        .id("6")
                        .name("Diana Evans")
                        .salary(80000)
                        .age(40)
                        .title("Engineering Manager")
                        .email("diana.evans@example.com")
                        .build(),
                Employee.builder()
                        .id("7")
                        .name("Ethan Harris")
                        .salary(48000)
                        .age(27)
                        .title("UI/UX Designer")
                        .email("ethan.harris@example.com")
                        .build(),
                Employee.builder()
                        .id("8")
                        .name("Fiona Green")
                        .salary(52000)
                        .age(29)
                        .title("Business Analyst")
                        .email("fiona.green@example.com")
                        .build(),
                Employee.builder()
                        .id("9")
                        .name("George King")
                        .salary(62000)
                        .age(33)
                        .title("Backend Developer")
                        .email("george.king@example.com")
                        .build(),
                Employee.builder()
                        .id("10")
                        .name("John Doe")
                        .salary(66000)
                        .age(31)
                        .title("Full Stack Engineer")
                        .email("john.doe2@example.com")
                        .build(),
                Employee.builder()
                        .id("11")
                        .name("Ivy Taylor")
                        .salary(47000)
                        .age(26)
                        .title("Marketing Specialist")
                        .email("ivy.taylor@example.com")
                        .build(),
                Employee.builder()
                        .id("12")
                        .name("Jack Wilson")
                        .salary(75000)
                        .age(37)
                        .title("Solutions Architect")
                        .email("jack.wilson@example.com")
                        .build(),
                Employee.builder()
                        .id("13")
                        .name("Karen White")
                        .salary(54000)
                        .age(34)
                        .title("HR Manager")
                        .email("karen.white@example.com")
                        .build()
        );
    }

    public static EmployeeResponse defaultEmployeeResponse(){
        return EmployeeResponse.builder()
                .data(defaultEmployees())
                .status("success")
                .build();
    }

    public static EmployeeResponse emptyEmployeeResponse(){
        return EmployeeResponse.builder()
                .data(List.of())
                .status("success")
                .build();
    }

    public static Employee defaultEmployeeWithId(String id) {
        return Employee.builder()
                .id(id)
                .name("John Doe")
                .salary(50000)
                .age(30)
                .title("Software Engineer")
                .email("john.doe@example.com")
                .build();
    }

    public static EmployeeResponse defaultEmployeeResponseWithId(String id) {
        return EmployeeResponse.builder()
                .data(List.of(defaultEmployeeWithId(id)))
                .status("success")
                .build();
    }
}
