package com.example.RegisterDemo.employee;

import com.example.RegisterDemo.employee.exception.BadRequestException;
import com.example.RegisterDemo.employee.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        String email = "david@gmail.com";
        Student student = new Student("David", email, Gender.MALE);
        //when
        underTest.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        //ArgumentCaptor allows us to capture the value that the save method took as an argument
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);

    }

    @Test
    void willThrowWhenEmailIsTaken() {
        //given
        Student employee = new Student("David", "david@gmail.com", Gender.MALE);
        given(studentRepository.selectExistsEmail(anyString()))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() -> underTest.addStudent(employee))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + employee.getEmail() + " has already been taken");

        verify(studentRepository, never()).save(any());

    }

    @Test
    void canDeleteStudent() {
        //given
        long id = 10;
        given(studentRepository.existsById(id))
            .willReturn(true);
        //when
        underTest.deleteStudent(id);
        //then
        verify(studentRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteEmployeeNotFound() {
        //given
        long id = 10;
        given(studentRepository.existsById(id))
                .willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with ID:" + id + " does not exist");

        verify(studentRepository, never()).deleteById(any());
    }
}