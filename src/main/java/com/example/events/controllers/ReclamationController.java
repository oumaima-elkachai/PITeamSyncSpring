package com.example.events.controllers;

import com.example.events.entity.Reclamation;
import com.example.events.services.interfaces.IReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReclamationController {

    @Autowired
    private IReclamationService reclamationService;

    @PostMapping
    public ResponseEntity<Reclamation> submitReclamation(@RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.submitReclamation(reclamation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> updateReclamation(
            @PathVariable String id, 
            @RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.updateReclamation(id, reclamation));
    }

    @GetMapping
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Reclamation>> getReclamationsByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(reclamationService.getReclamationsByEvent(eventId));
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<Reclamation>> getReclamationsByParticipant(@PathVariable String participantId) {
        return ResponseEntity.ok(reclamationService.getReclamationsByParticipant(participantId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reclamation>> getReclamationsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(reclamationService.getReclamationsByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable String id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Reclamation> assignReclamation(
            @PathVariable String id,
            @RequestParam String assignedTo) {
        return ResponseEntity.ok(reclamationService.assignReclamation(id, assignedTo));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Reclamation> resolveReclamation(
            @PathVariable String id,
            @RequestParam String resolution) {
        return ResponseEntity.ok(reclamationService.resolveReclamation(id, resolution));
    }
}
