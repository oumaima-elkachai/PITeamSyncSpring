package com.example.attendance.Backend.dto;

import com.example.attendance.Backend.entity.Presence;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.format.DateTimeFormatter;

public class PresenceDTO {

    private String id;

    private String employeeId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;

    @JsonFormat(pattern = "HH:mm")
    private String checkInTime;

    @JsonFormat(pattern = "HH:mm")
    private String checkOutTime;

    private double hoursWorked;
    private double overtimeHours;
    private String employeeName; // Optionnel

    // Getters
    public String getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public String getDate() { return date; }
    public String getCheckInTime() { return checkInTime; }
    public String getCheckOutTime() { return checkOutTime; }
    public double getHoursWorked() { return hoursWorked; }
    public double getOvertimeHours() { return overtimeHours; }
    public String getEmployeeName() { return employeeName; }

    // Setters
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public PresenceDTO(Presence presence) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        this.id = presence.getId();
        this.employeeId = presence.getEmployeeId();
        this.date = presence.getDate() != null ? presence.getDate().format(dateFormatter) : null;
        this.checkInTime = presence.getCheckInTime() != null ? presence.getCheckInTime().format(timeFormatter) : null;
        this.checkOutTime = presence.getCheckOutTime() != null ? presence.getCheckOutTime().format(timeFormatter) : null;
        this.hoursWorked = presence.getHoursWorked();
        this.overtimeHours = presence.getOvertimeHours();
    }


}
