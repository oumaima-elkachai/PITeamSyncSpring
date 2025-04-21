package com.example.attendance.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class EmployeeDashboardDTO {
    private String id;
    private String name;
    private String email;
    private String role;
    private String department;
    private String phone;
    private int attendanceDays;
    private int attendancePercentage;
    private List<String> badges;
    private Map<String, Integer> leaveBalances;
}

