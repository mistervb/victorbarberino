package br.com.victorbarberino.portfolio.domain.admin;

import br.com.victorbarberino.portfolio.system.enums.NotificationType;
import br.com.victorbarberino.portfolio.system.generics.WebController;
import br.com.victorbarberino.portfolio.domain.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.com.victorbarberino.portfolio.domain.project.ProjectData;
import br.com.victorbarberino.portfolio.domain.project.ProjectEntity;
import br.com.victorbarberino.portfolio.domain.project.ProjectRepository;
import java.util.UUID;
import java.util.Optional;

@Controller
@RequestMapping("/admin/projects")
public class AdminProjectController extends WebController {
    @Autowired
    private ProjectService projectService;

    public AdminProjectController() {
        super();
    }

    @GetMapping
    public ModelAndView dispatchAdminProjectsView() {
        ModelAndView mv = new ModelAndView("/admin/project/projects");
        mv.addObject("projects", projectService.getAllProjects());
        return dispatchMv(mv);
    }

    @GetMapping("/new")
    public ModelAndView dispatchNewProjectView() {
        ModelAndView mv = new ModelAndView("/admin/project/new-project");
        mv.addObject("projectData", new ProjectData());
        return dispatchMv(mv);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProjectView(@PathVariable UUID id) {
        ProjectData project = projectService.getProjectById(id);
        if (project == null) {
            addNotification(NotificationType.error, "Projeto não encontrado.");
            return dispatchMv(new ModelAndView("redirect:/admin/projects"));
        }
        ModelAndView mv = new ModelAndView("/admin/project/edit-project");
        mv.addObject("projectData", project);
        return dispatchMv(mv);
    }

    @PostMapping("/action/save")
    public ModelAndView saveProject(@ModelAttribute @Validated ProjectData projectData,
                                   BindingResult result,
                                   @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                   RedirectAttributes ra) {
        // Garante que o ProjectData recebe o arquivo (caso não seja setado automaticamente)
        if (imageFile != null && !imageFile.isEmpty()) {
            projectData.setImageFile(imageFile);
        }
        if (!projectData.isImageValid()) {
            result.rejectValue("image", null, "É necessário fornecer uma imagem (URL ou upload de arquivo).");
        }
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("/admin/project/new-project");
            mv.addObject("projectData", projectData);
            System.out.println("Deu erro aew");
            if(result.getErrorCount() > 0) {
                for(ObjectError error : result.getAllErrors()) {
                    System.out.println(error.getDefaultMessage());
                }
            }
            return dispatchMv(mv);
        }
        try {
            projectService.saveProject(projectData);
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("/admin/project/new-project");
            mv.addObject("projectData", projectData);
            addNotification(NotificationType.error, e.getMessage());
            return dispatchMv(mv);
        }
        addNotification(NotificationType.success, "Operação realizada com sucesso!");
        return dispatchMv(new ModelAndView("redirect:/admin/projects"));
    }

    @PostMapping("/action/update/{id}")
    public ModelAndView updateProject(@PathVariable UUID id, @ModelAttribute @Validated ProjectData projectData, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("/admin/project/edit-project");
            StringBuilder errorMessages = new StringBuilder();
            result.getFieldErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("<br/>"));
            addNotification(NotificationType.error, errorMessages.toString());
            mv.addObject("projectData", projectData);
            return dispatchMv(mv);
        }
        projectData.setProjectId(id);
        if (projectService.updateProject(projectData) == null) {
            ModelAndView mv = new ModelAndView("/admin/project/edit-project");
            addNotification(NotificationType.error, "Ocorreu um erro ao atualizar o projeto.");
            mv.addObject("projectData", projectData);
            return dispatchMv(mv);
        }
        addNotification(NotificationType.success, "Projeto atualizado com sucesso!");
        return dispatchMv(new ModelAndView("redirect:/admin/projects"));
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteProject(@PathVariable UUID id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            addNotification(NotificationType.success, "Projeto excluído com sucesso!");
        } else {
            addNotification(NotificationType.error, "Erro ao excluir projeto.");
        }
        return dispatchMv(new ModelAndView("redirect:/admin/projects"));
    }
}

@RestController
class ProjectImageController {
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/project/img")
    public ResponseEntity<byte[]> getProjectImage(@RequestParam(required = false, name = "id") UUID id) {
        Optional<ProjectEntity> projectOpt = projectRepository.findById(id);
        if (projectOpt.isEmpty() || projectOpt.get().getImageBytes() == null) {
            return ResponseEntity.notFound().build();
        }
        ProjectEntity project = projectOpt.get();
        String contentType = project.getImageContentType() != null ? project.getImageContentType() : "image/jpeg";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(project.getImageBytes());
    }
}
