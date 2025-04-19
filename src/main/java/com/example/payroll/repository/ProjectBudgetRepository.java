package com.example.payroll.repository;

import com.example.payroll.model.ProjectBudget;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectBudgetRepository extends MongoRepository<ProjectBudget, String> {  // Utilisation de MongoRepository
}
