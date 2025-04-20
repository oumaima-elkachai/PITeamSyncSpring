package com.example.events.services.interfaces;

public interface INotificationService {
    void notifyParticipant(String participantId, String message);
    void notifyEventUpdate(String eventId, String message);
}
