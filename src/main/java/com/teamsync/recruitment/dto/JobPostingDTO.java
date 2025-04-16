package com.teamsync.recruitment.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class JobPostingDTO {
    private String id;
    private String title;
    private String department;
    private String category;
    private String status;
    private double salary;
    private Date datePosted;
    private Date expirationDate;
    private String imageUrl;
}

