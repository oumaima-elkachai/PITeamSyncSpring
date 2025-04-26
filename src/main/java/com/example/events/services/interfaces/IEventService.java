package com.example.events.services.interfaces;

import com.example.events.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface IEventService {
    Page<Event> getAllEventsPaginated(int page, int size);
    List<Event> getAllEvents();
    Event addEvent(Event event);
    Event addEvent(Event event, MultipartFile imageFile) throws IOException;
    Event getEventById(String id);
    void deleteEvent(String id);
    Event updateEvent(String id, Event eventDetails);
    Event updateEvent(String id, Event eventDetails, MultipartFile imageFile) throws IOException;
    void addParticipantToEvent(String eventId, String participantId);
    void removeParticipantFromEvent(String eventId, String participantId);
    List<Event> getEventsByStartDate(LocalDate startDate);
    List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate);

    List<Event> getEventsByDate(LocalDate today);
}