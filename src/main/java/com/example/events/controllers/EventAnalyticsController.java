package com.example.events.controllers;

import com.example.events.entity.EventAnalytics;
import com.example.events.services.interfaces.IEventAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:4200")
public class EventAnalyticsController {

    private static final Logger logger = LoggerFactory.getLogger(EventAnalyticsController.class);

    @Autowired
    private IEventAnalyticsService analyticsService;

    @GetMapping("/events/{eventId}/insights")
    public ResponseEntity<?> getEventInsights(@PathVariable String eventId) {
        logger.info("Received request for event insights: {}", eventId);
        try {
            return ResponseEntity.ok(analyticsService.generateEventInsights(eventId));
        } catch (Exception e) {
            logger.error("Error getting insights for event {}: {}", eventId, e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage(), "eventId", eventId));
        }
    }

    @GetMapping("/events/{eventId}/recommendations")
    public ResponseEntity<?> getEventRecommendations(@PathVariable String eventId) {
        return ResponseEntity.ok(analyticsService.generateRecommendations(eventId));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventAnalytics> getEventAnalytics(@PathVariable String eventId) {
        logger.info("Fetching analytics for event: {}", eventId);
        EventAnalytics analytics = analyticsService.getEventAnalytics(eventId);
        logger.info("Analytics response: {}", analytics);
        return ResponseEntity.ok(analytics);
    }

    @PostMapping("/events/{eventId}/update")
    public ResponseEntity<EventAnalytics> updateEventAnalytics(@PathVariable String eventId) {
        return ResponseEntity.ok(analyticsService.updateEventAnalytics(eventId));
    }
}
