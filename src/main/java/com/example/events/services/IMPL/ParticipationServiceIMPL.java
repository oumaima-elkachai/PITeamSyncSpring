package com.example.events.services.IMPL;

import com.example.events.entity.Participation;
import com.example.events.entity.ParticipationStatus;
import com.example.events.entity.Event;
import com.example.events.entity.Participant;
import com.example.events.entity.AuditLog;
import com.example.events.repository.ParticipationRepository;
import com.example.events.repository.eventRepository;
import com.example.events.repository.ParticipantRepository;
import com.example.events.repository.AuditLogRepository;
import com.example.events.services.interfaces.IParticipationService;
import com.example.events.services.interfaces.IParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.events.exception.ResourceNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class ParticipationServiceIMPL implements IParticipationService {

    private final ParticipationRepository participationRepository;
    private final eventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final AuditLogRepository auditLogRepository;
    private final IParticipantService participantService;

    @Autowired
    public ParticipationServiceIMPL(
            ParticipationRepository participationRepository, 
            eventRepository eventRepository,
            ParticipantRepository participantRepository,
            AuditLogRepository auditLogRepository,
            IParticipantService participantService) {
        this.participationRepository = participationRepository;
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
        this.auditLogRepository = auditLogRepository;
        this.participantService = participantService;
    }

    private void createAuditLog(String action, Participation participation, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setParticipationId(participation.getId());
        auditLog.setEventId(participation.getEventId());
        auditLog.setParticipantId(participation.getParticipantId());
        auditLog.setPerformedBy("system"); // TODO: Replace with actual user when authentication is implemented
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDetails(details);
        
        auditLogRepository.save(auditLog);
    }

    @Override
    public Participation addParticipation(Participation participation) {
        Event event = eventRepository.findByIdEvent(participation.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found with idEvent: " + participation.getEventId()));

        // Get current number of active participants
        long currentParticipants = participationRepository.findByEventId(participation.getEventId())
                .stream()
                .filter(p -> !p.getParticipationStatus().equals(ParticipationStatus.REFUSED))
                .count();

        // Set participation date
        participation.setParticipationDate(LocalDateTime.now());

        // Check if event is at capacity
        if (event.getCapacity() != null && currentParticipants >= event.getCapacity()) {
            participation.setParticipationStatus(ParticipationStatus.WAITLISTED);
        } else {
            participation.setParticipationStatus(ParticipationStatus.PENDING);
        }

        Participation savedParticipation = participationRepository.save(participation);
        
        // Create audit log for new participation
        createAuditLog("ADD", savedParticipation, 
            String.format("Added participant to event with status: %s", participation.getParticipationStatus()));
        
        return savedParticipation;
    }

    @Override
    public Participation updateParticipationStatus(String id, String newStatus) {
        Participation participation = participationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participation not found with id: " + id));

        try {
            ParticipationStatus status = ParticipationStatus.valueOf(newStatus.toUpperCase());
            ParticipationStatus oldStatus = participation.getParticipationStatus();
            participation.setParticipationStatus(status);
            
            Participation updatedParticipation = participationRepository.save(participation);
            
            // Create audit log
            createAuditLog("UPDATE", updatedParticipation, 
                String.format("Status changed from %s to %s", oldStatus, status));
            
            return updatedParticipation;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }
    }

    @Override
    public Participation confirmParticipation(String id) {
        Participation participation = participationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participation not found with id: " + id));
        
        participation.setParticipationStatus(ParticipationStatus.CONFIRMED);
        return participationRepository.save(participation);
    }

    private boolean isValidStatus(String status) {
        try {
            ParticipationStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
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
        Participation participation = participationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Participation not found"));
            
        // Create audit log before deletion
        createAuditLog("REMOVE", participation, "Removed participant from event");
    
        
        participationRepository.deleteById(id);
    }

    @Override
    public List<Participation> getAllParticipations() {
        return participationRepository.findAll();
    }

    @Override
    public String getEventTitleForParticipation(String eventId) {
        return eventRepository.findById(eventId)
                .map(Event::getTitle)
                .orElse("Unknown Event");
    }

    @Override
    public String getParticipantEmailForParticipation(String participantId) {
        return participantRepository.findById(participantId)
                .map(Participant::getEmail)
                .orElse("Unknown Participant");
    }

    @Override
    public List<Participation> getParticipationsByEventTitle(String title) {
        List<Event> events = eventRepository.findByTitleContainingIgnoreCase(title);
        return events.stream()
                .flatMap(event -> participationRepository.findByEventId(event.getIdEvent()).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<Participation> getParticipationsByParticipantEmail(String email) {
        Participant participant = participantRepository.findByEmail(email);
        if (participant != null) {
            return participationRepository.findByParticipantId(participant.getId());
        }
        return Collections.emptyList();
    }

    @Override
    public String getParticipantNameForParticipation(String participantId) {
        return participantRepository.findById(participantId)
                .map(participant -> participant.getName())
                .orElse("Unknown Participant");
    }
}