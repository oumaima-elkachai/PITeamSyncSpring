package tn.esprit.spring.teamsync.Repository;

import tn.esprit.spring.teamsync.Entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}
