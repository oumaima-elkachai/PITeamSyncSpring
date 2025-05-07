package com.example.events.repository;

import com.example.events.entity.EventAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EventAnalyticsRepository extends MongoRepository<EventAnalytics, String> {
    Optional<EventAnalytics> findByEventId(String eventId);
}
