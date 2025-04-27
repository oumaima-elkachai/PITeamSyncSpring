package tn.esprit.spring.teamsync.Services.MPL;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tn.esprit.spring.teamsync.Entity.Employee;
import tn.esprit.spring.teamsync.Entity.Task;
import tn.esprit.spring.teamsync.Entity.Project;

import tn.esprit.spring.teamsync.Event.TaskAssignedEvent;
import tn.esprit.spring.teamsync.Repository.EmployeeRepository;
import tn.esprit.spring.teamsync.Repository.TaskRepository;
import tn.esprit.spring.teamsync.Repository.ProjectRepository;

import tn.esprit.spring.teamsync.Services.Interfaces.TaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisher;
import tn.esprit.spring.teamsync.Event.TaskAssignedEvent;
import tn.esprit.spring.teamsync.Event.ExtensionRequestedEvent;
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final @Lazy EmailService emailService; // Add this

    private final ProjectRepository projectRepository; // Added
    private final EmployeeRepository employeeRepository; // Added
    private final ApplicationEventPublisher eventPublisher; // Add this


    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task addLink(String taskId, Map<String, String> link) {
        link.put("createdAt", LocalDateTime.now().toString());
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.getLinks().add(link);
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public Task removeLink(String taskId, int linkIndex) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    if (linkIndex >= 0 && linkIndex < task.getLinks().size()) {
                        task.getLinks().remove(linkIndex);
                        return taskRepository.save(task);
                    }
                    throw new IllegalArgumentException("Invalid link index");
                })
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public Optional<Task> findById(String id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task createTask(Task task) {
        Project project = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (task.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(task.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
            if (!project.getTeamMemberIds().contains(employee.getId())) {
                project.getTeamMemberIds().add(employee.getId());
            }
            if (!employee.getProjectIds().contains(project.getId())) {
                employee.getProjectIds().add(project.getId());
                employeeRepository.save(employee);
            }
        }
        Task savedTask = taskRepository.save(task);
        project.getTaskIds().add(savedTask.getId());
        projectRepository.save(project);
        if (task.getEmployeeId() != null) {
            eventPublisher.publishEvent(new TaskAssignedEvent(this, savedTask.getId(), task.getEmployeeId()));
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

    @Override
    public Task requestExtension(String taskId, LocalDate newDeadline) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setRequestedExtensionDate(newDeadline);
        task.setExtensionStatus("PENDING");
        Task updatedTask = taskRepository.save(task);

        eventPublisher.publishEvent(new ExtensionRequestedEvent(this, taskId));
        return updatedTask;
    }

    @Override
    public Task approveExtension(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if ("PENDING".equals(task.getExtensionStatus())) {
            task.setDeadline(task.getRequestedExtensionDate());
            task.setExtensionStatus("APPROVED");
            return taskRepository.save(task);
        }
        throw new IllegalStateException("No pending extension request");
    }

    @Override
    public List<Task> getTasksBySkill(String skill) {
        return taskRepository.findByRequiredSkillsContaining(skill);
    }

}