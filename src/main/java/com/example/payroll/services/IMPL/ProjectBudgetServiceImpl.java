package com.example.payroll.services.IMPL;

import com.example.payroll.model.ProjectBudget;
import com.example.payroll.model.Projet;
import com.example.payroll.repository.ProjectBudgetRepository;
import com.example.payroll.repository.ProjetRepository;
import com.example.payroll.services.interfaces.ProjectBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectBudgetServiceImpl implements ProjectBudgetService {
    @Autowired
    private ProjetRepository projetRepository;  // Injection ici
    private final ProjectBudgetRepository projectBudgetRepository;

    public ProjectBudgetServiceImpl(ProjectBudgetRepository projectBudgetRepository) {
        this.projectBudgetRepository = projectBudgetRepository;
    }

    @Override
    public ProjectBudget createProjectBudget(ProjectBudget projectBudget) {
        // Vérifier que l'objet Projet est bien fourni
        if (projectBudget.getProjet() == null || projectBudget.getProjet().getId() == null) {
            throw new RuntimeException("Projet ID is required");
        }

        // Récupérer le projet associé par son ID
        Projet project = projetRepository.findById(projectBudget.getProjet().getId())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectBudget.getProjet().getId()));

        // Initialiser la liste budgetIds si elle est null
        if (project.getBudgetIds() == null) {
            project.setBudgetIds(new ArrayList<>());
        }

        // Sauvegarder le budget du projet
        projectBudget.setProjet(project);
        ProjectBudget savedBudget = projectBudgetRepository.save(projectBudget);

        // Ajouter l'ID du budget à la liste budgetIds du projet
        project.getBudgetIds().add(savedBudget.getId());

        // Sauvegarder le projet avec la liste budgetIds mise à jour
        projetRepository.save(project);

        return savedBudget;
    }


    @Override
    public ProjectBudget getProjectBudgetById(String id) {  // Changement de Long à String pour l'ID
        return projectBudgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project Budget not found with id: " + id));
    }

    @Override
    public List<ProjectBudget> getAllProjectBudgets() {
        return projectBudgetRepository.findAll();
    }

    @Override
    public ProjectBudget updateProjectBudget(String id, ProjectBudget projectBudget) {  // Changement de Long à String pour l'ID
        Optional<ProjectBudget> existingBudget = projectBudgetRepository.findById(id);
        if (existingBudget.isPresent()) {
            ProjectBudget updatedBudget = existingBudget.get();
            updatedBudget.setAllocatedFunds(projectBudget.getAllocatedFunds());
            updatedBudget.setUsedFunds(projectBudget.getUsedFunds());
            return projectBudgetRepository.save(updatedBudget);
        } else {
            throw new RuntimeException("Project Budget not found with id: " + id);
        }
    }

    @Override
    public void deleteProjectBudget(String id) {  // Changement de Long à String pour l'ID
        projectBudgetRepository.deleteById(id);
    }
}
