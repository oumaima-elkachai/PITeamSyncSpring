package com.example.events.repository;

import com.example.events.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {
    List<Feedback> findByEventId(String eventId);
    List<Feedback> findByParticipantId(String participantId);
    List<Feedback> findByEventIdAndRating(String eventId, Integer rating);
}
