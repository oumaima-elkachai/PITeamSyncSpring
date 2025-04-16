package com.teamsync.recruitment.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
@Getter
@Setter
@Document(collection = "quizzes")
public class Quiz {
    @Id
    private String id;
    private String jobId;
    private List<Question> questions;
    private int passingScore;


}

class Question {
    private String text;
    private List<String> options;
    private String correctAnswer;


}