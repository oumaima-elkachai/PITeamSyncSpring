package tn.esprit.spring.teamsync.Services.Interfaces;

import tn.esprit.spring.teamsync.Entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    Task updateTask(String id, Task task);
    boolean deleteTask(String id);
    Task getTaskById(String id);
    List<Task> getAllTasks();
}
