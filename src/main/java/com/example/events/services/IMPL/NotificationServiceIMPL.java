package com.example.events.services.IMPL;

import com.example.events.entity.NotificationMessage; // Updated import
import com.example.events.services.interfaces.INotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceIMPL implements INotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void notifyParticipant(String participantId, String message) {
        messagingTemplate.convertAndSend(
            "/topic/notifications/" + participantId, 
            new NotificationMessage("EVENT_UPDATE", message)
        );
    }

    @Override
    public void notifyEventUpdate(String eventId, String message) {
        messagingTemplate.convertAndSend(
            "/topic/events/" + eventId, 
            new NotificationMessage("EVENT_UPDATE", message)
        );
    }
}
