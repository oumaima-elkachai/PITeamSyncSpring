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
@Document(collection = "audit_logs")
public class AuditLog {
    @Id
    String id;
    String action; // "ADD", "REMOVE", "UPDATE"
    String participationId;
    String eventId;
    String participantId;
    String performedBy; // User who made the change
    LocalDateTime timestamp;
    String details;
}
