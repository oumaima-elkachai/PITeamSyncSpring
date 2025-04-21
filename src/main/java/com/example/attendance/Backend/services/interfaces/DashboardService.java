package com.example.attendance.Backend.services.interfaces;

import com.example.attendance.Backend.dto.EmployeeStatsDTO;

import java.util.List;

public interface DashboardService {
    List<EmployeeStatsDTO> generateStats();

}
