package com.teamsync.recruitment.service.impl;

import com.teamsync.recruitment.dto.ApplicationDetailsDTO;
import com.teamsync.recruitment.entity.Application;
import com.teamsync.recruitment.entity.Candidate;
import com.teamsync.recruitment.entity.JobPosting;
import com.teamsync.recruitment.exception.ApplicationNotFoundException;
import com.teamsync.recruitment.exception.JobNotFoundException;
import com.teamsync.recruitment.repository.ApplicationRepository;
import com.teamsync.recruitment.repository.CandidateRepository;
import com.teamsync.recruitment.repository.JobPostingRepository;
import com.teamsync.recruitment.service.EmailService;
import com.teamsync.recruitment.service.interfaces.ApplicationService;
import jakarta.mail.MessagingException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import com.teamsync.recruitment.service.PythonClient;
@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private CandidateRepository candidateRepository;
    @Autowired private JobPostingRepository jobPostingRepository;
    @Autowired private EmailService emailService;
    @Autowired private RestTemplate restTemplate;
    @Autowired private PythonClient pythonClient;

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Application getApplicationById(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Candidature introuvable avec l'id : " + id));
    }

    @Override
    public Application createApplication(Application application, String jobId) {
        application.setStatus("PENDING");

        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Offre d'emploi non trouvée avec l'id : " + jobId));

        Candidate candidate = candidateRepository.findById(application.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("Candidat introuvable avec l'id : " + application.getCandidateId()));

        List<Application> existing = applicationRepository.findByCandidateIdAndJobId(candidate.getId(), jobId);
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("Ce candidat a déjà postulé à cette offre.");
        }

        // Enrichir l'objet Application
        application.setCandidate(candidate);
        application.setCandidateId(candidate.getId());
        application.setCandidateName(candidate.getName());
        application.setCandidateEmail(candidate.getEmail());
        application.setCandidatePhone(candidate.getPhone());
        application.setCandidateLinkedIn(candidate.getLinkedIn());
        application.setCandidateGithub(candidate.getGithub());
        application.setCandidatePortfolio(candidate.getPortfolio());
        application.setJobId(jobId);

        // Sauvegarder et associer à l'offre
        Application saved = applicationRepository.save(application);
        job.getApplicationIds().add(saved.getId());
        jobPostingRepository.save(job);

        return saved;
    }

    @Override
    public void updateApplicationStatus(String id, String status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Candidature introuvable avec l'id : " + id));

        application.setStatus(status);
        application.addStatusChange(status);
        applicationRepository.save(application);

        try {
            String email = application.getCandidateEmail();
            String name = application.getCandidateName();
            String jobTitle = application.getJobTitle();

            if ("ACCEPTED".equals(status)) {
                emailService.sendEmail(email, "🎉 Votre candidature a été acceptée !", getAcceptedEmailTemplate(name, jobTitle));
            } else if ("REJECTED".equals(status)) {
                emailService.sendEmail(email, "❌ Votre candidature a été refusée", getRejectedEmailTemplate(name, jobTitle));
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getAcceptedEmailTemplate(String candidateName, String jobTitle) {
        return "<html><body style='font-family: Arial; padding: 20px; background: #f4f4f4'>" +
                "<h2 style='color: #2E86C1;'>🎉 Félicitations " + candidateName + " !</h2>" +
                "<p>Votre candidature pour le poste <strong>" + jobTitle + "</strong> a été acceptée.</p>" +
                "<p>Nous reviendrons vers vous pour la suite du processus.</p>" +
                "<a href='https://www.votre-entreprise.com/next-steps' style='padding: 10px 20px; background: #2E86C1; color: white; text-decoration: none;'>Voir les prochaines étapes</a>" +
                "</body></html>";
    }

    private String getRejectedEmailTemplate(String candidateName, String jobTitle) {
        return "<html><body style='font-family: Arial; padding: 20px; background: #f4f4f4'>" +
                "<h2 style='color: #C0392B;'>❌ Candidature refusée</h2>" +
                "<p>Bonjour " + candidateName + ",</p>" +
                "<p>Nous sommes désolés de vous informer que votre candidature pour <strong>" + jobTitle + "</strong> n'a pas été retenue.</p>" +
                "<a href='http://localhost:4200/user/job-postings' style='padding: 10px 20px; background: #C0392B; color: white; text-decoration: none;'>Voir d'autres offres</a>" +
                "</body></html>";
    }

    public String extractTextFromCvUrl(String cvUrl) {
        try (InputStream inputStream = new URL(cvUrl).openStream()) {
            return new Tika().parseToString(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

   /* public double scoreCvWithPython(String cvText, String jobDescription) {
        String url = "http://localhost:5000/score";
        Map<String, String> body = new HashMap<>();
        body.put("cv_text", cvText);
        body.put("job_description", jobDescription);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return Double.parseDouble(response.getBody().get("score").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
*/
    /*public List<Application> getRankedApplications(String jobId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Offre d'emploi non trouvée"));

        List<Application> applications = applicationRepository.findByJobId(jobId);

        for (Application app : applications) {
            String cvText = extractTextFromCvUrl(app.getCvUrl());
            double score = scoreCvWithPython(cvText, job.getDescription());
            app.setScore(score);
        }

        applications.sort(Comparator.comparingDouble(Application::getScore).reversed());
        return applications;
    }*/

    @Override
    public List<Application> getRankedApplications(String jobId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        String jobDescription = job.getDescription();
        List<Application> applications = applicationRepository.findByJobId(jobId);

        for (Application app : applications) {
            try {
                double rawScore = pythonClient.getMatchingScore(jobDescription, app.getCvUrl());

                // 🔢 Conversion du score entre 1 et 10 (arrondi)
                double scoreOnTen = Math.max(1, (int) Math.round(rawScore * 10));
                app.setScore(scoreOnTen); // Si tu veux un int, sinon change type de score dans Application

            } catch (Exception e) {
                e.printStackTrace();
                app.setScore(1.0); // Score minimum en cas d'erreur
            }
        }

        // Trier par score décroissant
        applications.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        return applications;
    }





    /* public ApplicationDetailsDTO getApplicationWithDetails(String applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Candidature introuvable"));

        Candidate candidate = app.getCandidate();
        JobPosting job = app.getJob();

        ApplicationDetailsDTO dto = new ApplicationDetailsDTO();
        dto.setId(app.getId());
        dto.setJobTitle(app.getJobTitle());
        dto.setStatus(app.getStatus());
        dto.setCvUrl(app.getCvUrl());
        dto.setSubmittedAt(app.getSubmittedAt());

        if (candidate != null) {
            dto.setCandidateId(candidate.getId());
            dto.setCandidateName(candidate.getName());
            dto.setCandidateEmail(candidate.getEmail());
            dto.setCandidatePhone(candidate.getPhone());
            dto.setCandidatePortfolio(candidate.getPortfolio());
            dto.setCandidateLinkedIn(candidate.getLinkedIn());
            dto.setCandidateGithub(candidate.getGithub());
        }

        if (job != null) {
            dto.setJobId(job.getId());
            dto.setJobTitleFull(job.getTitle());
            dto.setJobDepartment(job.getDepartment());
            dto.setJobCategory(job.getCategory());
            dto.setJobSalary(job.getSalary());
            dto.setJobImageUrl(job.getImageUrl());
        }

        return dto;
    }
*/
    @Override
    public void deleteApplication(String id) {
        applicationRepository.deleteById(id);
    }

    @Override
    public List<Application> getApplicationsByJobId(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    @Override
    public Map<String, Long> getApplicationStats() {
        List<Application> applications = applicationRepository.findAll();

        if (applications.isEmpty()) {
            return Collections.emptyMap();
        }

        return applications.stream()
                .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()));
    }

    @Override
    public Map<String, Long> getApplicationsCountPerJob() {
        List<Application> applications = applicationRepository.findAll();


        return applications.stream()
                .collect(Collectors.groupingBy(
                        app -> app.getJobTitle() != null ? app.getJobTitle() : "Inconnu",
                        Collectors.counting()
                ));
    }



}


