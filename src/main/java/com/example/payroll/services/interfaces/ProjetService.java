package com.example.payroll.services.interfaces;

import com.example.payroll.model.Projet;

import java.util.List;
import java.util.Optional;

public interface ProjetService {
    List<Projet> getAllProjets();
    Optional<Projet> getProjetById(String id);
    Projet createProjet(Projet projet);
    Projet updateProjet(String id, Projet projetDetails);
    void deleteProjet(String id);
}
