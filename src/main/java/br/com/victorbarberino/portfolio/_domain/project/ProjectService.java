package br.com.victorbarberino.portfolio._domain.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    public List<ProjectData> getAllProjects() {
        return projectRepository.findAll().stream().map(ProjectEntity::convertToData).toList();
    }

    public ProjectData saveProject(ProjectData payload) {
        return projectRepository.saveAndFlush(new ProjectEntity(payload)).convertToData();
    }

    public ProjectData getProjectById(UUID id) {
        return projectRepository.findById(id).map(ProjectEntity::convertToData).orElse(null);
    }

    public ProjectData updateProject(ProjectData payload) {
        // Busca a entidade existente
        ProjectEntity entity = projectRepository.findById(payload.getProjectId()).orElse(null);
        if (entity == null) return null;
        entity.setTitle(payload.getTitle());
        entity.setDescription(payload.getDescription());
        entity.setImage(payload.getImage());
        entity.setTags(payload.getTags());
        return projectRepository.saveAndFlush(entity).convertToData();
    }

    public boolean deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) return false;
        projectRepository.deleteById(id);
        return true;
    }
}
