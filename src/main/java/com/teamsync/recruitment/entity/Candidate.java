package com.teamsync.recruitment.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@Document(collection = "candidates")
public class Candidate {
    @Id
    private String id;
    private String name;
    private String email;
    private int phone;
    private String portfolio;
    private String linkedIn;
    private String github;




    private List<String> applicationIds; // Stocke les IDs des candidatures du candidat



}

