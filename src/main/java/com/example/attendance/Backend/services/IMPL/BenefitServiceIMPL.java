package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.entity.Benefit;
import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.repository.BenefitRepository;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.services.interfaces.BenefitService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitServiceIMPL implements BenefitService {
    private static final Logger logger = LoggerFactory.getLogger(BenefitServiceIMPL.class);


    private final EmployeeRepository employeeRepository;
    private final PresenceRepository presenceRepository;
    private final BenefitRepository benefitRepository;

    //@Scheduled(cron = "0 0 0 1 * ?") // chaque 1er du mois à minuit
    @Scheduled(cron = "0 0 0 1 * ?") // Exécution le 1er du mois à minuit
    public void generateMonthlyBenefits() {
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        employeeRepository.findAll().forEach(employee -> {
            try {
                generateEmployeeBenefits(employee, firstDay, lastDay);
            } catch (Exception e) {
                logger.error("Error generating benefits for employee {}: {}", employee.getId(), e.getMessage());
            }
        });
    }

    private void generateEmployeeBenefits(Employee employee, LocalDate startDate, LocalDate endDate) {
        List<Presence> presences = presenceRepository.findByEmployeeIdAndDateBetween(
                employee.getId(), startDate, endDate
        );

        double totalHours = presences.stream()
                .mapToDouble(Presence::getHoursWorked)
                .sum();

        double overtimeHours = presences.stream()
                .mapToDouble(Presence::getOvertimeHours)
                .sum();

        // 1. Ticket Restaurant (200h/mois)
        if (totalHours >= 200) {
            createBenefit(employee, "Ticket Restaurant",
                    String.format("%.2fh travaillées", totalHours), "Performance");
        }

        // 2. Ticket Essence (20h supp/mois)
        if (overtimeHours >= 20) {
            createBenefit(employee, "Ticket Essence",
                    String.format("%.2fh supplémentaires", overtimeHours), "Effort");
        }

        // 3. Réduction -20% (2+ ans ancienneté)
        long yearsOfService = ChronoUnit.YEARS.between(
                employee.getHireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now()
        );
        if (yearsOfService >= 2) {
            createBenefit(employee, "Réduction -20%",
                    String.format("%d ans d'ancienneté", yearsOfService), "Fidélité");
        }

        // 4. Participation Conférences (Département Tech)
        if ("Tech".equalsIgnoreCase(employee.getDepartment())) {
            createBenefit(employee, "Participation Conférences",
                    "Membre du département Tech", "Développement professionnel");
        }
    }

    private void createBenefit(Employee employee, String type, String criteria, String description) {
        if (!benefitRepository.existsByEmployeeIdAndTypeAndMonthCreated(
                employee.getId(),
                type,
                LocalDate.now().getMonthValue()
        )) {
            Benefit benefit = new Benefit(
                    employee.getId(),
                    type,
                    criteria,
                    description,
                    employee,
                    LocalDate.now().getMonthValue()
            );
            benefitRepository.save(benefit);
            logger.info("Benefit créé: {} pour {}", type, employee.getName());
        }
    }

}

