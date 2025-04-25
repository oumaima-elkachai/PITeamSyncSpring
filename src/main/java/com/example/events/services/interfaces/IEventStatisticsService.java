package com.example.events.services.interfaces;

import com.example.events.entity.EventStatistics;

public interface IEventStatisticsService {
    EventStatistics getEventStatistics(String eventId);
    EventStatistics updateEventStatistics(String eventId);
}
