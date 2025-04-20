package com.example.events.services.interfaces;

import com.example.events.entity.Feedback;
import java.util.List;

public interface IFeedbackService {
    Feedback addFeedback(Feedback feedback);
    List<Feedback> getAllFeedbacks();
    List<Feedback> getFeedbacksByEvent(String eventId);
    List<Feedback> getFeedbacksByParticipant(String participantId);
    double getAverageRatingForEvent(String eventId);
    void deleteFeedback(String id);
}
