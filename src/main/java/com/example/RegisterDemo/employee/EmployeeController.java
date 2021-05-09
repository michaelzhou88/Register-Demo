package com.example.RegisterDemo.employee;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

//import java.util.Arrays;
import java.util.List;

@RestController // Marks the class as a controller and returns into JSON object
@RequestMapping(path = "api/v1/employees")  // Map requests to controller methods
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping // Expose endpoints
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public void addStudent(@RequestBody Employee employee) {
        employeeService.addEmployee(employee);
    }


}
