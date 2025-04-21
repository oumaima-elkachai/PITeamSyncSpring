package com.example.attendance.Backend.dto;

public class EmployeeStatsDTO {
    private String name;
    private double totalHours;
    private long totalRequests;
    private long approvedRequests;

    public EmployeeStatsDTO(String name, double totalHours, long totalRequests, long approvedRequests) {
        this.name = name;
        this.totalHours = totalHours;
        this.totalRequests = totalRequests;
        this.approvedRequests = approvedRequests;
    }

    // Getters & Setters
}

