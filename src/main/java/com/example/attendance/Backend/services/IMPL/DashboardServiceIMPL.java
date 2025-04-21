package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.dto.EmployeeStatsDTO;
import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.entity.RequestStatus;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.repository.RequestRepository;
import com.example.attendance.Backend.services.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceIMPL implements DashboardService {

    private final PresenceRepository presenceRepository;
    private final EmployeeRepository employeeRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<EmployeeStatsDTO> generateStats() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeStatsDTO> stats = new ArrayList<>();

        for (Employee emp : employees) {
            double totalHours = presenceRepository.findByEmployeeId(emp.getId())
                    .stream().mapToDouble(Presence::getHoursWorked).sum();

            long totalRequests = requestRepository.findByEmployeeId(emp.getId()).size();
            long approvedRequests = requestRepository.findByEmployeeId(emp.getId())
                    .stream().filter(r -> r.getStatus().equals(RequestStatus.APPROVED)).count();

            stats.add(new EmployeeStatsDTO(
                    emp.getName(),
                    totalHours,
                    totalRequests,
                    approvedRequests
            ));
        }
        return stats;
    }

}
