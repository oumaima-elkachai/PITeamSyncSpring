package com.teamsync.recruitment.service.impl;

import com.teamsync.recruitment.entity.Application;
import com.teamsync.recruitment.entity.Candidate;
import com.teamsync.recruitment.repository.ApplicationRepository;
import com.teamsync.recruitment.repository.CandidateRepository;
import com.teamsync.recruitment.service.interfaces.CandidateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository; // Marquez-le comme final

    // Injection de dépendances via le constructeur
    public CandidateServiceImpl(CandidateRepository candidateRepository, ApplicationRepository applicationRepository) {
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
    }

    @Override
    public void deleteCandidate(String candidateId) {
        // Vérifier si le candidat existe
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + candidateId));

        // Supprimer toutes les candidatures de ce candidat
        List<Application> applications = applicationRepository.findByCandidateId(candidateId);
        applicationRepository.deleteAll(applications);

        // Supprimer le candidat
        candidateRepository.delete(candidate);
    }
}
