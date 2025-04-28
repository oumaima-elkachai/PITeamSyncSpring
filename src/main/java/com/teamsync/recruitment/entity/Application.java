package com.teamsync.recruitment.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "applications")
public class Application {
    @Id
    private String id;
    private String jobTitle;
    private String jobId;         // Lien vers l'offre d'emploi
    private String candidateId;   // ID du candidat (lié au microservice User)
    private String candidateName;
    private String candidateEmail;
    private String cvUrl;         // URL du CV (stockage cloud ou fichier base64)
    private String status;
    private int quizScore;
    @Transient
    private Double score; // Score de matching CV


    /// //////////

    private String candidatePhone;
    private String candidatePortfolio;
    private String candidateLinkedIn;
    private String candidateGithub;
    private int experience;

    @CreatedDate
    private LocalDateTime submittedAt;
    private List<StatusChange> statusHistory = new ArrayList<>();

    public void addStatusChange(String status) {
        this.statusHistory.add(new StatusChange(status, LocalDateTime.now()));
    }

    @DBRef
    private Candidate candidate;
    @DBRef
    private JobPosting job;
}
