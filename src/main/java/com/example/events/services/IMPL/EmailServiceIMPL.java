package com.example.events.services.IMPL;

import com.example.events.services.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
>>>>>>> parent of a43303c (final commit)
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
=======
=======
>>>>>>> Stashed changes
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> parent of a43303c (final commit)
@Service
public class EmailServiceIMPL implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendParticipationConfirmationEmail(String toEmail, String participantName, String eventName) {
<<<<<<< HEAD
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
>>>>>>> parent of a43303c (final commit)
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your-email@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Participation Confirmation - " + eventName);
        message.setText("Dear " + participantName + ",\n\n"
                + "Your participation in " + eventName + " has been confirmed.\n"
                + "Thank you for joining!\n\n"
                + "Best regards,\nPITeamSync Team");

        mailSender.send(message);
<<<<<<< HEAD
=======
=======
>>>>>>> Stashed changes
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("rania.gasmi@esprit.tn");
            helper.setTo(toEmail);
            helper.setSubject("Participation Confirmation - " + eventName);

            String htmlContent = String.format("""
                <html>
                <body>
                    <h2>Participation Confirmation</h2>
                    <p>Dear %s,</p>
                    <p>Your participation in <strong>%s</strong> has been confirmed.</p>
                    <p>Thank you for joining!</p>
                    <br>
                    <p>Best regards,<br>PITeamSync Team</p>
                </body>
                </html>
                """, participantName, eventName);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> parent of a43303c (final commit)
    }
}