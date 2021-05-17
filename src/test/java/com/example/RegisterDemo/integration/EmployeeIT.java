package com.example.RegisterDemo.integration;

import com.example.RegisterDemo.employee.Employee;
import com.example.RegisterDemo.employee.EmployeeRepository;
import com.example.RegisterDemo.employee.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void canRegisterNewEmployee() throws Exception {
        //given
        Employee employee = new Employee("David", "davidjones@gmail.com", Gender.MALE);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee)));
        //then
        resultActions.andExpect(status().isOk());
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).usingElementComparatorIgnoringFields("id").contains(employee);
    }
}
