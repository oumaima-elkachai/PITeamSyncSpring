package com.example.events.controllers;

import com.example.events.entity.Feedback;
import com.example.events.services.interfaces.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackController {

    @Autowired
    private IFeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Feedback> addFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.addFeedback(feedback));
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByEvent(eventId));
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByParticipant(@PathVariable String participantId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByParticipant(participantId));
    }

    @GetMapping("/event/{eventId}/rating")
    public ResponseEntity<Double> getEventAverageRating(@PathVariable String eventId) {
        return ResponseEntity.ok(feedbackService.getAverageRatingForEvent(eventId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
