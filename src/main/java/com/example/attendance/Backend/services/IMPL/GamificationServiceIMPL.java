package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.entity.RequestStatus;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.repository.RequestRepository;
import com.example.attendance.Backend.services.interfaces.DashboardService;
import com.example.attendance.Backend.services.interfaces.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GamificationServiceIMPL implements GamificationService {
    private final PresenceRepository presenceRepository;
    private final RequestRepository requestRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<String> getBadgesForEmployee(String employeeId) {
        List<String> badges = new ArrayList<>();

        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

        // 🥇 Ponctuel du mois
        long daysPresent = presenceRepository
                .findByEmployeeIdAndDateBetween(employeeId, firstDay, lastDay).size();
        if (daysPresent >= 20) badges.add("🥇 Ponctuel du mois");

        // 🧠 Sans absence
        long totalPresences = presenceRepository.findByEmployeeId(employeeId).size();
        long totalDaysSinceHire = ChronoUnit.DAYS.between(
                employeeRepository.findById(employeeId).get().getHireDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate(),
                now);
        if (totalPresences >= totalDaysSinceHire * 0.8) {
            badges.add("🧠 Sans absence");
        }

        // 🔁 Réactivité
        if (daysPresent >= 20) badges.add("🔁 Réactivité");

        // 🎯 Zéro demande refusée
        long refusedRequests = requestRepository.findByEmployeeId(employeeId).stream()
                .filter(r -> r.getStatus() == RequestStatus.REJECTED)
                .count();
        if (refusedRequests == 0) badges.add("🎯 Zéro demande refusée");

        // 🔥 Employé du Mois
        String topEmployeeId = calculateTopPerformer(); // 👇 méthode locale
        if (employeeId.equals(topEmployeeId)) {
            badges.add("🔥 Employé du Mois");
        }

        return badges;
    }

    @Override
    public String getTopPerformerEmployeeId() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        Map<String, Double> employeeHoursMap = new HashMap<>();

        employeeRepository.findAll().forEach(employee -> {
            List<Presence> presences = presenceRepository.findByEmployeeIdAndDateBetween(employee.getId(), start, end);
            double totalHours = presences.stream().mapToDouble(Presence::getHoursWorked).sum();
            employeeHoursMap.put(employee.getId(), totalHours);
        });

        return employeeHoursMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Calcul direct dans le service
    private String calculateTopPerformer() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        Map<String, Double> employeeHours = new HashMap<>();
        employeeRepository.findAll().forEach(emp -> {
            double hours = presenceRepository.findByEmployeeIdAndDateBetween(emp.getId(), start, end)
                    .stream()
                    .mapToDouble(Presence::getHoursWorked)
                    .sum();
            employeeHours.put(emp.getId(), hours);
        });

        return employeeHours.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

}
