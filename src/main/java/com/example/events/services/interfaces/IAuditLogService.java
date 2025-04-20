package com.example.events.services.interfaces;

import com.example.events.entity.AuditLog;
import java.util.List;

public interface IAuditLogService {
    AuditLog createAuditLog(String action, String participationId, String eventId, String participantId, String details);
    List<AuditLog> getAuditLogsByParticipationId(String participationId);
    List<AuditLog> getAuditLogsByEventId(String eventId);
    List<AuditLog> getAuditLogsByParticipantId(String participantId);
    List<AuditLog> getAllAuditLogs();
    void deleteAuditLog(String id);
}
