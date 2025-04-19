package com.example.payroll.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;  // Changement de Long à String pour l'ID, MongoDB utilise ObjectId qui est de type String

    @DBRef  // Utilisation de DBRef pour les relations avec MongoDB
    private Payroll payroll;

    @DBRef
    private ProjectBudget projectBudget;

    private BigDecimal amount;

    private LocalDateTime transactionDate = LocalDateTime.now();

    // Getters et Setters générés par @Getter et @Setter de Lombok, mais je les ajoute ici explicitement au cas où tu préférerais les définir manuellement.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payroll getPayroll() {
        return payroll;
    }

    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    public ProjectBudget getProjectBudget() {
        return projectBudget;
    }

    public void setProjectBudget(ProjectBudget projectBudget) {
        this.projectBudget = projectBudget;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
