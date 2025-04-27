package com.example.attendance.Backend.controllers;

import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/face")
public class FaceRecognitionController {

    private final RestTemplate restTemplate;
    private final EmployeeRepository employeeRepository;
    private final PresenceRepository presenceRepository;

    @PostMapping("/check")
    public ResponseEntity<?> checkInOrOut(@RequestBody Map<String, String> body) {
        String employeeId = body.get("employeeId");
        String capturedImage = body.get("image");

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) return ResponseEntity.status(404).body("Employee not found");

        Employee employee = optionalEmployee.get();
        String storedImage = employee.getFaceImage();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of(
                "capturedImage", capturedImage,
                "storedImage", storedImage
        ), headers);

        ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:8000/verify", request, Map.class);

        if (Boolean.TRUE.equals(response.getBody().get("verified"))) {
            LocalDate today = LocalDate.now();
            Presence presence = presenceRepository.findByEmployeeIdAndDate(employeeId, today)
                    .orElseGet(() -> new Presence());

            presence.setEmployeeId(employeeId);
            presence.setDate(today);

            if (presence.getCheckInTime() == null) {
                presence.setCheckInTime(LocalTime.now());
            } else {
                presence.setCheckOutTime(LocalTime.now());
            }

            presenceRepository.save(presence);

            return ResponseEntity.ok("Check-in/out successful for " + employee.getName());
        }

        return ResponseEntity.status(401).body("Face not recognized");
    }
}
