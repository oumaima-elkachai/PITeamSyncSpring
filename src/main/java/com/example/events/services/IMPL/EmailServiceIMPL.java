package com.example.events.services.IMPL;

import com.example.events.services.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceIMPL implements IEmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Override
    public void sendParticipationConfirmationEmail(String toEmail, String participantName, String eventName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Participation Confirmation - " + eventName);
        message.setText("Dear " + participantName + ",\n\n"
                + "Your participation in " + eventName + " has been confirmed.\n"
                + "Thank you for joining!\n\n"
                + "Best regards,\nPITeamSync Team");
        
        mailSender.send(message);
    }
}