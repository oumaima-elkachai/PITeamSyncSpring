package com.example.events.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "reclamations")
public class Reclamation {
    @Id
    String id;
    String participantId;
    String eventId;
    String subject;
    String description;
    LocalDateTime submissionDate;
    String status; // "PENDING", "IN_PROGRESS", "RESOLVED", "REJECTED"
    String priority; // "LOW", "MEDIUM", "HIGH", "URGENT"
    String resolution;
    LocalDateTime resolutionDate;
    String assignedTo;
}
