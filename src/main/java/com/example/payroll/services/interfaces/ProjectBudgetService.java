package com.example.payroll.services.interfaces;

import com.example.payroll.model.ProjectBudget;
import java.util.List;

public interface ProjectBudgetService {
    ProjectBudget createProjectBudget(ProjectBudget projectBudget);
    ProjectBudget getProjectBudgetById(String id);  // Changement de Long à String pour l'ID
    List<ProjectBudget> getAllProjectBudgets();
    ProjectBudget updateProjectBudget(String id, ProjectBudget projectBudget);  // Changement de Long à String pour l'ID
    void deleteProjectBudget(String id);  // Changement de Long à String pour l'ID
}
