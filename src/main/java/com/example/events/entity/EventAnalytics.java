package com.example.events.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "event_analytics")
public class EventAnalytics {
    @Id
    private String id;
    private String eventId;
    private String eventType;
    private Double participationRate = 0.0;
    private Double averageDuration = 0.0;
    private Integer totalParticipants = 0;
    private String mostActiveTimeSlot = "Not available";
    private String popularityTrend = "LOW";
    private Double engagementScore = 0.0;
    private Double predictedAttendance = 0.0;
    private Long timeSpent = 0L;
    private Integer interactionCount = 0;
}
