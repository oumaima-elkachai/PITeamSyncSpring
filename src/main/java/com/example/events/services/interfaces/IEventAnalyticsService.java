package com.example.events.services.interfaces;

import com.example.events.entity.EventAnalytics;
import java.util.List;
import java.util.Map;

public interface IEventAnalyticsService {
    Map<String, Object> generateEventInsights(String eventId);
    List<String> generateRecommendations(String eventId);
    EventAnalytics getEventAnalytics(String eventId);
    EventAnalytics updateEventAnalytics(String eventId);
    double calculateEngagementScore(String eventId);
    double predictAttendance(String eventId);
}
