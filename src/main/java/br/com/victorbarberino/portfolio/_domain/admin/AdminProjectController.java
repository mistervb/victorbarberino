package br.com.victorbarberino.portfolio._domain.admin;

import br.com.victorbarberino.portfolio._system.enums.NotificationType;
import br.com.victorbarberino.portfolio._system.generics.WebController;
import br.com.victorbarberino.portfolio._domain.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.victorbarberino.portfolio._domain.project.ProjectData;
import java.util.UUID;

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
    public ModelAndView saveProject(@ModelAttribute @Validated ProjectData projectData, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("/admin/project/new-project");

            StringBuilder errorMessages = new StringBuilder();
            result.getFieldErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("<br/>"));

            addNotification(NotificationType.error, errorMessages.toString());
            mv.addObject("projectData", projectData);
            return dispatchMv(mv);
        }

        ModelAndView mv = new ModelAndView("redirect:/admin/projects");
        if (projectService.saveProject(projectData) == null) {
            mv = new ModelAndView("/admin/project/new-project");
            addNotification(NotificationType.error, "Ocorreu um erro desconhecido durante o cadastro de projetos.");
            mv.addObject("projectData", projectData);
            return dispatchMv(mv);
        }

        addNotification(NotificationType.success, "Operação realizada com sucesso!");
        return dispatchMv(mv);
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
