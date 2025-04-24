package com.example.events.services.interfaces;

import com.example.events.entity.Participation;
import java.util.List;

public interface IParticipationService {
    Participation addParticipation(Participation participation);
    Participation updateParticipationStatus(String id, String newStatus);
    void deleteParticipation(String id);
    List<Participation> getAllParticipations();
    List<Participation> getParticipationsByParticipant(String participantId);
    List<Participation> getParticipationsByEvent(String eventId);
    String getEventTitleForParticipation(String eventId);
    String getParticipantEmailForParticipation(String participantId);
}
