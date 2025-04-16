package com.example.attendance.Backend.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Document(collection = "requests")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    private String id;

    private String employeeId;  // Stocke uniquement l'ID de l'employé

    private String type;  // Ex: Maladie, Congé payé, Sans solde
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private RequestStatus status;  // Enum : PENDING, APPROVED, REJECTED

    private String justification;
    @DBRef
    private Employee employee;


    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    //Getters

    public RequestStatus getStatus() {
        return status;
    }
}
