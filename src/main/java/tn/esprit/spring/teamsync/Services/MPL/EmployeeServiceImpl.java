package tn.esprit.spring.teamsync.Services.MPL;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.teamsync.Entity.Employee;
import tn.esprit.spring.teamsync.Entity.Project;
import tn.esprit.spring.teamsync.Entity.Task;
import tn.esprit.spring.teamsync.Repository.EmployeeRepository;
import tn.esprit.spring.teamsync.Repository.ProjectRepository;
import tn.esprit.spring.teamsync.Repository.TaskRepository;
import tn.esprit.spring.teamsync.Services.Interfaces.EmployeeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public List<Employee> getEmployeesByProject(String projectId) {
        return employeeRepository.findByProjectIdsContaining(projectId);
    }
    @Override
    public Employee createEmployee(Employee employee) {
        if (employee.getAssignedTaskIds() == null) {
            employee.setAssignedTaskIds(new ArrayList<>());
        }
        if (employee.getProjectIds() == null) {
            employee.setProjectIds(new ArrayList<>());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(String id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) return null;

        updatedEmployee.setId(id);
        // Preserve existing relationships if not explicitly updated
        if (updatedEmployee.getAssignedTaskIds() == null) {
            updatedEmployee.setAssignedTaskIds(employee.getAssignedTaskIds());
        }
        if (updatedEmployee.getProjectIds() == null) {
            updatedEmployee.setProjectIds(employee.getProjectIds());
        }

        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public boolean deleteEmployee(String id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            return false;
        }

        // Remove employee from all projects
        for (String projectId : employee.get().getProjectIds()) {
            Project project = projectRepository.findById(projectId).orElse(null);
            if (project != null) {
                project.getTeamMemberIds().remove(id);
                projectRepository.save(project);
            }
        }

        // Unassign all tasks
        for (String taskId : employee.get().getAssignedTaskIds()) {
            Task task = taskRepository.findById(taskId).orElse(null);
            if (task != null) {
                task.setEmployeeId(null);
                taskRepository.save(task);
            }
        }

        employeeRepository.deleteById(id);
        return true;
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Task> getEmployeeAssignedTasks(String employeeId) {
        return taskRepository.findByEmployeeId(employeeId);
    }

    //@Override
    //public List<Project> getEmployeeProjects(String employeeId) {
      //  return projectRepository.findByTeamMemberIdsContaining(employeeId);
   // }

    @Override
    public boolean assignTaskToEmployee(String employeeId, String taskId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        Task task = taskRepository.findById(taskId).orElse(null);

        if (employee == null || task == null) {
            return false;
        }

        // Update task
        task.setEmployeeId(employeeId);
        taskRepository.save(task);

        // Update employee
        if (!employee.getAssignedTaskIds().contains(taskId)) {
            employee.getAssignedTaskIds().add(taskId);
            employeeRepository.save(employee);
        }

        return true;
    }

    @Override
    public boolean addEmployeeToProject(String employeeId, String projectId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);

        if (employee == null || project == null) {
            return false;
        }

        // Update project
        if (!project.getTeamMemberIds().contains(employeeId)) {
            project.getTeamMemberIds().add(employeeId);
            projectRepository.save(project);
        }

        // Update employee
        if (!employee.getProjectIds().contains(projectId)) {
            employee.getProjectIds().add(projectId);
            employeeRepository.save(employee);
        }

        return true;
    }
}