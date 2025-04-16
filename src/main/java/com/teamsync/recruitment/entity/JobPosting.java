package com.teamsync.recruitment.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "job_postings")
public class JobPosting {
    @Id
    private String id;
    private String title;         // Intitulé du poste
    private String description;   // Description du poste
    private String department;    // Département (ex: IT, RH, Marketing)
    private String category;      // Catégorie du poste (ex: CDI, Stage, CDD)
    private List<String> applicationIds; // IDs des candidatures pour ce job
    private String status;
    private double salary;  // Salaire du poste
    private Date datePosted;  // Date de publication
    private Date expirationDate;  // Date d'expiration de l'offre
    private String imageUrl;  // URL de l'image du job
    private String jobId;

}
