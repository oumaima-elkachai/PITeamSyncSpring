package com.example.attendance.Backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter

@Document(collection = "attendances")

public class Presence {


    @Id
    private String id;

    @Field("employeeId")
    private String employeeId;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkInTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkOutTime;



    private double hoursWorked;
    private double overtimeHours;

    public Presence() {}

    //setters
    public void setId(String id) {
        this.id = id;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    //Getters


    // Constructeur avec paramètres
    public Presence(String id, String employeeId, LocalDate date, LocalTime checkInTime,LocalTime checkOutTime,double hoursWorked, double overtimeHours) {
        this.id = id;
        this.employeeId = employeeId;
        this.date = date;
        this.checkInTime=checkInTime;
        this.checkOutTime=checkOutTime;
        this.hoursWorked = hoursWorked;
        this.overtimeHours = overtimeHours;
    }



    // toString()
    @Override
    public String toString() {
        return "Payroll{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", date=" + checkInTime +
                ", checkOutTime" +checkOutTime +
                '}';
    }



}