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
@Document(collection = "participations")
public class Participation {

    @Id
    String id; // Unique ID for the participation record

    String participantId; // ID of the participant
    String eventId;       // ID of the event

    LocalDateTime participationDate; // Date and time of participation
    private ParticipationStatus ParticipationS; 
}
