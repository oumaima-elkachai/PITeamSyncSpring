package com.teamsync.recruitment.repository;

import com.teamsync.recruitment.entity.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByJobId(String jobId);
    List<Application> findByCandidateId(String candidateId);
    List<Application> findByStatus(String status);
    List<Application> findByCandidateIdAndJobId(String candidateId, String jobId);
    boolean existsByJobIdAndCandidateId(String jobId, String candidateId);
    int countByJobId(String jobId);
}