package com.example.events.repository;

import com.example.events.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByParticipationId(String participationId);
    List<AuditLog> findByEventId(String eventId);
    List<AuditLog> findByParticipantId(String participantId);
}
