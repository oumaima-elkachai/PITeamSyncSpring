package tn.esprit.spring.teamsync.Services.Interfaces;

import tn.esprit.spring.teamsync.Entity.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project);
    Project updateProject(String id, Project project);
    boolean deleteProject(String id);
    Project getProjectById(String id);
    List<Project> getAllProjects();
}
