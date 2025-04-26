package com.example.events.controllers;

import com.example.events.entity.Participation;
import com.example.events.entity.AuditLog;
import com.example.events.repository.AuditLogRepository;
import com.example.events.services.interfaces.IParticipationService;
import com.example.events.services.interfaces.IAuditLogService;
import com.example.events.services.interfaces.IEmailService;
import com.example.events.services.interfaces.IEventStatisticsService;
import com.example.events.entity.EventStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participations")
@CrossOrigin(origins = "http://localhost:4200")
public class ParticipationController {
    
    @Autowired
    private IEmailService emailService;  // Update this line
    @Autowired
    private IParticipationService participationService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private IAuditLogService auditLogService;

    @Autowired
    private IEventStatisticsService statisticsService;

    @PostMapping
    public ResponseEntity<Participation> addParticipation(@RequestBody Participation participation) {
        return ResponseEntity.ok(participationService.addParticipation(participation));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateParticipationStatus(
        @PathVariable String id,
        @RequestBody String status) {
        try {
            Participation participation = participationService.updateParticipationStatus(id, status);
            return ResponseEntity.ok(participation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
        return ResponseEntity.ok(auditLogService.getAuditLogsByParticipation(id));
    }

    @GetMapping("/event/{eventId}/audit-logs")
    public ResponseEntity<List<AuditLog>> getEventAuditLogs(@PathVariable String eventId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEvent(eventId));
    }

    @GetMapping("/participant/{participantId}/audit-logs")
    public ResponseEntity<List<AuditLog>> getParticipantAuditLogs(@PathVariable String participantId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByParticipant(participantId));
    }

    @GetMapping("/event-title/{eventId}")
    public ResponseEntity<String> getEventTitle(@PathVariable String eventId) {
        return ResponseEntity.ok(participationService.getEventTitleForParticipation(eventId));
    }

    @GetMapping("/participant-email/{participantId}")
    public ResponseEntity<String> getParticipantEmail(@PathVariable String participantId) {
        return ResponseEntity.ok(participationService.getParticipantEmailForParticipation(participantId));
    }

    @GetMapping("/statistics/{eventId}")
    public ResponseEntity<EventStatistics> getEventStatistics(@PathVariable String eventId) {
        return ResponseEntity.ok(statisticsService.getEventStatistics(eventId));
    }

    /*@PostMapping("/statistics/{eventId}/update")
    public ResponseEntity<EventStatistics> updateEventStatistics(@PathVariable String eventId) {        
        return ResponseEntity.ok(statisticsService.updateEventStatistics(eventId));    
    }*/


    @GetMapping("/filter/event-title/{title}")
    public ResponseEntity<List<Participation>> getParticipationsByEventTitle(@PathVariable String title) {
        List<Participation> participations = participationService.getParticipationsByEventTitle(title);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/filter/participant-email/{email}")
    public ResponseEntity<List<Participation>> getParticipationsByParticipantEmail(@PathVariable String email) {
        List<Participation> participations = participationService.getParticipationsByParticipantEmail(email);
        return ResponseEntity.ok(participations);
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<?> confirmParticipation(@PathVariable String id) {
        Participation participation = participationService.confirmParticipation(id);
        
        // Get participant email and name
        String participantEmail = participationService.getParticipantEmailForParticipation(participation.getParticipantId());
        String participantName = participationService.getParticipantNameForParticipation(participation.getParticipantId());
        
        // Get event name
        String eventName = participationService.getEventTitleForParticipation(participation.getEventId());
        
        emailService.sendParticipationConfirmationEmail(
            participantEmail,
            participantName,
            eventName
        );
        
        return ResponseEntity.ok().build();
    }
}

// Removed StatusUpdateRequest class to place it in its own file