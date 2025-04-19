package com.example.payroll.repository;

import com.example.payroll.model.Projet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjetRepository extends MongoRepository<Projet, String> {
}
