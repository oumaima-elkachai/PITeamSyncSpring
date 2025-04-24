package com.example.events.controllers;

import com.example.events.entity.Certification;
import com.example.events.services.interfaces.ICertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/certification")
public class CertificationController {

    @Autowired
    private ICertificationService certificationService;


    @PostMapping("/add")
    public Certification addCertification(@RequestBody Certification certification) {
        return certificationService.addCertification(certification);
    }

    @GetMapping("/all")
    public List<Certification> getAll() {
        return certificationService.getAllCertifications();
    }

    @GetMapping("/{id}")
    public Certification getById(@PathVariable String id) {
        return certificationService.getCertificationById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        certificationService.deleteCertification(id);
    }

    @PutMapping("/update/{id}")
    public Certification update(@PathVariable String id, @RequestBody Certification certification) {
        return certificationService.updateCertification(id, certification);

    }

    @PostMapping("/assign")
    public ResponseEntity<String> assignCertification(
            @RequestParam String certificationId,
            @RequestParam String participantId,
            @RequestParam String eventId) {
        certificationService.assignCertification(certificationId, participantId, eventId);
        return ResponseEntity.ok("Certification assigned successfully.");
    }


    @GetMapping("/byParticipant/{participantId}")
    public ResponseEntity<List<Certification>> getByParticipant(@PathVariable String participantId) {
        return ResponseEntity.ok(certificationService.getCertificationsByParticipant(participantId));
    }


    @GetMapping("/byEvent/{eventId}")
    public ResponseEntity<List<Certification>> getByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(certificationService.getCertificationsByEvent(eventId));
    }

    @DeleteMapping("/deleteByEvent/{eventId}")
    public ResponseEntity<Void> deleteByEvent(@PathVariable String eventId) {
        certificationService.deleteCertificationsByEvent(eventId);
        return ResponseEntity.noContent().build();
    }







}
