package br.com.victorbarberino.portfolio.domain.admin;

import br.com.victorbarberino.portfolio.domain.notification.NotificationService;
import br.com.victorbarberino.portfolio.domain.notification.NotificationType;
import br.com.victorbarberino.portfolio.domain.section.SectionData;
import br.com.victorbarberino.portfolio.domain.section.SectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin/sections")
public class AdminSectionController {

    private final SectionService sectionService;
    private final NotificationService notificationService;

    @Autowired
    public AdminSectionController(SectionService sectionService, NotificationService notificationService) {
        this.sectionService = sectionService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ModelAndView listSections() {
        ModelAndView mv = new ModelAndView("admin/section/sections");
        mv.addObject("sections", sectionService.getAllSections());
        mv.addObject("notifications", notificationService.getNotifications());
        return mv;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editSectionForm(@PathVariable UUID id) {
        try {
            ModelAndView mv = new ModelAndView("admin/section/edit-section");
            mv.addObject("sectionData", sectionService.getSectionById(id));
            return mv;
        } catch (Exception e) {
            notificationService.addNotification("Seção não encontrada", NotificationType.ERROR);
            return new ModelAndView("redirect:/admin/sections");
        }
    }

    @PostMapping("/action/update/{id}")
    public ModelAndView updateSection(@PathVariable UUID id, @ModelAttribute @Valid SectionData sectionData, 
                                     BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("admin/section/edit-section");
            mv.addObject("sectionData", sectionData);
            notificationService.addNotification("Erro ao atualizar: verifique os campos", NotificationType.ERROR);
            mv.addObject("notifications", notificationService.getNotifications());
            return mv;
        }

        try {
            sectionService.updateSection(id, sectionData);
            notificationService.addNotification("Seção atualizada com sucesso!", NotificationType.SUCCESS);
            return new ModelAndView("redirect:/admin/sections");
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("admin/section/edit-section");
            mv.addObject("sectionData", sectionData);
            notificationService.addNotification("Erro ao atualizar: " + e.getMessage(), NotificationType.ERROR);
            mv.addObject("notifications", notificationService.getNotifications());
            return mv;
        }
    }

    @GetMapping("/toggle/{id}")
    public String toggleSectionVisibility(@PathVariable UUID id) {
        try {
            sectionService.toggleSectionVisibility(id);
            notificationService.addNotification("Visibilidade da seção alterada com sucesso!", NotificationType.SUCCESS);
        } catch (Exception e) {
            notificationService.addNotification("Erro ao alterar visibilidade: " + e.getMessage(), NotificationType.ERROR);
        }
        return "redirect:/admin/sections";
    }
}
