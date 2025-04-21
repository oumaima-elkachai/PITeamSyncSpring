package tn.esprit.spring.teamsync.Services.MPL;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.teamsync.Entity.Employee;
import tn.esprit.spring.teamsync.Entity.Task;
import tn.esprit.spring.teamsync.Entity.Project;

import tn.esprit.spring.teamsync.Repository.EmployeeRepository;
import tn.esprit.spring.teamsync.Repository.TaskRepository;
import tn.esprit.spring.teamsync.Repository.ProjectRepository;

import tn.esprit.spring.teamsync.Services.Interfaces.TaskService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository; // Added
    private final EmployeeRepository employeeRepository; // Added


    @Override
    public Task createTask(Task task) {
        // Validate project exists
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (task.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(task.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

            // Add employee to project team if not already present
            if (!project.getTeamMemberIds().contains(employee.getId())) {
                project.getTeamMemberIds().add(employee.getId());
            }

            // Add project to employee's project list if not present
            if (!employee.getProjectIds().contains(project.getId())) {
                employee.getProjectIds().add(project.getId());
                employeeRepository.save(employee);
            }


        }
        // Create task
        Task savedTask = taskRepository.save(task);
        // Update project's task list
        project.getTaskIds().add(savedTask.getId());
        projectRepository.save(project);
        if (task.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(task.getEmployeeId()).get();
            employee.getAssignedTaskIds().add(savedTask.getId());
            employeeRepository.save(employee);
        }
        return savedTask;
    }

    @Override
    public List<Task> getTasksByProject(String projectId) {
        return taskRepository.findByProjectId(projectId); // Use correct method name
    }


    @Override
    public Task updateTask(String id, Task task) {
        task.setId(id); // Ensure the ID matches
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task getTaskById(String id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }




}