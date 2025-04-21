package tn.esprit.spring.teamsync.Services.Interfaces;

import tn.esprit.spring.teamsync.Entity.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskService {

    Task save(Task task);

    Optional<Task> findById(String id);

    Task addLink(String taskId, Map<String, String> link);
    Task removeLink(String taskId, int linkIndex);

    Task createTask(Task task);

    List<Task> getTasksByProject(String projectId);

    Task updateTask(String id, Task task);
    void deleteTask(String id);
    Task getTaskById(String id);
    List<Task> getAllTasks();
    Task requestExtension(String taskId, LocalDate newDeadline);
}