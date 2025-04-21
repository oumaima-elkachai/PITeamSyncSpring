package com.example.attendance.Backend.controllers;

import com.example.attendance.Backend.dto.EmployeeDashboardDTO;
import com.example.attendance.Backend.dto.EmployeeStatsDTO;
import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.entity.Request;
import com.example.attendance.Backend.entity.RequestStatus;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.repository.RequestRepository;
import com.example.attendance.Backend.services.interfaces.DashboardService;
import com.example.attendance.Backend.services.interfaces.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")

public class DashboardController {

    private final EmployeeRepository employeeRepository;
    private final PresenceRepository presenceRepository;
    private final RequestRepository requestRepository;
    private final GamificationService gamificationService;


    @GetMapping("/stats")
    public List<Map<String, Object>> getPresenceStats() {
        List<Employee> employees = employeeRepository.findAll();
        List<Map<String, Object>> stats = new ArrayList<>();

        for (Employee employee : employees) {
            double totalHours = presenceRepository
                    .findByEmployeeId(employee.getId())
                    .stream()
                    .mapToDouble(Presence::getHoursWorked)
                    .sum();

            Map<String, Object> data = new HashMap<>();
            data.put("name", employee.getName() != null ? employee.getName() : "N/A");
            //data.put("totalHours", totalHours);
            data.put("totalHours", (int) Math.round(totalHours));

            stats.add(data);
        }

        return stats;
    }

    @GetMapping("/weekly-stats")
    public Map<String, List<Integer>> getWeeklyStats() {
        Map<String, List<Integer>> weeklyStats = new LinkedHashMap<>();
        List<Employee> employees = employeeRepository.findAll();
        LocalDate today = LocalDate.now();

        // Obtenir les 4 dernières semaines
        for (Employee emp : employees) {
            List<Presence> presences = presenceRepository.findByEmployeeId(emp.getId());
            List<Integer> hoursPerWeek = new ArrayList<>(Arrays.asList(0, 0, 0, 0));

            for (Presence p : presences) {
                LocalDate date = p.getDate();
                int weekDiff = getWeekIndex(today, date); // 0 pour cette semaine, 1 pour -1 semaine, etc.
                if (weekDiff >= 0 && weekDiff < 4) {
                    int old = hoursPerWeek.get(weekDiff);
                    hoursPerWeek.set(weekDiff, old + (int) p.getHoursWorked());
                }
            }

            weeklyStats.put(emp.getName() != null ? emp.getName() : "N/A", hoursPerWeek);
        }

        return weeklyStats;
    }

    // Retourne l'index de semaine (0 pour actuelle, 1 pour la semaine passée… jusqu'à 3 max)
    private int getWeekIndex(LocalDate now, LocalDate date) {
        WeekFields wf = WeekFields.of(Locale.getDefault());
        int currentWeek = now.get(wf.weekOfWeekBasedYear());
        int dateWeek = date.get(wf.weekOfWeekBasedYear());

        return currentWeek - dateWeek;
    }


    @GetMapping("/employees-with-badges")
    public List<EmployeeDashboardDTO> getEmployeesWithStats() {
        List<EmployeeDashboardDTO> result = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        for (Employee emp : employeeRepository.findAll()) {
            int totalDays = presenceRepository.findByEmployeeIdAndDateBetween(emp.getId(), startOfMonth, endOfMonth).size();
            int percentage = Math.min(100, (int) (Math.random() * 100)); // à améliorer

            // Leave balance
            List<Request> approved = requestRepository.findByEmployeeIdAndStatus(emp.getId(), RequestStatus.APPROVED);
            Map<String, Integer> balances = new HashMap<>();
            for (Request r : approved) {
                int days = (int) ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate()) + 1;
                balances.merge(r.getType(), days, Integer::sum);
            }

            result.add(new EmployeeDashboardDTO(
                    emp.getId(),
                    emp.getName(),
                    emp.getEmail(),
                    emp.getRole(),
                    emp.getDepartment(),
                    emp.getPhone() != null ? emp.getPhone() : "+216 12 345 678",
                    totalDays,
                    percentage,
                    gamificationService.getBadgesForEmployee(emp.getId()),
                    balances
            ));
        }

        return result;
    }


}
