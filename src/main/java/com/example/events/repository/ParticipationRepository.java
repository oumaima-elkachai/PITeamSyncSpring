package com.example.events.repository;

import com.example.events.entity.Participation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends MongoRepository<Participation, String> {
    List<Participation> findByParticipantId(String participantId);
    List<Participation> findByEventId(String eventId);
}
