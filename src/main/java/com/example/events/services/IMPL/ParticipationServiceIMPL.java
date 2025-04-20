package com.example.events.services.IMPL;

import com.example.events.entity.Participation;
import com.example.events.entity.Event;
import com.example.events.repository.ParticipationRepository;
import com.example.events.repository.eventRepository;
import com.example.events.services.interfaces.IParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipationServiceIMPL implements IParticipationService {

    private final ParticipationRepository participationRepository;
    private final eventRepository eventRepository;

    @Autowired
    public ParticipationServiceIMPL(ParticipationRepository participationRepository, eventRepository eventRepository) {
        this.participationRepository = participationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Participation addParticipation(Participation participation) {
        // Check if event exists and get its capacity
        Event event = eventRepository.findById(participation.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + participation.getEventId()));

        // Get current number of active participants (not cancelled)
        long currentParticipants = participationRepository.findByEventId(participation.getEventId())
                .stream()
                .filter(p -> !p.getStatus().equals("CANCELLED"))
                .count();

        // Set participation date
        participation.setParticipationDate(java.time.LocalDateTime.now());

        // Check if event is at capacity
        if (currentParticipants >= event.getCapacity()) {
            participation.setStatus("WAITLISTED");
        } else {
            participation.setStatus("PENDING");
        }

        return participationRepository.save(participation);
    }

    @Override
    public Participation updateParticipationStatus(String id, String newStatus) {
        Participation participation = participationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participation with ID " + id + " not found."));

        // Validate the new status
        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }

        // Update the status
        participation.setStatus(newStatus);
        return participationRepository.save(participation);
    }

    private boolean isValidStatus(String status) {
        return status.equals("CONFIRMED") || status.equals("CANCELLED") || status.equals("WAITLISTED") || status.equals("PENDING");
    }

    @Override
    public List<Participation> getParticipationsByParticipant(String participantId) {
        return participationRepository.findByParticipantId(participantId);
    }

    @Override
    public List<Participation> getParticipationsByEvent(String eventId) {
        return participationRepository.findByEventId(eventId);
    }

    @Override
    public void deleteParticipation(String id) {
        participationRepository.deleteById(id);
    }

    @Override
    public List<Participation> getAllParticipations() {
        return participationRepository.findAll();
    }
}