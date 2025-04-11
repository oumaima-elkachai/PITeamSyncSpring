// CONTROLLER
package com.example.attendance.Backend.controllers;

import com.example.attendance.Backend.dto.PresenceDTO;
import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.services.interfaces.EmployeeService;
import com.example.attendance.Backend.services.interfaces.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/attendances")
public class PresenceController {

    @Autowired
    private PresenceService presenceService;
    @Autowired  // Ajoutez cette annotation
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PresenceRepository presenceRepository;



    @GetMapping("/all")
    public ResponseEntity<?> getAllAttendances() {
        try {
            List<Presence> presences = presenceService.getAllAttendances();
            List<PresenceDTO> dtos = presences.stream()
                    .map(p -> {
                        PresenceDTO dto = new PresenceDTO(p);
                        // Gestion des employeeId null
                        if (p.getEmployeeId() != null) {
                            dto.setEmployeeName(employeeService.getEmployeeName(p.getEmployeeId()));
                        } else {
                            dto.setEmployeeName("Employé non spécifié");
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching attendances: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Presence> getAttendanceById(@PathVariable String id) {
        Presence presence = presenceService.getAttendanceById(id);
        return ResponseEntity.ok(presence);
    }

    @PostMapping("/saveAttendance")
    public ResponseEntity<?> saveAttendance(@RequestBody Presence presence) {
        // Debug avant sauvegarde


        Presence saved = presenceService.saveAttendance(presence);

        // Debug après sauvegarde
        System.out.println("EmployeeId sauvegardé: " + saved.getEmployeeId());

        return ResponseEntity.ok(saved);
    }

    /*@PutMapping("/update/{id}")
    public ResponseEntity<Presence> updateAttendance(@PathVariable String id, @RequestBody Presence presenceDetails) {
        Presence updated = presenceService.updateAttendance(id, presenceDetails);
        return ResponseEntity.ok(updated);
    }*/

    @PutMapping("/update/{id}")
    public ResponseEntity<Presence> update(@PathVariable String id, @RequestBody Map<String, Object> body) {
        Presence presence = parsePresenceFromBody(body);
        return ResponseEntity.ok(presenceService.updateAttendance(id, presence));
    }
    @PutMapping("/updatePresenceManually/{id}")
    public ResponseEntity<Presence> updatePresenceManually(@PathVariable String id, @RequestBody Presence presence) {
        Presence updated = presenceService.updateAttendance(id, presence);
        return ResponseEntity.ok(updated);
    }

    private Presence parsePresenceFromBody(Map<String, Object> body) {
        Presence presence = new Presence();
        presence.setEmployeeId((String) body.get("employeeId"));
        presence.setDate(LocalDate.parse((String) body.get("date")));
        presence.setCheckInTime(LocalTime.parse((String) body.get("checkInTime")));
        presence.setCheckOutTime(LocalTime.parse((String) body.get("checkOutTime")));
        return presence;
    }


    @DeleteMapping("/{id}")
    public void deleteAttendance(@PathVariable String id) {
        presenceService.deleteAttendance(id);
    }

    @PutMapping("/updateCheckOutTime")
    public ResponseEntity<?> updateCheckOutTime(@RequestBody Map<String, String> data) {
        String employeeId = data.get("employeeId");
        String dateStr = data.get("date");
        String checkOutTimeStr = data.get("checkOutTime");

        try {
            LocalDate date = LocalDate.parse(dateStr); // conversion ici
            LocalTime checkOutTime = LocalTime.parse(checkOutTimeStr);

            Optional<Presence> attendanceOpt = presenceRepository.findByEmployeeIdAndDate(employeeId, date);

            if (attendanceOpt.isPresent()) {
                Presence attendance = attendanceOpt.get();
                attendance.setCheckOutTime(checkOutTime);
                presenceRepository.save(attendance);
                return ResponseEntity.ok("Check-out mis à jour");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Présence non trouvée pour l'employé à cette date");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    @PutMapping("/updatePresenceManually")
    public ResponseEntity<?> updatePresenceManually(@RequestBody Map<String, String> data) {
        String employeeId = data.get("employeeId");
        String dateStr = data.get("date");
        String checkInTimeStr = data.get("checkInTime");
        String checkOutTimeStr = data.get("checkOutTime");

        if (employeeId == null || dateStr == null) {
            return ResponseEntity.badRequest().body("employeeId et date sont obligatoires.");
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);
            Optional<Presence> presenceOpt = presenceRepository.findByEmployeeIdAndDate(employeeId, date);

            if (presenceOpt.isPresent()) {
                Presence presence = presenceOpt.get();

                if (checkInTimeStr != null && !checkInTimeStr.isEmpty()) {
                    presence.setCheckInTime(LocalTime.parse(checkInTimeStr));
                }

                if (checkOutTimeStr != null && !checkOutTimeStr.isEmpty()) {
                    presence.setCheckOutTime(LocalTime.parse(checkOutTimeStr));
                }

                presenceRepository.save(presence);
                return ResponseEntity.ok("Présence mise à jour manuellement.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aucune présence trouvée pour cet employé à cette date.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de la mise à jour manuelle : " + e.getMessage());
        }
    }







}
