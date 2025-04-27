package tn.esprit.spring.teamsync.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.teamsync.Entity.Project;
import tn.esprit.spring.teamsync.Services.Interfaces.ProjectService;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Create
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        if (project.getDepartment() == null || project.getDepartment().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Project savedProject = projectService.createProject(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    // Read All
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    // Read One
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable String id) {
        Project project = projectService.getProjectById(id);
        return project != null
                ? ResponseEntity.ok(project)
                : ResponseEntity.notFound().build();
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(
            @PathVariable String id,
            @RequestBody Project project
    ) {
        Project updatedProject = projectService.updateProject(id, project);
        return updatedProject != null
                ? ResponseEntity.ok(updatedProject)
                : ResponseEntity.notFound().build();
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-department")
    public ResponseEntity<List<Project>> getProjectsByDepartment(
            @RequestParam String department
    ) {
        return ResponseEntity.ok(projectService.getProjectsByDepartment(department));
    }

}