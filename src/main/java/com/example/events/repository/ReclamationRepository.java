package com.example.events.repository;

import com.example.events.entity.Reclamation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReclamationRepository extends MongoRepository<Reclamation, String> {
    List<Reclamation> findByEventId(String eventId);
    List<Reclamation> findByParticipantId(String participantId);
    List<Reclamation> findByStatus(String status);
    List<Reclamation> findByPriority(String priority);
    List<Reclamation> findByAssignedTo(String assignedTo);
}
