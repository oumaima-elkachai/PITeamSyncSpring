package com.example.events.services.IMPL;

import com.example.events.entity.Event;
import com.example.events.entity.Participation;
import com.example.events.entity.ParticipationStatus;
import com.example.events.entity.TypeEvent;
import com.example.events.repository.eventRepository;
import com.example.events.repository.ParticipationRepository;
import com.example.events.services.interfaces.IEventService;
import com.example.events.exception.ResourceNotFoundException;  // Fix import path
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class EventServiceIMPL implements IEventService {

    @Autowired
    private eventRepository eventRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ParticipationRepository participationRepository;

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
            try {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), Map.of());
                String imageUrl = (String) uploadResult.get("url");
                event.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new IOException("Failed to upload image: " + e.getMessage());
            }
        }
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(String id) {
        return eventRepository.findByIdEvent(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Override
    public void deleteEvent(String id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
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
        if (eventDetails.getCapacity() != null) {
            existingEvent.setCapacity(eventDetails.getCapacity());
        }
        if (eventDetails.getEventType() != null) {
            existingEvent.setEventType(eventDetails.getEventType());
        }

        return eventRepository.save(existingEvent);
    }

    @Override 
    public Event updateEvent(String id, Event eventDetails, MultipartFile imageFile) throws IOException {
        Event existingEvent = getEventById(id);
        validateEventDates(eventDetails);

        // Update fields if they are not null
        if (eventDetails.getTitle() != null) existingEvent.setTitle(eventDetails.getTitle());
        if (eventDetails.getDescription() != null) existingEvent.setDescription(eventDetails.getDescription());
        if (eventDetails.getTypeS() != null) existingEvent.setTypeS(eventDetails.getTypeS());
        if (eventDetails.getStartDate() != null) existingEvent.setStartDate(eventDetails.getStartDate());
        if (eventDetails.getEndDate() != null) existingEvent.setEndDate(eventDetails.getEndDate());
        if (eventDetails.getStartTime() != null) existingEvent.setStartTime(eventDetails.getStartTime());
        if (eventDetails.getEndTime() != null) existingEvent.setEndTime(eventDetails.getEndTime());
        if (eventDetails.getCapacity() != null) existingEvent.setCapacity(eventDetails.getCapacity());
        if (eventDetails.getEventType() != null) existingEvent.setEventType(eventDetails.getEventType());

        // Handle image update
        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), Map.of());
            String imageUrl = (String) uploadResult.get("url");
            existingEvent.setImageUrl(imageUrl);
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
    public Participation addParticipantToEvent(String eventId, String participantId, String status) {
        // Check if event exists
        Event event = getEventById(eventId);

        // Check if participation already exists
        if (participationRepository.existsByEventIdAndParticipantId(eventId, participantId)) {
            throw new IllegalStateException("Participant is already registered for this event");
        }

        // Create new participation
        Participation participation = new Participation();
        participation.setEventId(eventId);
        participation.setParticipantId(participantId);
        participation.setParticipationStatus(ParticipationStatus.valueOf(status.toUpperCase()));
        participation.setParticipationDate(LocalDateTime.now());

        // Save participation
        Participation savedParticipation = participationRepository.save(participation);

<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
=======

>>>>>>> Stashed changes
        // Update event's participant list
        if (event.getParticipantId() == null) {
            event.setParticipantId(new ArrayList<>());
        }
        event.getParticipantId().add(participantId);
        eventRepository.save(event);

        return savedParticipation;
    }

    @Override
    public void addParticipantToEvent(String eventId, String participantId) {
        Event event = getEventById(eventId);
        if (event.getParticipantId() == null) {
            event.setParticipantId(new ArrayList<>());
        }
        if (!event.getParticipantId().contains(participantId)) {
            event.getParticipantId().add(participantId);
            eventRepository.save(event);
        }
    }

    @Override
    public void removeParticipantFromEvent(String eventId, String participantId) {
        Event event = getEventById(eventId);
        if (event.getParticipantId() != null) {
            event.getParticipantId().remove(participantId);
            eventRepository.save(event);
        }
    }

    public List<Event> getEventsByDate(LocalDate date) {
        return eventRepository.findByStartDate(date);
    }

    @Override
    public List<Event> getEventsByType(TypeEvent eventType) {
        return eventRepository.findByEventType(eventType);
    }

}