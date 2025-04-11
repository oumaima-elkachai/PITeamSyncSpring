package com.example.attendance.Backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "employees")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private String id;

    private String name;
    private String email;
    private String department;
    private String role;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date hireDate;

    @DBRef(lazy = true)
    @JsonIgnoreProperties({"employee"}) // Évite la référence circulaire
    private List<Presence> attendances;



    // Modifiez le getter pour éviter la sérialisation infinie
    @JsonIgnore
    public List<Presence> getAttendances() {
        return attendances;
    }


    public String getId() { return id; }
    public String getName() { return name; }


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

