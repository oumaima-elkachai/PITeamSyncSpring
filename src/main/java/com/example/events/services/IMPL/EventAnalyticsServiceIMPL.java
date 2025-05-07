package com.example.events.services.IMPL;

import com.example.events.entity.Event;
import com.example.events.entity.EventAnalytics;
import com.example.events.entity.Participation;
import com.example.events.entity.ParticipationStatus;
import com.example.events.repository.EventAnalyticsRepository;
import com.example.events.repository.eventRepository;
import com.example.events.repository.ParticipationRepository;
import com.example.events.services.interfaces.IEventAnalyticsService;
import com.example.events.exception.ResourceNotFoundException;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventAnalyticsServiceIMPL implements IEventAnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(EventAnalyticsServiceIMPL.class);

    @Autowired
    private eventRepository eventRepository;
    
    @Autowired
    private ParticipationRepository participationRepository;
    
    @Autowired
    private EventAnalyticsRepository analyticsRepository;

    @Override
    public Map<String, Object> generateEventInsights(String eventId) {
        logger.info("Generating insights for event: {}", eventId);
        try {
            Event event = eventRepository.findByIdEvent(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
            
            List<Participation> participations = participationRepository.findByEventId(eventId);
            
            // Calculate core metrics
            double participationRate = calculateParticipationRate(event, participations);
            double predictedAttendance = predictAttendance(eventId); // Updated this line
            double engagementScore = calculateEngagementScore(eventId);
            
            // Calculate additional metrics
            int confirmedParticipants = (int) participations.stream()
                .filter(p -> p.getParticipationStatus() == ParticipationStatus.CONFIRMED)
                .count();
            
            int totalCapacity = event.getCapacity() != null ? event.getCapacity() : 0;
            
            Map<String, Object> insights = new HashMap<>();
            insights.put("participationRate", Math.min(1.0, participationRate));
            insights.put("predictedAttendance", Math.max(predictedAttendance, confirmedParticipants));
            insights.put("engagementScore", engagementScore);
            insights.put("confirmedParticipants", confirmedParticipants);
            insights.put("totalCapacity", totalCapacity);
            insights.put("availableSpots", Math.max(0, totalCapacity - confirmedParticipants));
            insights.put("eventType", event.getEventType());
            insights.put("eventStatus", event.getTypeS());
            
            return insights;
        } catch (Exception e) {
            logger.error("Error generating insights for event {}: {}", eventId, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<String> generateRecommendations(String eventId) {
        Map<String, Object> insights = generateEventInsights(eventId);
        List<String> recommendations = new ArrayList<>();
        
        double participationRate = (double) insights.get("participationRate");
        double engagementScore = (double) insights.get("engagementScore");
        
        if (participationRate < 0.5) {
            recommendations.add("Consider sending reminder notifications");
            recommendations.add("Try scheduling the event at a different time");
        }
        
        if (engagementScore < 0.6) {
            recommendations.add("Include more interactive elements in the event");
            recommendations.add("Consider breaking the event into shorter segments");
        }
        
        return recommendations;
    }

    @Override
    public EventAnalytics getEventAnalytics(String eventId) {
        return analyticsRepository.findByEventId(eventId)
                .orElseGet(() -> updateEventAnalytics(eventId));
    }

    @Override
    public EventAnalytics updateEventAnalytics(String eventId) {
        Event event = eventRepository.findByIdEvent(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        List<Participation> participations = participationRepository.findByEventId(eventId);
        
        EventAnalytics analytics = analyticsRepository.findByEventId(eventId)
                .orElse(new EventAnalytics());
        
        try {
            // Basic information
            analytics.setId(analytics.getId() != null ? analytics.getId() : UUID.randomUUID().toString());
            analytics.setEventId(eventId);
            analytics.setEventType(event.getEventType() != null ? event.getEventType().toString() : "UNKNOWN");
            
            // Core metrics
            double participationRate = calculateParticipationRate(event, participations);
            double predictedAtt = calculatePredictedAttendance(event, participations);
            double engagementScore = calculateEngagementScore(eventId);
            
            analytics.setParticipationRate(participationRate);
            analytics.setPredictedAttendance(predictedAtt);
            analytics.setEngagementScore(engagementScore);
            
            // Additional metrics
            analytics.setTotalParticipants(participations.size());
            analytics.setAverageDuration(calculateAverageDuration(participations));
            analytics.setMostActiveTimeSlot(determineMostActiveTimeSlot(event));
            analytics.setPopularityTrend(calculatePopularityTrend(event, participations));
            
            // Interaction metrics
            long timeSpent = calculateTotalTimeSpent(participations);
            analytics.setTimeSpent(timeSpent);
            analytics.setInteractionCount(participations.size());
            
            return analyticsRepository.save(analytics);
        } catch (Exception e) {
            logger.error("Error updating analytics for event {}: {}", eventId, e.getMessage());
            throw new RuntimeException("Failed to update analytics", e);
        }
    }

    private Double calculateAverageDuration(List<Participation> participations) {
        if (participations.isEmpty()) {
            return 0.0;
        }
        // For now, return a simple average based on participation dates
        return participations.stream()
                .filter(p -> p.getParticipationDate() != null)
                .count() * 1.0 / participations.size();
    }

    private String determineMostActiveTimeSlot(Event event) {
        if (event.getStartTime() != null && event.getEndTime() != null) {
            return event.getStartTime() + " - " + event.getEndTime();
        }
        return "All Day";
    }

    private String calculatePopularityTrend(Event event, List<Participation> participations) {
        if (event.getCapacity() == null || event.getCapacity() == 0) {
            return "LOW";
        }
        
        int confirmedCount = (int) participations.stream()
                .filter(p -> p.getParticipationStatus() == ParticipationStatus.CONFIRMED)
                .count();
        
        if (confirmedCount >= event.getCapacity() * 0.8) {
            return "HIGH";
        } else if (confirmedCount >= event.getCapacity() * 0.5) {
            return "MEDIUM";
        }
        return "LOW";
    }

    private long calculateTotalTimeSpent(List<Participation> participations) {
        // For now, return a simple calculation based on confirmed participants
        return participations.stream()
                .filter(p -> p.getParticipationStatus() == ParticipationStatus.CONFIRMED)
                .count() * 60; // Assuming 60 minutes per participant
    }

    @Override
    public double calculateEngagementScore(String eventId) {
        List<Participation> participations = participationRepository.findByEventId(eventId);
        return participations.stream()
                .mapToDouble(p -> {
                    double timeSpent = p.getParticipationDate() != null ? 1.0 : 0.0;
                    return timeSpent * 0.6;
                })
                .average()
                .orElse(0.0);
    }

    @Override
    public double predictAttendance(String eventId) {
        Event event = eventRepository.findByIdEvent(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
        List<Participation> participations = participationRepository.findByEventId(eventId);
        return calculatePredictedAttendance(event, participations);
    }

    private double calculateParticipationRate(Event event, List<Participation> participations) {
        if (event.getCapacity() == null || event.getCapacity() == 0) {
            return 0.0;
        }
        
        long confirmedParticipants = participations.stream()
            .filter(p -> p.getParticipationStatus() == ParticipationStatus.CONFIRMED)
            .count();
            
        return (double) confirmedParticipants / event.getCapacity();
    }

    private double calculatePredictedAttendance(Event event, List<Participation> participations) {
        int confirmedCount = (int) participations.stream()
            .filter(p -> p.getParticipationStatus() == ParticipationStatus.CONFIRMED)
            .count();
        
        int pendingCount = (int) participations.stream()
            .filter(p -> p.getParticipationStatus() == ParticipationStatus.PENDING)
            .count();
            
        // Predict using confirmed + percentage of pending
        return confirmedCount + (pendingCount * 0.7);
    }
}
