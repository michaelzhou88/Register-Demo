package com.example.RegisterDemo.employee;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Handles all the business logic
@AllArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
