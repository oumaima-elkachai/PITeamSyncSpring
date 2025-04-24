package com.example.events.controllers;

import com.example.events.entity.AuditLog;
import com.example.events.services.interfaces.IAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "http://localhost:4200")
public class AuditLogController {

    @Autowired
    private IAuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAllAuditLogs());
    }

    @GetMapping("/participation/{participationId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByParticipation(@PathVariable String participationId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByParticipation(participationId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEvent(eventId));
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByParticipant(@PathVariable String participantId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByParticipant(participantId));
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<AuditLog>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByDateRange(startDate, endDate));
    }
}