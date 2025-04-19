package com.example.events.controllers;

import com.example.events.entity.Participant;
import com.example.events.services.interfaces.IParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private IParticipantService participantService;

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantService.getAllParticipants();
    }

    @PostMapping("/add")
    public ResponseEntity<Participant> addParticipant(@RequestBody Participant participant) {
        Participant savedParticipant = participantService.addParticipant(participant);
        return ResponseEntity.ok(savedParticipant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable String id) {
        Participant participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(participant);
    }

    @DeleteMapping("/{id}")
    public void deleteParticipant(@PathVariable String id) {
        participantService.deleteParticipant(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable String id, @RequestBody Participant participantDetails) {
        Participant updatedParticipant = participantService.updateParticipant(id, participantDetails);
        return ResponseEntity.ok(updatedParticipant);
    }
}
