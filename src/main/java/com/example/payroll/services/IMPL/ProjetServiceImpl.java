package com.example.payroll.services.IMPL;

import com.example.payroll.model.Projet;
import com.example.payroll.repository.ProjetRepository;
import com.example.payroll.services.interfaces.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjetServiceImpl implements ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Override
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    @Override
    public Optional<Projet> getProjetById(String id) {
        return projetRepository.findById(id);
    }

    @Override
    public Projet createProjet(Projet projet) {
        return projetRepository.save(projet);
    }

    @Override
    public Projet updateProjet(String id, Projet projetDetails) {
        return projetRepository.findById(id).map(projet -> {
            projet.setName(projetDetails.getName());
            projet.setStartDate(projetDetails.getStartDate());
            projet.setEndDate(projetDetails.getEndDate());

            return projetRepository.save(projet);
        }).orElseThrow(() -> new RuntimeException("Projet non trouvé"));
    }

    @Override
    public void deleteProjet(String id) {
        projetRepository.deleteById(id);
    }
}

