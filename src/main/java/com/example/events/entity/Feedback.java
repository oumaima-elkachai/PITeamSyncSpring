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
@Document(collection = "feedbacks")
public class Feedback {
    @Id
    String id;
    String participantId;
    String eventId;
    Integer rating; // 1-5 stars
    String comment;
    LocalDateTime submissionDate;
    String type; // "EVENT", "ORGANIZATION", "CONTENT"
    Boolean isAnonymous;
}
