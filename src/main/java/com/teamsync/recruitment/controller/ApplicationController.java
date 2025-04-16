package com.teamsync.recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsync.recruitment.entity.Application;
import com.teamsync.recruitment.exception.ApplicationNotFoundException;
import com.teamsync.recruitment.exception.JobNotFoundException;
import com.teamsync.recruitment.repository.JobPostingRepository;
import com.teamsync.recruitment.repository.ApplicationRepository;
import com.teamsync.recruitment.entity.JobPosting;

import com.teamsync.recruitment.service.CloudinaryService;
import com.teamsync.recruitment.service.EmailService;
import com.teamsync.recruitment.service.FileStorageService;
import com.teamsync.recruitment.service.interfaces.ApplicationService;
import com.teamsync.recruitment.service.interfaces.JobPostingService;
import com.teamsync.recruitment.service.interfaces.NotificationService;
import io.github.classgraph.Resource;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
public class ApplicationController {


    private final ApplicationService applicationService;


    private final FileStorageService fileStorageService; //  Injection du service


    private final JobPostingRepository jobPostingRepository;


    private final ApplicationRepository applicationRepository;


    private final JobPostingService jobPostingService;

    private final EmailService emailService;

    private final NotificationService notificationService;

    private final CloudinaryService cloudinaryService;



    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody Application application, String jobId) {

        boolean alreadyApplied = applicationRepository
                .existsByJobIdAndCandidateId(application.getJobId(), application.getCandidateId());

        if (alreadyApplied) {
            throw new RuntimeException("Vous avez déjà postulé à ce job.");
        }


        return ResponseEntity.ok(applicationService.createApplication(application,jobId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Application> updateApplicationStatus(@PathVariable String id, @RequestParam String status) {
        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/{jobId}/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Application> applyToJob(
            @PathVariable String jobId,
            @RequestParam("candidateId") String candidateId,
            @RequestPart("application") String applicationJson, // JSON sous forme de texte
            @RequestPart("file") MultipartFile file

    ) {
        // 1. Log du JSON reçu
        System.out.println("JSON reçu: " + applicationJson);

        // 2. Tentative de conversion du JSON en objet Application
        ObjectMapper objectMapper = new ObjectMapper();
        Application application;
        try {
            application = objectMapper.readValue(applicationJson, Application.class);

            // 3. Log de l'objet Application après la conversion
            System.out.println("Application convertie: " + application);
        } catch (JsonProcessingException e) {
            // Log de l'erreur de conversion
            e.printStackTrace();  // Affiche l'erreur détaillée
            return ResponseEntity.badRequest().body(null);
        }

        // Vérifie si le candidat a déjà postulé
        boolean alreadyApplied = applicationRepository.existsByJobIdAndCandidateId(jobId, candidateId);
        if (alreadyApplied) {
            return ResponseEntity.badRequest().body(null);
        }

        // Récupérer le job en base de données
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job non trouvé avec id: " + jobId));

        // Stocker le fichier CV et récupérer son chemin
        try {
            String cvUrl = cloudinaryService.uploadFile(file);
            application.setCvUrl(cvUrl);
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'upload du CV vers Cloudinary", e);
        }

        // Assigner les valeurs correctement
        application.setJobId(jobId);
        application.setJobTitle(job.getTitle());
        application.setStatus("PENDING");
        application.setExperience(application.getExperience());
        application.setCandidateId(candidateId);

        // Sauvegarde de l'application
        Application savedApplication = applicationService.createApplication(application, jobId);

        // Envoyer un email de confirmation
        try {
            emailService.sendEmail(
                    savedApplication.getCandidateEmail(),
                    "Votre candidature a été soumise",
                    "<p>Merci d'avoir postulé au poste <strong>" + savedApplication.getJobTitle() + "</strong>.</p>" +
                            "<p>Nous examinerons votre candidature et nous vous contacterons bientôt.</p>"
            );

            // Notification admin
            notificationService.sendNotification(
                    "Nouvelle candidature reçue",
                    "Le candidat " + savedApplication.getCandidateName() + " a postulé pour le poste " + savedApplication.getJobTitle()
            );

            System.out.println("✅ Notification envoyée avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
    }



    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getApplicationStats() {
        System.out.println("✅ Requête reçue pour les statistiques");

        Map<String, Long> stats = applicationService.getApplicationStats();

        System.out.println("📊 Stats générées : " + stats);


        if (stats == null) {
            return ResponseEntity.badRequest().body(Collections.emptyMap());
        }
        return ResponseEntity.ok(stats);

    }


    @GetMapping("/job/{jobId}")
    public List<Application> getApplicationsByJobId(@PathVariable String jobId) {
        List<Application> applications = applicationService.getApplicationsByJobId(jobId);
        if (applications.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune candidature trouvée pour ce jobId");
        }
        return applications;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable String id) {
        Application application = applicationService.getApplicationById(id);
        if (application == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(application);
    }







}
