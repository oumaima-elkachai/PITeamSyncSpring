package com.teamsync.recruitment.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationCandidateDTO {

    private String id;
    private String jobTitle;
    private String status;
    private String cvUrl;

    private String candidateId;
    private String candidateName;
    private String candidateEmail;
    private int candidatePhone;
    private String candidatePortfolio;
    private String candidateLinkedIn;
    private String candidateGithub;
    private int experience;
}
