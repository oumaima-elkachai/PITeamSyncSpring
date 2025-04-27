package tn.esprit.spring.teamsync.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.spring.teamsync.Entity.Employee;
import java.util.List;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
    List<Employee> findByProjectIdsContaining(String projectId);
    List<Employee> findByAssignedTaskIdsContaining(String taskId);
    List<Employee> findByRole(String role);

}