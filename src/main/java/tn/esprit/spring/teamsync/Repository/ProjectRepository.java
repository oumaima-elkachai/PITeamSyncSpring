package tn.esprit.spring.teamsync.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.teamsync.Entity.Project;
import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    // Basic CRUD provided by MongoRepository
    List<Project> findByStatus(String status);
    List<Project> findByType(String type);
    List<Project> findByDepartment(String department);
}