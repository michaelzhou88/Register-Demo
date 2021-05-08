package com.example.RegisterDemo.employee;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController // Marks the class as a controller and returns into JSON object
@RequestMapping(path = "api/v1/employees")  // Map requests to controller methods
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping // Expose endpoints
    public List<Employee> getAllEmployees() {
//        Hard coded data
//        List<Employee> employees = Arrays.asList(
//                new Employee(1L, "Bill", "billGates@hotmail.com", Gender.MALE),
//                new Employee(2L, "Elon", "eMusk@tesla.com" , Gender.MALE)
//        );
//        return employees;
        return employeeService.getAllEmployees();
    }


}
