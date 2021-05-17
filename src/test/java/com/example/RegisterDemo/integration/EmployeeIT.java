package com.example.RegisterDemo.integration;

import com.example.RegisterDemo.employee.Employee;
import com.example.RegisterDemo.employee.EmployeeRepository;
import com.example.RegisterDemo.employee.Gender;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StringUtils.trimAllWhitespace;


@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class EmployeeIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    private final Faker faker = new Faker();

    @Test
    @Disabled
    void canRegisterNewEmployee() throws Exception {
        //given
        String name = String.format("%s %s",
                faker.name().firstName(),
                faker.name().lastName());
        String email = faker.internet().emailAddress();
        Employee employee = new Employee(
                name,
                email, Gender.FEMALE);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee)));
        //then
        resultActions.andExpect(status().isOk());
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).usingElementComparatorIgnoringFields("id").contains(employee);
    }

    @Test
    void canDeleteEmployee() throws Exception {
        //given
        String name = String.format(
                "%s %s",
                faker.name().firstName(),
                faker.name().lastName()
        );

        String email = String.format("%s@gmail.com",
                StringUtils.trimAllWhitespace(name.trim().toLowerCase()));
        Employee employee = new Employee(
                name,
                email,
                Gender.MALE);
        mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee))).andExpect(status().isOk());

        // perform GET request to API
        MvcResult getEmployeesResult = mockMvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Grab the response as a string
        String contentAsString = getEmployeesResult
                .getResponse()
                .getContentAsString();

        // Map the string into a list of objects
        List<Employee> employees = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        long id = employees.stream()
                .filter(e -> e.getEmail().equals(employee.getEmail()))
                .map(Employee::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Employee with email:" + email + " not found"));

        //when
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/employees/" + id));
        //then
        resultActions.andExpect(status().isOk());
        boolean exists = employeeRepository.existsById(id);
        assertThat(exists).isFalse();
    }
}
