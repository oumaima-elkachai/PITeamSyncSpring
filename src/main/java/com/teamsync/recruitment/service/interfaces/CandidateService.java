package com.teamsync.recruitment.service.interfaces;

import com.teamsync.recruitment.entity.Candidate;

import java.util.List;

public interface CandidateService {
    Candidate createCandidate(Candidate candidate);
    List<Candidate> getAllCandidates();
    Candidate getCandidateById(String id);
    void deleteCandidate(String candidateId);
}
