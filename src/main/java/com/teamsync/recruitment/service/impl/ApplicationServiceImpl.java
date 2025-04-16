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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private EmailService emailService;
    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Application getApplicationById(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with id: " + id));
    }

    @Override
    public Application createApplication(Application application, String jobId) {
        application.setStatus("PENDING");

        // Vérifier si le job existe
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + jobId));

        // Vérifier si le candidat existe déjà via son ID
        Candidate candidate = candidateRepository.findById(application.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + application.getCandidateId()));

        // 📌 Vérifier si ce candidat a déjà postulé à ce job
        List<Application> existingApplications = applicationRepository.findByCandidateIdAndJobId(candidate.getId(), jobId);
        if (!existingApplications.isEmpty()) {
            throw new IllegalArgumentException("This candidate has already applied for this job.");
        }

        // Associer les informations du candidat à la candidature
        application.setCandidate(candidate);
        application.setCandidateId(candidate.getId());
        application.setCandidateName(candidate.getName());
        application.setCandidateEmail(candidate.getEmail());
        application.setCandidatePhone(candidate.getPhone());
        application.setCandidateLinkedIn(candidate.getLinkedIn());
        application.setCandidateGithub(candidate.getGithub());
        application.setCandidatePortfolio(candidate.getPortfolio());
        application.setJobId(jobId);

        // Sauvegarder la candidature
        Application savedApplication = applicationRepository.save(application);

        // Ajouter l'ID de la candidature dans `applicationIds` du JobPosting
        if (job.getApplicationIds() == null) {
            job.setApplicationIds(new ArrayList<>());
        }
        job.getApplicationIds().add(savedApplication.getId());
        jobPostingRepository.save(job);

        return savedApplication;
    }




    @Override
    public Map<String, Long> getApplicationStats() {
        List<Application> applications = applicationRepository.findAll();

        if (applications.isEmpty()) {
            System.out.println("⚠️ Aucune candidature trouvée !");
            return Collections.emptyMap();
        }

        return applications.stream()
                .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()));
    }






    @Override
    public void updateApplicationStatus(String id, String status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application non trouvée avec id: " + id));

        application.setStatus(status);
        application.addStatusChange(status);
        applicationRepository.save(application);

        try {
            String candidateName = application.getCandidateName();
            String candidateEmail = application.getCandidateEmail();
            String jobTitle = application.getJobTitle();


            if ("ACCEPTED".equals(status)) {
                emailService.sendEmail(
                        candidateEmail,
                        "🎉 Votre candidature a été acceptée !",
                        getAcceptedEmailTemplate(candidateName, jobTitle)
                );
            } else if ("REJECTED".equals(status)) {
                emailService.sendEmail(
                        candidateEmail,
                        "❌ Votre candidature a été refusée",
                        getRejectedEmailTemplate(candidateName, jobTitle)
                );
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Génère le contenu HTML pour l'email d'acceptation.
     */
    private String getAcceptedEmailTemplate(String candidateName, String jobTitle) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "<table align='center' width='600' style='background-color: #ffffff; padding: 20px; border-radius: 8px;'>" +
                "<tr><td align='center'>" +
                "<h2 style='color: #2E86C1;'>🎉 Félicitations, " + candidateName + " !</h2>" +
                "<p style='font-size: 16px; color: #333;'>Votre candidature pour le poste <strong>" + jobTitle + "</strong> a été acceptée.</p>" +
                "<p style='font-size: 16px; color: #333;'>Nous vous contacterons bientôt pour la suite du processus.</p>" +
                "<a href='https://www.votre-entreprise.com/next-steps' " +
                "style='display: inline-block; padding: 10px 20px; background-color: #2E86C1; color: white; text-decoration: none; border-radius: 5px;'>Voir les prochaines étapes</a>" +
                "<p style='margin-top: 20px; font-size: 14px; color: #777;'>Merci de votre confiance,</p>" +
                "<p style='font-size: 14px; color: #777;'>L'équipe Recrutement</p>" +
                "</td></tr></table></body></html>";
    }

    /**
     * Génère le contenu HTML pour l'email de refus.
     */
    private String getRejectedEmailTemplate(String candidateName, String jobTitle) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "<table align='center' width='600' style='background-color: #ffffff; padding: 20px; border-radius: 8px;'>" +
                "<tr><td align='center'>" +
                "<h2 style='color: #C0392B;'>❌ Candidature refusée</h2>" +
                "<p style='font-size: 16px; color: #333;'>Bonjour " + candidateName + ",</p>" +
                "<p style='font-size: 16px; color: #333;'>Nous sommes désolés de vous informer que votre candidature pour le poste <strong>" + jobTitle + "</strong> n'a pas été retenue.</p>" +
                "<p style='font-size: 16px; color: #333;'>Nous vous remercions pour votre intérêt et espérons vous revoir sur d'autres opportunités.</p>" +
                "<a href='http://localhost:4200/user/job-postings' " +
                "style='display: inline-block; padding: 10px 20px; background-color: #C0392B; color: white; text-decoration: none; border-radius: 5px;'>Voir d'autres offres</a>" +
                "<p style='margin-top: 20px; font-size: 14px; color: #777;'>Merci de votre compréhension,</p>" +
                "<p style='font-size: 14px; color: #777;'>L'équipe Recrutement</p>" +
                "</td></tr></table></body></html>";
    }







/// /////
public ApplicationDetailsDTO getApplicationWithDetails(String applicationId) {
    // Récupère l'application
    Application app = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Candidature introuvable"));

    // Récupère le candidat depuis l'objet Application (via @DBRef)
    Candidate candidate = app.getCandidate();
    JobPosting job = app.getJob();

    // Création du DTO combiné
    ApplicationDetailsDTO dto = new ApplicationDetailsDTO();

    // Infos de candidature
    dto.setId(app.getId());
    dto.setJobTitle(app.getJobTitle());
    dto.setStatus(app.getStatus());
    dto.setCvUrl(app.getCvUrl());
    dto.setSubmittedAt(app.getSubmittedAt());



    // Infos candidat
    if (candidate != null) {
        dto.setCandidateId(candidate.getId());
        dto.setCandidateName(candidate.getName());
        dto.setCandidateEmail(candidate.getEmail());
        dto.setCandidatePhone(candidate.getPhone());
        dto.setCandidatePortfolio(candidate.getPortfolio());
        dto.setCandidateLinkedIn(candidate.getLinkedIn());
        dto.setCandidateGithub(candidate.getGithub());

    }

    // Infos job
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








    @Override
    public void deleteApplication(String id) {
        applicationRepository.deleteById(id);
    }


    @Override
    public List<Application> getApplicationsByJobId(String jobId) {
        return applicationRepository.findByJobId(jobId);
    }



}
