package com.example.events.services.interfaces;

import com.example.events.entity.Event;
import com.example.events.entity.Participation;
import com.example.events.entity.TypeEvent;
import com.example.events.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface IEventService {
    Page<Event> getAllEventsPaginated(int page, int size);
    List<Event> getAllEvents() throws Exception;
    Event addEvent(Event event) throws Exception;
    Event addEvent(Event event, MultipartFile imageFile) throws IOException, Exception;
    Event getEventById(String id) throws ResourceNotFoundException;
    void deleteEvent(String id) throws ResourceNotFoundException;
    Event updateEvent(String id, Event eventDetails) throws ResourceNotFoundException;
    Event updateEvent(String id, Event eventDetails, MultipartFile imageFile) throws IOException, ResourceNotFoundException;
    void addParticipantToEvent(String eventId, String participantId);
    Participation addParticipantToEvent(String eventId, String participantId, String status);
    void removeParticipantFromEvent(String eventId, String participantId);
    List<Event> getEventsByStartDate(LocalDate startDate);
    List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate);
    List<Event> getEventsByDate(LocalDate today);
    List<Event> getEventsByType(TypeEvent eventType);
}