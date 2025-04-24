package com.teamsync.recruitment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsync.recruitment.entity.JobPosting;
import com.teamsync.recruitment.service.CloudinaryService;
import com.teamsync.recruitment.service.interfaces.JobPostingService;
;
import com.teamsync.recruitment.repository.JobPostingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")  // Allowing CORS from Angular app
@RestController
@AllArgsConstructor
@RequestMapping("/api/jobs")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    private final CloudinaryService cloudinaryService;
    private final JobPostingRepository jobPostingRepository;;

    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllJobs() {
        return ResponseEntity.ok(jobPostingService.getAllJobPostings());
    }

    @PostMapping
    public ResponseEntity<JobPosting> createJob(
            @RequestPart("file") MultipartFile file,  // Image file
            @RequestPart("jobPosting") String jobPostingJson  // JSON string of JobPosting
    ) {
        try {
            // Step 1: Convert the JSON string into a JobPosting object
            ObjectMapper objectMapper = new ObjectMapper();
            JobPosting jobPosting = objectMapper.readValue(jobPostingJson, JobPosting.class);

            // Step 2: Upload the image to Cloudinary and get the URL
            String imageUrl = cloudinaryService.uploadImage(file);

            // Step 3: Set the image URL in the job posting
            jobPosting.setImageUrl(imageUrl);

            // Step 4: Save the job posting in the database
            JobPosting savedJob = jobPostingService.createJobPosting(jobPosting);

            // Step 5: Return the saved job posting
            return ResponseEntity.ok(savedJob);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.noContent().build();
    }

   /* @PutMapping("/{id}")
    public ResponseEntity<JobPosting> updateJob(
            @PathVariable String id,
            @RequestPart("file") MultipartFile file,  // Image file
            @RequestPart("jobPosting") String jobPostingJson  // JSON string of JobPosting
    ) {
        try {
            // Step 1: Convert the JSON string into a JobPosting object
            ObjectMapper objectMapper = new ObjectMapper();
            JobPosting jobPosting = objectMapper.readValue(jobPostingJson, JobPosting.class);

            // Step 2: Upload the image to Cloudinary if a file is provided and get the URL
            if (file != null && !file.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(file);
                jobPosting.setImageUrl(imageUrl);
            }

            // Step 3: Update the job posting in the database
            jobPosting.setId(id);  // Ensure the job posting has the correct ID for update
            jobPosting.setDatePosted(new Date());  // Optionally set a new date posted

            JobPosting updatedJob = jobPostingService.updateJobPosting(id, jobPosting);

            // Step 4: Return the updated job posting
            return ResponseEntity.ok(updatedJob);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Handle parsing JSON
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);  // Handle general errors
        }
    }*/



    @PutMapping("/{id}")
    public ResponseEntity<JobPosting> updateJob(
            @PathVariable String id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("jobPosting") String jobPostingJson
    ) {
        try {
            System.out.println("jobPostingJson reçu : " + jobPostingJson);

            ObjectMapper objectMapper = new ObjectMapper();
            JobPosting jobPosting = objectMapper.readValue(jobPostingJson, JobPosting.class);

            System.out.println("JobPosting après conversion : " + jobPosting);

            // Récupère l'ancien job depuis la base pour garder l'image si nécessaire
            JobPosting existingJob = jobPostingService.getJobPostingById(id);
            if (existingJob == null) {
                return ResponseEntity.notFound().build(); // Job non trouvé
            }

            // Si un nouveau fichier est envoyé → upload + set imageUrl
            if (file != null && !file.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(file);
                jobPosting.setImageUrl(imageUrl);
            } else {
                // Sinon → on garde l'image actuelle
                jobPosting.setImageUrl(existingJob.getImageUrl());
            }

            jobPosting.setId(id); // Assure que l'ID est bien défini
            JobPosting updatedJob = jobPostingService.updateJobPosting(id, jobPosting);

            return ResponseEntity.ok(updatedJob);
        } catch (IOException e) {
            System.err.println("Erreur de parsing JSON : " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            System.err.println("Erreur générale : " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }




    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobById(@PathVariable String id) {
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(id);
        return jobPosting.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/most-applied")
    public ResponseEntity<JobPosting> getJobWithMostApplications() {
        JobPosting job = jobPostingService.findJobWithMostApplications();
        return ResponseEntity.ok(job);
    }







    // This is an alternative upload endpoint for image only
    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Upload the image to Cloudinary
            String imageUrl = cloudinaryService.uploadImage(file);

            // Return the URL of the uploaded image
            Map<String, String> response = Map.of("imageUrl", imageUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to upload image"));
        }
    }
}
