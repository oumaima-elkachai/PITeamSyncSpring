package com.teamsync.recruitment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public class ApplicationDetailsDTO {
    // Infos de candidature
    private String id;
    private String jobTitle;
    private String status;
    private String cvUrl;
    private LocalDateTime submittedAt;
    private int experience;
    // Candidat
    private String candidateId;
    private String candidateName;
    private String candidateEmail;
    private int candidatePhone;
    private String candidatePortfolio;
    private String candidateLinkedIn;
    private String candidateGithub;


    // Job
    private String jobId;
    private String jobTitleFull;
    private String jobDepartment;
    private String jobCategory;
    private double jobSalary;
    private String jobImageUrl;
}
