package com.example.events.controllers;

import com.example.events.entity.Event;
import com.example.events.services.interfaces.IEventService;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.entity.Participation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.example.events.entity.TypeEvent;


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
        try {
            List<Event> events = eventService.getAllEvents();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Event> addEvent(
            @RequestPart("event") Event event,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        try {
            Event savedEvent = eventService.addEvent(event, imageFile);
            return ResponseEntity.ok(savedEvent);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        try {
            Event event = eventService.getEventById(id);
            return ResponseEntity.ok(event);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Event> updateEvent(
            @PathVariable String id,
            @RequestPart("event") Event eventDetails,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDetails, imageFile));
    }

    @DeleteMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<Map<String, String>> removeParticipant(
            @PathVariable String eventId, 
            @PathVariable String participantId) {
        try {
            eventService.removeParticipantFromEvent(eventId, participantId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Participant removed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<Map<String, Object>> addParticipant(
            @PathVariable String eventId,
            @PathVariable String participantId,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String status = body != null ? body.get("status") : "PENDING";
            Participation participation = eventService.addParticipantToEvent(eventId, participantId, status);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Participant added successfully");
            response.put("data", participation);
            
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
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

    @GetMapping("/by-type/{eventType}")
    public ResponseEntity<List<Event>> getEventsByType(@PathVariable TypeEvent eventType) {
        try {
            List<Event> events = eventService.getEventsByType(eventType);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}