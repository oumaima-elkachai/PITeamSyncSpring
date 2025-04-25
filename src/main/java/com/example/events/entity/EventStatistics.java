package com.example.events.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "event_statistics")
public class EventStatistics {
    @Id
    private String eventId;
    private String eventTitle;
    private int totalParticipants;
    private int confirmed;
    private int pending;
    private int cancelled;
    private int waitlisted;
}
