package tn.esprit.spring.teamsync.Services.MPL;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.teamsync.Entity.Project;
import tn.esprit.spring.teamsync.Repository.ProjectRepository;
import tn.esprit.spring.teamsync.Services.Interfaces.ProjectService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(String id, Project updatedProject) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) return null;

        updatedProject.setId(id);
        return projectRepository.save(updatedProject);
    }

    @Override
    public boolean deleteProject(String id) {
        projectRepository.deleteById(id);
        return false;
    }

    @Override
    public Project getProjectById(String id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
