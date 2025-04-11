package com.example.attendance.Backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "benefits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Benefit {
    // Getter et Setter pour id
    @Getter
    @Id
    private String id;
    private String employeeId;
    private String type;
    private String eligibilityCriteria;
    private String description;


    public void setId(String id) {
        this.id = id;
    }

}