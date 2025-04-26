package com.example.events.services.IMPL;

import com.example.events.entity.EventStatistics;
import com.example.events.entity.ParticipationStatus;
import com.example.events.repository.EventStatisticsRepository;
import com.example.events.repository.ParticipationRepository;
import com.example.events.repository.eventRepository;
import com.example.events.services.interfaces.IEventStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventStatisticsIMPL implements IEventStatisticsService {

    @Autowired
    private EventStatisticsRepository statisticsRepository;
    
    @Autowired
    private ParticipationRepository participationRepository;
    
    @Autowired
    private eventRepository eventRepository;

    @Override
    public EventStatistics getEventStatistics(String eventId) {
        return statisticsRepository.findByEventId(eventId);
    }

 /*  @Override
    public EventStatistics updateEventStatistics(String eventId) {
        EventStatistics statistics = new EventStatistics();
        statistics.setEventId(eventId);
        
        eventRepository.findById(eventId).ifPresent(event -> 
            statistics.setEventTitle(event.getTitle())
        );

        statistics.setTotalParticipants((int) participationRepository.countByEventId(eventId));
        statistics.setConfirmed((int) participationRepository.findByEventId(eventId).stream()
                .filter(p -> p.getParticipationS() == ParticipationStatus.CONFIRMED).count());
        statistics.setPending((int) participationRepository.findByEventId(eventId).stream()
                .filter(p -> p.getParticipationS() == ParticipationStatus.PENDING).count());
        statistics.setWaitlisted((int) participationRepository.findByEventId(eventId).stream()
                .filter(p -> p.getParticipationS() == ParticipationStatus.WAITLISTED).count());
                
        return statisticsRepository.save(statistics);
    }*/
}
