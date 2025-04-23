package tn.esprit.spring.teamsync.Controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.teamsync.Entity.Task;
import tn.esprit.spring.teamsync.Services.Interfaces.TaskService;
import tn.esprit.spring.teamsync.Services.MPL.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    // TaskController.java
    @PostMapping("/{taskId}/links")
    public ResponseEntity<Task> addLink(
            @PathVariable String taskId,
            @RequestBody Map<String, String> linkRequest
    ) {
        Task updatedTask = taskService.addLink(taskId, linkRequest);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}/links/{linkIndex}")
    public ResponseEntity<Task> removeLink(
            @PathVariable String taskId,
            @PathVariable int linkIndex
    ) {
        Task updatedTask = taskService.removeLink(taskId, linkIndex);
        return ResponseEntity.ok(updatedTask);
    }


    // Create
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskService.createTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/request-extension")
    public ResponseEntity<Task> requestExtension(
            @PathVariable String id,
            @RequestParam("newDeadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDeadline
    ) {
        Task updatedTask = taskService.requestExtension(id, newDeadline);
        return ResponseEntity.ok(updatedTask);
    }

    // Read All
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // Read One
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable String id) {
        Task task = taskService.getTaskById(id);
        return task != null
                ? ResponseEntity.ok(task)
                : ResponseEntity.notFound().build();
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable String id,
            @RequestBody Task task
    ) {
        Task updatedTask = taskService.updateTask(id, task);
        return updatedTask != null
                ? ResponseEntity.ok(updatedTask)
                : ResponseEntity.notFound().build();
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-project")
    public ResponseEntity<?> getTasksByProject(
            @RequestParam String projectId
    ) {
        try {
            List<Task> tasks = taskService.getTasksByProject(projectId);
            if (tasks.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to retrieve tasks: " + e.getMessage());
        }
    }

    // TaskController.java
    @PatchMapping("/{id}/approve-extension")
    public ResponseEntity<Task> approveExtension(@PathVariable String id) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if ("PENDING".equals(task.getExtensionStatus())) { // Use string comparison
            task.setDeadline(task.getRequestedExtensionDate());
            task.setExtensionStatus("APPROVED"); // Direct string assignment
        }

        return ResponseEntity.ok(taskService.save(task));
    }

    @PatchMapping("/{id}/reject-extension")
    public ResponseEntity<Task> rejectExtension(@PathVariable String id) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setExtensionStatus("REJECTED"); // Direct string assignment
        return ResponseEntity.ok(taskService.save(task));
    }


}