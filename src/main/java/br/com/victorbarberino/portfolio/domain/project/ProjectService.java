package br.com.victorbarberino.portfolio.domain.project;

import br.com.victorbarberino.portfolio.system.utils.HttpUtil;
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
        // Lógica de acordo com a aba ativa
        String inputType = payload.getImageInputType();
        byte[] imageBytes = null;
        String imageContentType = null;
        if ("file".equals(inputType)) {
            // Ignora URL, processa só arquivo
            payload.setImage(null);
            if (payload.getImageFile() == null || payload.getImageFile().isEmpty()) {
                throw new IllegalArgumentException("É necessário enviar um arquivo de imagem.");
            }
            try {
                imageBytes = payload.getImageFile().getBytes();
                imageContentType = payload.getImageFile().getContentType();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar imagem: " + e.getMessage());
            }
        } else {
            // Ignora arquivo, processa só URL
            payload.setImageFile(null);
            if (payload.getImage() == null || payload.getImage().trim().isEmpty()) {
                throw new IllegalArgumentException("É necessário informar a URL da imagem.");
            }
        }
        ProjectEntity project = projectRepository.saveAndFlush(new ProjectEntity(payload, imageBytes, imageContentType));
        if ("file".equals(inputType)) {
            project.setImage(montaLinkProject(project));
            project.setWasUploaded(true);
            project = projectRepository.saveAndFlush(project);
        }

        return project.convertToData();
    }

    public ProjectData getProjectById(UUID id) {
        return projectRepository.findById(id).map(ProjectEntity::convertToData).orElse(null);
    }

    public ProjectData updateProject(ProjectData payload) {
        System.out.println(payload);
        ProjectEntity entity = projectRepository.findById(payload.getProjectId()).orElse(null);
        if (entity == null) return null;
        entity.setTitle(payload.getTitle());
        entity.setDescription(payload.getDescription());
        entity.setTags(payload.getTags());

        String inputType = payload.getImageInputType();
        boolean hasNewFile = payload.getImageFile() != null && !payload.getImageFile().isEmpty();
        boolean hasNewUrl = payload.getImage() != null && !payload.getImage().trim().isEmpty();

        if ("file".equals(inputType) && hasNewFile) {
            entity.setImage(null);
            try {
                entity.setImageBytes(payload.getImageFile().getBytes());
                entity.setImageContentType(payload.getImageFile().getContentType());
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar imagem: " + e.getMessage());
            }

            entity = projectRepository.saveAndFlush(entity);
            entity.setWasUploaded(true);
            entity.setImage(montaLinkProject(entity));
        } else if (!"file".equals(inputType) && hasNewUrl) {
            entity.setImageBytes(null);
            entity.setImageContentType(null);
            entity.setWasUploaded(false);
            entity.setImage(payload.getImage());
        }
        entity = projectRepository.saveAndFlush(entity);
        return entity.convertToData();
    }

    public boolean deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) return false;
        projectRepository.deleteById(id);
        return true;
    }

    private String montaLinkProject(ProjectEntity project) {
        return HttpUtil.getDynamicAppDomain() + "/project/img?id=" + project.getId();
    }
}
