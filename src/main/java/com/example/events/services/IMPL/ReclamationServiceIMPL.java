package com.example.events.services.IMPL;

import com.example.events.entity.Reclamation;
import com.example.events.repository.ReclamationRepository;
import com.example.events.services.interfaces.IReclamationService;
//import com.example.events.services.interfaces.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReclamationServiceIMPL implements IReclamationService {

    private final ReclamationRepository reclamationRepository;

    @Autowired
    public ReclamationServiceIMPL(ReclamationRepository reclamationRepository)
                            {
        this.reclamationRepository = reclamationRepository;
    }

    @Override
    public Reclamation submitReclamation(Reclamation reclamation) {
        reclamation.setSubmissionDate(LocalDateTime.now());
        reclamation.setStatus("PENDING");
        Reclamation saved = reclamationRepository.save(reclamation);
        
     
      
        
        return saved;
    }

    @Override
    public Reclamation updateReclamation(String id, Reclamation reclamation) {
        Reclamation existing = reclamationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reclamation not found"));
        
        existing.setStatus(reclamation.getStatus());
        existing.setPriority(reclamation.getPriority());
        existing.setDescription(reclamation.getDescription());
        
        return reclamationRepository.save(existing);
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public List<Reclamation> getReclamationsByEvent(String eventId) {
        return reclamationRepository.findByEventId(eventId);
    }

    @Override
    public List<Reclamation> getReclamationsByParticipant(String participantId) {
        return reclamationRepository.findByParticipantId(participantId);
    }

    @Override
    public List<Reclamation> getReclamationsByStatus(String status) {
        return reclamationRepository.findByStatus(status);
    }

    @Override
    public void deleteReclamation(String id) {
        reclamationRepository.deleteById(id);
    }

    @Override
    public Reclamation assignReclamation(String id, String assignedTo) {
        Reclamation reclamation = reclamationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reclamation not found"));
        
        reclamation.setAssignedTo(assignedTo);
        reclamation.setStatus("IN_PROGRESS");
        
        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation resolveReclamation(String id, String resolution) {
        Reclamation reclamation = reclamationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reclamation not found"));
        
        reclamation.setResolution(resolution);
        reclamation.setStatus("RESOLVED");
        reclamation.setResolutionDate(LocalDateTime.now());
        
        Reclamation resolved = reclamationRepository.save(reclamation);
        
     
        
        return resolved;
    }
}
