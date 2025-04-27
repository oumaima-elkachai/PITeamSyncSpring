package tn.esprit.spring.teamsync.Services.MPL;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.context.event.EventListener;
import tn.esprit.spring.teamsync.Entity.*;
import tn.esprit.spring.teamsync.Repository.*;
import tn.esprit.spring.teamsync.Event.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    private void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @EventListener
    public void handleTaskAssignedEvent(TaskAssignedEvent event) {
        Employee assignee = employeeRepository.findById(event.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Task task = taskRepository.findById(event.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        String emailContent = String.format(
                "New Task Assignment\n\n" +
                        "Task: %s\n" +
                        "Description: %s\n" +
                        "Deadline: %s\n" +
                        "Project ID: %s",
                task.getTitle(),
                task.getDescription(),
                task.getDeadline().format(DateTimeFormatter.ISO_DATE),
                task.getProjectId()
        );
        sendSimpleEmail(assignee.getEmail(), "New Task Assignment", emailContent);
    }

    @EventListener
    public void handleExtensionApproved(ExtensionApprovedEvent event) {
        Task task = taskRepository.findById(event.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        Employee employee = employeeRepository.findById(task.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        String emailContent = String.format(
                "Extension Approved\n\n" +
                        "Task: %s\n" +
                        "New Deadline: %s\n" +
                        "Project ID: %s",
                task.getTitle(),
                task.getDeadline().format(DateTimeFormatter.ISO_DATE),
                task.getProjectId()
        );
        sendSimpleEmail(employee.getEmail(), "Extension Approved", emailContent);
    }

    @EventListener
    public void handleExtensionRefused(ExtensionRefusedEvent event) {
        Task task = taskRepository.findById(event.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        Employee employee = employeeRepository.findById(task.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        String emailContent = String.format(
                "Extension Refused\n\n" +
                        "Task: %s\n" +
                        "Deadline: %s\n" +
                        "Project ID: %s",
                task.getTitle(),
                task.getDeadline().format(DateTimeFormatter.ISO_DATE),
                task.getProjectId()
        );
        sendSimpleEmail(employee.getEmail(), "Extension Refused", emailContent);
    }

    private static class EmailException extends RuntimeException {
        public EmailException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}