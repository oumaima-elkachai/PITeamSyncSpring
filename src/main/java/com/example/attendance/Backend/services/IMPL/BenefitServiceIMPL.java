package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.entity.Benefit;
import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.repository.BenefitRepository;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.services.interfaces.BenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitServiceIMPL implements BenefitService {

    private final EmployeeRepository employeeRepository;
    private final PresenceRepository presenceRepository;
    private final BenefitRepository benefitRepository;

    @Scheduled(cron = "0 0 0 1 * ?") // chaque 1er du mois à minuit
    public void generateMonthlyBenefits() {
        List<Employee> employees = employeeRepository.findAll();
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

        for (Employee employee : employees) {
            List<Presence> presences = presenceRepository.findByEmployeeIdAndDateBetween(
                    employee.getId(), firstDay, lastDay
            );

            double totalHours = presences.stream().mapToDouble(Presence::getHoursWorked).sum();
            double overtime = presences.stream().mapToDouble(Presence::getOvertimeHours).sum();

            // ✅ 1. Benefit: Ticket Restaurant
            if (totalHours > 200 && !benefitRepository.existsByEmployeeIdAndType(employee.getId(), "Ticket Restaurant")) {
                benefitRepository.save(new Benefit(
                        null,
                        employee.getId(),
                        "Ticket Restaurant",
                        "200h/mois",
                        "Dépassement d’heures",
                        employee
                ));

            }

            // ✅ 2. Benefit: Ticket Essence
            if (overtime > 20 && !benefitRepository.existsByEmployeeIdAndType(employee.getId(), "Ticket Essence")) {
                benefitRepository.save(new Benefit(
                        null,
                        employee.getId(),
                        "Ticket Essence",
                        "20h supp/mois",
                        "Effort supplémentaire",
                        employee
                ));

            }

            // ✅ 3. Benefit: Fidélité
            if (ChronoUnit.YEARS.between(employee.getHireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), now) >= 2
                    && !benefitRepository.existsByEmployeeIdAndType(employee.getId(), "Réduction -20%")) {
                benefitRepository.save(new Benefit(
                        null,
                        employee.getId(),
                        "Réduction -20%",
                        ">= 2 ans ancienneté",
                        "Fidélité",
                        employee
                ));

            }

            // ✅ 4. Benefit: Participation Conférences
            if ("Tech".equalsIgnoreCase(employee.getDepartment())
                    && !benefitRepository.existsByEmployeeIdAndType(employee.getId(), "Participation Conférences")) {
                benefitRepository.save(new Benefit(
                        null,
                        employee.getId(),
                        "Participation Conférences",
                        "Département Tech",
                        "Valorisation tech",
                        employee
                ));
            }
        }
    }
}

