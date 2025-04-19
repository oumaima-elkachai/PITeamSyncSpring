package com.example.payroll.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "employees")
public class Employee {

    @Id
    private String id;

    private String name;
    private String email;
    private String department;
    private String role;
    private Date hireDate;

    // Constructors
    public Employee() {}

    public Employee(String id, String name, String email, String department, String role, Date hireDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
        this.hireDate = hireDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", hireDate=" + hireDate +
                '}';
    }
}
