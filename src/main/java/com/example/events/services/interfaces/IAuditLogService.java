package com.example.events.services.interfaces;

import com.example.events.entity.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

public interface IAuditLogService {

     List<AuditLog> getAuditLogsByParticipation(String participationId);
    List<AuditLog> getAuditLogsByEvent(String eventId);
    List<AuditLog> getAuditLogsByParticipant(String participantId);
    List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> getAllAuditLogs();

}

