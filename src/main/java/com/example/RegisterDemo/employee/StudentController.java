package com.example.RegisterDemo.employee;

import com.example.RegisterDemo.employee.exception.ApiRequestException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController // Marks the class as a controller and returns into JSON object
@RequestMapping(path = "api/v1/students")  // Map requests to controller methods
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping // Expose endpoints
    public List<Student> getAllStudents() {
        try {
            return studentService.getAllStudents();
        } catch (Exception e) {
            throw new ApiRequestException("Oops cannot get all students. " + e);
        }
    }

    @PostMapping
    public void addStudent(@Valid @RequestBody Student student) {
        studentService.addStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(
            @PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }

}
