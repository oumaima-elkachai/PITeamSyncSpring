package com.example.attendance.Backend.controllers;

import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.repository.BenefitRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.services.interfaces.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;
    private final PresenceRepository presenceRepository;




    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getBadges(@PathVariable String employeeId) {
        try {
            return ResponseEntity.ok(badgeService.getBadgesForEmployee(employeeId));
        } catch (Exception e) {
            e.printStackTrace(); // 👈 Affiche l’erreur dans la console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur : " + e.getMessage());
        }
    }
    @GetMapping("/debug/{employeeId}/hours")
    public ResponseEntity<?> getTotalHoursAndOvertime(@PathVariable String employeeId) {
        try {
            List<Presence> presences = presenceRepository.findByEmployeeId(employeeId);
            double totalHours = presences.stream().mapToDouble(Presence::getHoursWorked).sum();
            double overtime = presences.stream().mapToDouble(Presence::getOvertimeHours).sum();

            return ResponseEntity.ok(
                    String.format("Total hours: %.2f | Overtime hours: %.2f", totalHours, overtime)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }




}
