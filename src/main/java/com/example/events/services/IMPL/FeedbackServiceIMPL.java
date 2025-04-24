package com.example.events.services.IMPL;

import com.example.events.entity.Feedback;
import com.example.events.repository.FeedbackRepository;
import com.example.events.services.interfaces.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackServiceIMPL implements IFeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceIMPL(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback addFeedback(Feedback feedback) {
        feedback.setSubmissionDate(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getFeedbacksByEvent(String eventId) {
        return feedbackRepository.findByEventId(eventId);
    }

    @Override
    public List<Feedback> getFeedbacksByParticipant(String participantId) {
        return feedbackRepository.findByParticipantId(participantId);
    }

    @Override
    public double getAverageRatingForEvent(String eventId) {
        List<Feedback> feedbacks = feedbackRepository.findByEventId(eventId);
        if (feedbacks.isEmpty()) return 0.0;
        return feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    @Override
    public void deleteFeedback(String id) {
        feedbackRepository.deleteById(id);
    }
}
