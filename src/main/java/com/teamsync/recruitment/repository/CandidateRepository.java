package com.teamsync.recruitment.repository;

import com.teamsync.recruitment.entity.Application;
import com.teamsync.recruitment.entity.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Optional<Candidate> findById(String id);  // ✅ Utiliser `id` et non `candidateId`
    Optional<Candidate> findByEmail(String email);
}
