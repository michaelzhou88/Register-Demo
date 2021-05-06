package com.example.RegisterDemo.employee;

import lombok.*;

// Model - Defines the properties of the Customer
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
}
