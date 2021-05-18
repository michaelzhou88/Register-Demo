package com.example.RegisterDemo.employee;

import com.example.RegisterDemo.employee.exception.BadRequestException;
import com.example.RegisterDemo.employee.exception.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// Handles all the business logic
@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PostMapping
    public void addStudent(Student student) {
        // check if email is taken
        Boolean existsEmail = studentRepository.selectExistsEmail(student.getEmail());
        if (existsEmail) {
            throw new BadRequestException(
                    "Email " + student.getEmail() + " has already been taken.");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        // check if employee exists
        if (!studentRepository.existsById(studentId)){
            throw new StudentNotFoundException("Student with ID:" + studentId + " does not exist.");
        }
        studentRepository.deleteById(studentId);
    }
}
