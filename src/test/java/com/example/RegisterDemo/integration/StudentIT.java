package com.example.RegisterDemo.integration;

import com.example.RegisterDemo.employee.Student;
import com.example.RegisterDemo.employee.StudentRepository;
import com.example.RegisterDemo.employee.Gender;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
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


@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class StudentIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    private final Faker faker = new Faker();

    @Test
    void canRegisterNewStudent() throws Exception {
        //given
        String name = String.format("%s %s",
                faker.name().firstName(),
                faker.name().lastName());
        String email = faker.internet().emailAddress();
        Student student = new Student(
                name,
                email, Gender.FEMALE);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/students").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student)));
        //then
        resultActions.andExpect(status().isOk());
        List<Student> students = studentRepository.findAll();
        assertThat(students).usingElementComparatorIgnoringFields("id").contains(student);
    }

    @Test
    void canDeleteStudent() throws Exception {
        //given
        String name = String.format(
                "%s %s",
                faker.name().firstName(),
                faker.name().lastName()
        );

        String email = String.format("%s@gmail.com",
                StringUtils.trimAllWhitespace(name.trim().toLowerCase()));
        Student student = new Student(
                name,
                email,
                Gender.MALE);
        mockMvc.perform(post("/api/v1/students").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student))).andExpect(status().isOk());

        // perform GET request to API
        MvcResult getStudentsResult = mockMvc.perform(get("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Grab the response as a string
        String contentAsString = getStudentsResult
                .getResponse()
                .getContentAsString();

        // Map the string into a list of objects
        List<Student> students = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        long id = students.stream()
                .filter(s -> s.getEmail().equals(student.getEmail()))
                .map(Student::getId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Student with email:" + email + " not found"));

        //when
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/students/" + id));
        //then
        resultActions.andExpect(status().isOk());
        boolean exists = studentRepository.existsById(id);
        assertThat(exists).isFalse();
    }
}
