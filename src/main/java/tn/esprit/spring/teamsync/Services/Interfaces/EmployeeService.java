package tn.esprit.spring.teamsync.Services.Interfaces;

import tn.esprit.spring.teamsync.Entity.Employee;
import tn.esprit.spring.teamsync.Entity.Project;
import tn.esprit.spring.teamsync.Entity.Task;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    List<Employee> getEmployeesByProject(String projectId);
    Employee updateEmployee(String id, Employee employee);
    boolean deleteEmployee(String id);
    Employee getEmployeeById(String id);
    List<Employee> getAllEmployees();
    List<Task> getEmployeeAssignedTasks(String employeeId);
   // List<Project> getEmployeeProjects(String employeeId);
    boolean assignTaskToEmployee(String employeeId, String taskId);
    boolean addEmployeeToProject(String employeeId, String projectId);
}