package com.example.events.repository;

import com.example.events.entity.Certification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface certificationRepository extends MongoRepository<Certification, String> {
    List<Certification> findByParticipantId(String participantId);

    List<Certification> findByEventId(String eventId);

    void deleteByEventId(String eventId);
}
