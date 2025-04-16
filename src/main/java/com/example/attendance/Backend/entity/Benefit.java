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

    @DBRef
    private Employee employee;


    public void setId(String id) {
        this.id = id;
    }

}