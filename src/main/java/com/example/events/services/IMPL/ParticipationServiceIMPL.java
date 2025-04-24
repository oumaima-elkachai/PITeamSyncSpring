package com.example.events.services.IMPL;

import com.example.events.entity.Participation;
import com.example.events.entity.ParticipationStatus;
import com.example.events.entity.Event;

import com.example.events.entity.AuditLog;
import com.example.events.repository.ParticipationRepository;
import com.example.events.repository.eventRepository;
import com.example.events.repository.AuditLogRepository;
import com.example.events.services.interfaces.IParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParticipationServiceIMPL implements IParticipationService {

    private final ParticipationRepository participationRepository;
    private final eventRepository eventRepository;
    private final AuditLogRepository auditLogRepository;

    @Autowired
    public ParticipationServiceIMPL(ParticipationRepository participationRepository, 
                                   eventRepository eventRepository,
                                   AuditLogRepository auditLogRepository)
                                  {
        this.participationRepository = participationRepository;
        this.eventRepository = eventRepository;
        this.auditLogRepository = auditLogRepository;
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
                .filter(p -> !p.getParticipationS().equals(ParticipationStatus.REFUSED))
                .count();

        // Set participation date
        participation.setParticipationDate(LocalDateTime.now());

        // Check if event is at capacity
        if (event.getCapacity() != null && currentParticipants >= event.getCapacity()) {
            participation.setParticipationS(ParticipationStatus.WAITLISTED);
        } else {
            participation.setParticipationS(ParticipationStatus.PENDING);
        }

        Participation savedParticipation = participationRepository.save(participation);
        
        // Create audit log for new participation
        createAuditLog("ADD", savedParticipation, 
            String.format("Added participant to event with status: %s", participation.getParticipationS()));
        
        return savedParticipation;
    }

    @Override
    public Participation updateParticipationStatus(String id, String newStatus) {
        Participation participation = participationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Participation with ID " + id + " not found."));

        // Validate and convert the status
        ParticipationStatus status;
        try {
            status = ParticipationStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }

        ParticipationStatus oldStatus = participation.getParticipationS();
        participation.setParticipationS(status);
        
        Participation updatedParticipation = participationRepository.save(participation);
        
        // Create audit log for status update
        createAuditLog("UPDATE", updatedParticipation, 
            String.format("Status changed from %s to %s", oldStatus, status));
        
        return updatedParticipation;
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
}