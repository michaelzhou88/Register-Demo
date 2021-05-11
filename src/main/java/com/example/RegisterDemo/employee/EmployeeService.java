package com.example.RegisterDemo.employee;

import com.example.RegisterDemo.employee.exception.BadRequestException;
import com.example.RegisterDemo.employee.exception.EmployeeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// Handles all the business logic
@AllArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PostMapping
    public void addEmployee(Employee employee) {
        // check if email is taken
        Boolean existsEmail = employeeRepository.selectExistsEmail(employee.getEmail());
        if (existsEmail) {
            throw new BadRequestException(
                    "Email " + employee.getEmail() + " has already been taken.");
        }
        employeeRepository.save(employee);
    }

    public void deleteEmployee(Long employeeId) {
        // check if employee exists
        if (!employeeRepository.existsById(employeeId)){
            throw new EmployeeNotFoundException("Employee with id" + employeeId + " does not exist.");
        }
        employeeRepository.deleteById(employeeId);
    }
}
