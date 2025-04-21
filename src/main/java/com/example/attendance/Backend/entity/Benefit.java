package com.example.attendance.Backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "benefits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Benefit {
    @Getter
    @Id
    private String id;
    private String employeeId;
    private String type;
    private String eligibilityCriteria;
    private String description;
    private int monthCreated;

    @DBRef
    private Employee employee;



    public void setId(String id) {
        this.id = id;
    }

    public Benefit(String employeeId, String type, String eligibilityCriteria,
                   String description, Employee employee, int monthCreated) {
        this.employeeId = employeeId;
        this.type = type;
        this.eligibilityCriteria = eligibilityCriteria;
        this.description = description;
        this.employee = employee;
        this.monthCreated = monthCreated;
    }

}