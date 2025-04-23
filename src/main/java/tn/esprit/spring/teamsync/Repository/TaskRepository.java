package tn.esprit.spring.teamsync.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.teamsync.Entity.Task;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByProjectId(String projectId);
    List<Task> findByEmployeeId(String employeeId);
    List<Task> findByEmployeeIdAndDeadlineBetweenAndStatusNot(
            String employeeId,
            LocalDate start,
            LocalDate end,
            Task.Status status
    );

}