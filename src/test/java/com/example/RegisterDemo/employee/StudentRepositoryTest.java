package com.example.RegisterDemo.employee;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest    // For unit testing repositories
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckWhenEmployeeEmailExists() {
        //given
        String email = "david@gmail.com";
        Student employee = new Student("David", email, Gender.MALE);
        underTest.save(employee);

        //when
        boolean expected = underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isTrue();

    }

    @Test
    void itShouldCheckWhenEmployeeEmailDoesNotExists() {
        //given
        String email = "david@gmail.com";

        //when
        boolean expected = underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isFalse();

    }
}