package com.example.events.services.IMPL;

import com.example.events.entity.AuditLog;
import com.example.events.repository.AuditLogRepository;
import com.example.events.services.interfaces.IAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogServiceIMPL implements IAuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditLogServiceIMPL(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public AuditLog createAuditLog(String action, String participationId, String eventId, String participantId, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setParticipationId(participationId);
        auditLog.setEventId(eventId);
        auditLog.setParticipantId(participantId);
        auditLog.setPerformedBy("system"); // TODO: Replace with actual user when authentication is implemented
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDetails(details);
        
        return auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLog> getAuditLogsByParticipationId(String participationId) {
        return auditLogRepository.findByParticipationId(participationId);
    }

    @Override
    public List<AuditLog> getAuditLogsByEventId(String eventId) {
        return auditLogRepository.findByEventId(eventId);
    }

    @Override
    public List<AuditLog> getAuditLogsByParticipantId(String participantId) {
        return auditLogRepository.findByParticipantId(participantId);
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    public void deleteAuditLog(String id) {
        auditLogRepository.deleteById(id);
    }
}
