package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.entity.Benefit;
import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.repository.BenefitRepository;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.services.interfaces.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BadgeServiceIMPL implements BadgeService {
    private final BenefitRepository benefitRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public List<String> getBadgesForEmployee(String employeeId) {
        Optional<Employee> emp = employeeRepository.findById(employeeId);
        if (emp.isEmpty()) throw new RuntimeException("Employee not found");

        List<Benefit> benefits = benefitRepository.findByEmployeeId(employeeId);
        if (benefits == null) benefits = new ArrayList<>();

        List<String> badges = new ArrayList<>();

        System.out.println("🟡 Employee: " + employeeId + " a les benefits:");
        for (Benefit benefit : benefits) {
            System.out.println("🟢 - " + benefit.getType());

            switch (benefit.getType()) {
                case "Ticket Restaurant" -> badges.add("Top Performer");
                case "Ticket Essence" -> badges.add("Overtimer");
                case "Réduction -20%" -> badges.add("Loyal");
                case "Participation Conférences" -> badges.add("Tech");
            }
        }


        if (badges.size() >= 3) {
            badges.add("All-Star");
        }

        return badges;
    }




}
