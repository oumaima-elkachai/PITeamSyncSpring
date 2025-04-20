// filepath: c:\Users\ASUS-INFOTEC\Documents\GitHub\PITeamSyncSpring\src\main\java\com\example\events\controllers\ParticipationController.java
package com.example.events.controllers;

import com.example.events.entity.Participation;
import com.example.events.entity.AuditLog;
import com.example.events.repository.AuditLogRepository;
import com.example.events.services.interfaces.IParticipationService;
import com.example.events.services.interfaces.IAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
public class ParticipationController {

    @Autowired
    private IParticipationService participationService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private IAuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<Participation> addParticipation(@RequestBody Participation participation) {
        return ResponseEntity.ok(participationService.addParticipation(participation));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Participation> updateParticipationStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(participationService.updateParticipationStatus(id, status));
    }

    @GetMapping("/by-participant/{participantId}")
    public ResponseEntity<List<Participation>> getParticipationsByParticipant(@PathVariable String participantId) {
        return ResponseEntity.ok(participationService.getParticipationsByParticipant(participantId));
    }

    @GetMapping("/by-event/{eventId}")
    public ResponseEntity<List<Participation>> getParticipationsByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(participationService.getParticipationsByEvent(eventId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable String id) {
        participationService.deleteParticipation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Participation>> getAllParticipations() {
        List<Participation> participations = participationService.getAllParticipations();
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAllAuditLogs());
    }

    @GetMapping("/{id}/audit-logs")
    public ResponseEntity<List<AuditLog>> getParticipationAuditLogs(@PathVariable String id) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByParticipationId(id));
    }

    @GetMapping("/event/{eventId}/audit-logs")
    public ResponseEntity<List<AuditLog>> getEventAuditLogs(@PathVariable String eventId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEventId(eventId));
    }

    @GetMapping("/participant/{participantId}/audit-logs")
    public ResponseEntity<List<AuditLog>> getParticipantAuditLogs(@PathVariable String participantId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByParticipantId(participantId));
    }
}