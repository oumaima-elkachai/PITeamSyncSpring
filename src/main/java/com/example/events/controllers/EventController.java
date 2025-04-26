package com.example.events.controllers;

import com.example.events.entity.Event;
import com.example.events.services.interfaces.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    @Autowired
    private IEventService eventService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Event> addEvent(
            @RequestPart("event") Event event,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        Event savedEvent = eventService.addEvent(event, imageFile);
        return ResponseEntity.ok(savedEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Event> updateEvent(
            @PathVariable String id,
            @RequestPart("event") Event eventDetails,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDetails, imageFile));
    }

    @DeleteMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<String> removeParticipant(@PathVariable String eventId, @PathVariable String participantId) {
        eventService.removeParticipantFromEvent(eventId, participantId);
        return ResponseEntity.ok("Participant removed successfully");
    }

    @PostMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<String> addParticipant(@PathVariable String eventId, @PathVariable String participantId) {
        eventService.addParticipantToEvent(eventId, participantId);
        return ResponseEntity.ok("Participant added successfully");
    }

    @GetMapping("/by-date")
    public List<Event> getEventByDate(@RequestParam String startDate) {
        return eventService.getEventsByStartDate(LocalDate.parse(startDate));
    }

    @GetMapping("/by-range")
    public List<Event> getEventsByRange(@RequestParam String startDate, @RequestParam String endDate) {
        return eventService.getEventsByDateRange(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @DeleteMapping("/clear-db")
    @PreAuthorize("hasRole('ADMIN')")
    public String clearDatabase() {
        mongoTemplate.dropCollection("events");
        return "Database cleared successfully";
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Event>> getAllEventsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getAllEventsPaginated(page, size));
    }

    @GetMapping("/today")
    public ResponseEntity<List<Event>> getTodayEvents() {
        LocalDate today = LocalDate.now();
        List<Event> events = eventService.getEventsByDate(today);
        return ResponseEntity.ok(events);
    }


}