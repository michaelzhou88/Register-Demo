package com.example.RegisterDemo.employee;

import lombok.*;

import javax.persistence.*;

// Model - Defines the properties of the Customer
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
// Alternatively could have just used @Data to represent all the annotations above but that would make the strings below immutable
@Table
public class Employee {
    @Id
    @SequenceGenerator(
            name = "employee_sequence",
            sequenceName = "employee_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "employee_sequence",
            strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Employee(String name, String email, Gender gender) {
        this.name = name;
        this.email = email;
        this.gender = gender;
    }
}
