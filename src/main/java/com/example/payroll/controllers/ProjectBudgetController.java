package com.example.payroll.controllers;

import com.example.payroll.model.ProjectBudget;
import com.example.payroll.services.interfaces.ProjectBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/project-budgets")
public class ProjectBudgetController {

    private final ProjectBudgetService projectBudgetService;

    @Autowired
    public ProjectBudgetController(ProjectBudgetService projectBudgetService) {
        this.projectBudgetService = projectBudgetService;
    }

    @PostMapping
    public ResponseEntity<?> createProjectBudget(@RequestBody ProjectBudget projectBudget) {
        try {
            ProjectBudget createdBudget = projectBudgetService.createProjectBudget(projectBudget);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBudget);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProjectBudget> getProjectBudgetById(@PathVariable String id) {  // Changement de Long à String pour l'ID
        ProjectBudget budget = projectBudgetService.getProjectBudgetById(id);
        return ResponseEntity.ok(budget);
    }

    @GetMapping
    public ResponseEntity<List<ProjectBudget>> getAllProjectBudgets() {
        List<ProjectBudget> budgets = projectBudgetService.getAllProjectBudgets();
        return ResponseEntity.ok(budgets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectBudget> updateProjectBudget(@PathVariable String id, @RequestBody ProjectBudget projectBudget) {  // Changement de Long à String pour l'ID
        ProjectBudget updatedBudget = projectBudgetService.updateProjectBudget(id, projectBudget);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectBudget(@PathVariable String id) {  // Changement de Long à String pour l'ID
        projectBudgetService.deleteProjectBudget(id);
        return ResponseEntity.noContent().build();
    }
}
