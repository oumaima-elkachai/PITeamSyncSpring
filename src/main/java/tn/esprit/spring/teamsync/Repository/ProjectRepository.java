package tn.esprit.spring.teamsync.Repository;

import tn.esprit.spring.teamsync.Entity.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
