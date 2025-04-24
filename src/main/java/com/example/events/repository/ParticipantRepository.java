package com.example.events.repository;
import com.example.events.entity.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends MongoRepository<Participant, String> {
    // Additional custom queries can be defined here
    Participant findByEmail(String email);
}

