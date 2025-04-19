package com.example.payroll.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "payrolls")  // Annotation pour MongoDB
@Getter
public class Payroll {

    @Id
    private String id;  // MongoDB utilise String pour les IDs

    private String employeeId;  // Stocke l'ID de l'employé (référence)
    private Double salary;
    private Double bonus;
    private Date payDate;


    // Constructeur par défaut
    public Payroll() {}

    // Constructeur avec paramètres
    public Payroll(String id, String employeeId, Double salary, Double bonus, Date payDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.salary = salary;
        this.bonus = bonus;
        this.payDate = payDate;
    }

    // Getters manuels

    public String getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Double getSalary() {
        return salary;
    }

    public Double getBonus() {
        return bonus;
    }

    public Date getPayDate() {
        return payDate;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    // toString()
    @Override
    public String toString() {
        return "Payroll{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", payDate=" + payDate +
                '}';
    }
}
