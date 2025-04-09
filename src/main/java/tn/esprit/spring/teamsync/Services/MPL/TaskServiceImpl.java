package tn.esprit.spring.teamsync.Services.MPL;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.teamsync.Entity.Task;
import tn.esprit.spring.teamsync.Repository.TaskRepository;
import tn.esprit.spring.teamsync.Services.Interfaces.TaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(String id, Task updatedTask) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) return null;

        updatedTask.setId(id);
        return taskRepository.save(updatedTask);
    }

    @Override
    public boolean deleteTask(String id) {
        taskRepository.deleteById(id);
        return false;
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
