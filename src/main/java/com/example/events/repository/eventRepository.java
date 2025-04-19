package com.example.events.repository;

import com.example.events.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface eventRepository extends MongoRepository<Event, String> {
    List<Event> findByStartDate(LocalDate startDate);
    List<Event> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<Event> findByParticipantIdContaining(String id);
}