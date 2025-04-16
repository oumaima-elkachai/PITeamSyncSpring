package com.teamsync.recruitment.repository;

import com.teamsync.recruitment.entity.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuizRepository extends MongoRepository<Quiz, String> {
    List<Quiz> findByJobId(String jobId);
}