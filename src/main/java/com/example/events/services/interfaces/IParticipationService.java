package com.example.events.services.interfaces;

import com.example.events.entity.Participation;

import java.util.List;

public interface IParticipationService {
    Participation addParticipation(Participation participation);
    Participation updateParticipationStatus(String id, String newStatus);
    List<Participation> getParticipationsByParticipant(String participantId);
    List<Participation> getParticipationsByEvent(String eventId);
    void deleteParticipation(String id);
    List<Participation> getAllParticipations();

    /**
     * Checks if an event has reached its capacity
     * @param eventId the ID of the event to check
     * @return true if the event is full, false otherwise
     */
    default boolean isEventFull(String eventId) {
        return false;
    }
}
