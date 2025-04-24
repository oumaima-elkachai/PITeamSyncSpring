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
    public List<AuditLog> getAuditLogsByParticipation(String participationId) {
        return auditLogRepository.findByParticipationId(participationId);
    }

    @Override
    public List<AuditLog> getAuditLogsByEvent(String eventId) {
        return auditLogRepository.findByEventId(eventId);
    }

    @Override
    public List<AuditLog> getAuditLogsByParticipant(String participantId) {
        return auditLogRepository.findByParticipantId(participantId);
    }

    @Override
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate);
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }



}
