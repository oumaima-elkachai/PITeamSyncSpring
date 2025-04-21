package com.example.payroll.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnomalyResponse {

    private String employeeId;
    private String employeeName;
    private Double amount;
    private String paymentMonth;
    private boolean isAnomalous;
    private String reason;



}