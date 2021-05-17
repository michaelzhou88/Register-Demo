package com.example.RegisterDemo.employee;

import com.example.RegisterDemo.employee.exception.BadRequestException;
import com.example.RegisterDemo.employee.exception.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    private EmployeeService underTest;

    @BeforeEach
    void setUp() {
        underTest = new EmployeeService(employeeRepository);
    }

    @Test
    void canGetAllEmployees() {
        //when
        underTest.getAllEmployees();
        //then
        verify(employeeRepository).findAll();
    }

    @Test
    void canAddEmployee() {
        //given
        String email = "david@gmail.com";
        Employee employee = new Employee("David", email, Gender.MALE);
        //when
        underTest.addEmployee(employee);
        //then
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        //ArgumentCaptor allows us to capture the value that the save method took as an argument
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee = employeeArgumentCaptor.getValue();

        assertThat(capturedEmployee).isEqualTo(employee);

    }

    @Test
    void willThrowWhenEmailIsTaken() {
        //given
        Employee employee = new Employee("David", "david@gmail.com", Gender.MALE);
        given(employeeRepository.selectExistsEmail(anyString()))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() -> underTest.addEmployee(employee))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + employee.getEmail() + " has already been taken");

        verify(employeeRepository, never()).save(any());

    }

    @Test
    void canDeleteEmployee() {
        //given
        long id = 10;
        given(employeeRepository.existsById(id))
            .willReturn(true);
        //when
        underTest.deleteEmployee(id);
        //then
        verify(employeeRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteEmployeeNotFound() {
        //given
        long id = 10;
        given(employeeRepository.existsById(id))
                .willReturn(false);
        //when
        //then
        assertThatThrownBy(() -> underTest.deleteEmployee(id))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("Employee with ID:" + id + " does not exist");

        verify(employeeRepository, never()).deleteById(any());
    }
}