package com.example.events.repository;

import com.example.events.entity.Participation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.events.entity.ParticipationStatus;

@Repository
public interface ParticipationRepository extends MongoRepository<Participation, String> {
    List<Participation> findByParticipantId(String participantId);
    List<Participation> findByEventId(String eventId);
    List<Participation> findByParticipationS(ParticipationStatus status);
    long countByEventId(String eventId);
}
