package com.example.events.services.interfaces;

public interface IEmailService {
    void sendParticipationConfirmationEmail(String toEmail, String participantName, String eventName);
}