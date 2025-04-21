package tn.esprit.spring.teamsync.Services.Interfaces;

import tn.esprit.spring.teamsync.Entity.Task;
import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    List<Task> getTasksByProject(String projectId);

    Task updateTask(String id, Task task);
    void deleteTask(String id);
    Task getTaskById(String id);
    List<Task> getAllTasks();
}