package com.example.events.services.interfaces;

import com.example.events.entity.Reclamation;
import java.util.List;

public interface IReclamationService {
    Reclamation submitReclamation(Reclamation reclamation);
    Reclamation updateReclamation(String id, Reclamation reclamation);
    List<Reclamation> getAllReclamations();
    List<Reclamation> getReclamationsByEvent(String eventId);
    List<Reclamation> getReclamationsByParticipant(String participantId);
    List<Reclamation> getReclamationsByStatus(String status);
    void deleteReclamation(String id);
    Reclamation assignReclamation(String id, String assignedTo);
    Reclamation resolveReclamation(String id, String resolution);
}
