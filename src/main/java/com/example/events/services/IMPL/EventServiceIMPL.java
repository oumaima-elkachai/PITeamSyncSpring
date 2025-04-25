package com.example.events.services.IMPL;

import com.example.events.entity.Event;
import com.example.events.repository.eventRepository;
import com.example.events.services.interfaces.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class EventServiceIMPL implements IEventService {

    @Autowired
    private eventRepository eventRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Page<Event> getAllEventsPaginated(int page, int size) {
        return eventRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event addEvent(Event event) {
        validateEventDates(event);
        return eventRepository.save(event);
    }

    @Override
    public Event addEvent(Event event, MultipartFile imageFile) throws IOException {
        validateEventDates(event);
        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), Map.of());
            String imageUrl = (String) uploadResult.get("url");
            event.setImageUrl(imageUrl);
        }
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(String id) {
        return eventRepository.findByIdEvent(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    @Override
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event updateEvent(String id, Event eventDetails) {
        Event existingEvent = getEventById(id);
        validateEventDates(eventDetails);

        // Update only non-null fields from eventDetails
        if (eventDetails.getTitle() != null) {
            existingEvent.setTitle(eventDetails.getTitle());
        }
        if (eventDetails.getDescription() != null) {
            existingEvent.setDescription(eventDetails.getDescription());
        }
        if (eventDetails.getTypeS() != null) {
            existingEvent.setTypeS(eventDetails.getTypeS());
        }
        if (eventDetails.getStartDate() != null) {
            existingEvent.setStartDate(eventDetails.getStartDate());
        }
        if (eventDetails.getEndDate() != null) {
            existingEvent.setEndDate(eventDetails.getEndDate());
        }
        if (eventDetails.getStartTime() != null) {
            existingEvent.setStartTime(eventDetails.getStartTime());
        }
        if (eventDetails.getEndTime() != null) {
            existingEvent.setEndTime(eventDetails.getEndTime());
        }

        return eventRepository.save(existingEvent);
    }

    private void validateEventDates(Event event) {
        if (event.getEndDate().isBefore(event.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        if (event.getStartTime() != null && event.getEndTime() != null
                && event.getEndDate().equals(event.getStartDate())
                && event.getEndTime().isBefore(event.getStartTime())) {
            throw new IllegalArgumentException("End time cannot be before start time on same day");
        }
    }

    @Override
    public List<Event> getEventsByStartDate(LocalDate startDate) {
        return eventRepository.findByStartDate(startDate);
    }

    @Override
    public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByStartDateBetween(startDate, endDate);
    }

    @Override
    public void addParticipantToEvent(String eventId, String participantId) {
        // Implementation needed
    }

    @Override
    public void removeParticipantFromEvent(String eventId, String participantId) {
        // Implementation needed
    }

    public List<Event> getEventsByDate(LocalDate date) {
        return eventRepository.findByStartDate(date);
    }

}