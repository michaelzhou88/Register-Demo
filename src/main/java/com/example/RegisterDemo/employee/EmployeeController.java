package com.example.RegisterDemo.employee;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


//import java.util.Arrays;
import java.util.List;

@RestController // Marks the class as a controller and returns into JSON object
@RequestMapping(path = "api/v1/employees")  // Map requests to controller methods
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping // Expose endpoints
    public List<Employee> getAllEmployees() {
//        throw new IllegalStateException("oops error");
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public void addEmployee(@Valid @RequestBody Employee employee) {
        employeeService.addEmployee(employee);
    }

    @DeleteMapping(path = "{employeeId}")
    public void deleteEmployee(
            @PathVariable("employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    public void addStudent(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
    }

}
