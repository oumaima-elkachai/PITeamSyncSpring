package com.example.attendance.Backend.controllers;

import com.example.attendance.Backend.services.interfaces.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class GamificationController {
    private final GamificationService gamificationService;

    @GetMapping("/gamified/{employeeId}")
    public ResponseEntity<List<String>> getGamifiedBadges(@PathVariable String employeeId) {
        return ResponseEntity.ok(gamificationService.getBadgesForEmployee(employeeId));
    }
    @GetMapping("/top-employee")
    public String getTopPerformerEmployeeId() {
        return gamificationService.getTopPerformerEmployeeId();
    }

}
