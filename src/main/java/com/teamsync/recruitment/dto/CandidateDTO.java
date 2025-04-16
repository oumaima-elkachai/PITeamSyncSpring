package com.teamsync.recruitment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateDTO {
    private String id;
    private String name;
    private String email;
    private int phone;
    private String portfolio;
    private String linkedIn;
    private String github;

}
