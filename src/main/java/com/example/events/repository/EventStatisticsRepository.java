package com.example.events.repository;

import com.example.events.entity.EventStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventStatisticsRepository extends MongoRepository<EventStatistics, String> {
    
    @Query(value = "{ 'eventId': ?0 }")  
    EventStatistics findByEventId(String eventId);
}
