package com.example.events.repository;

import com.example.events.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface eventRepository extends MongoRepository<Event, String> {
    List<Event> findByStartDate(LocalDate startDate);
    List<Event> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<Event> findByParticipantIdContaining(String id);
    List<Event> findByTitleContainingIgnoreCase(String title);

    @Query(value = "{ 'id': ?0 }")
    Optional<Event> findById(String id);

    Optional<Event> findByIdEvent(String idEvent);
}