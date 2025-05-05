package br.com.victorbarberino.portfolio.domain.admin;

import br.com.victorbarberino.portfolio.domain.testimonial.TestimonialData;
import br.com.victorbarberino.portfolio.domain.testimonial.TestimonialService;
import br.com.victorbarberino.portfolio.system.enums.NotificationType;
import br.com.victorbarberino.portfolio.system.generics.WebController;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import java.util.Optional;
import java.util.UUID;
import br.com.victorbarberino.portfolio.domain.testimonial.TestimonialRepository;
import br.com.victorbarberino.portfolio.domain.testimonial.TestimonialEntity;

@Controller
@RequestMapping("/admin/testimonials")
public class AdminTestimonialController extends WebController {

    @Autowired
    private TestimonialService testimonialService;

    public AdminTestimonialController() {
        super();
    }

    @GetMapping
    public ModelAndView dispatchAdminTestimonialsView() {
        ModelAndView mv = new ModelAndView("admin/testimonial/testimonial");
        mv.addObject("testimonials", testimonialService.getAllTestimonials());
        return dispatchMv(mv);
    }

    @GetMapping("/new")
    public ModelAndView dispatchNewTestimonialView() {
        ModelAndView mv = new ModelAndView("admin/testimonial/new-testimonial");
        mv.addObject("testimonialData", new TestimonialData());
        return dispatchMv(mv);
    }

    @PostMapping("/action/save")
    public ModelAndView saveTestimonial(@ModelAttribute @Validated TestimonialData testimonialData,
                                        BindingResult result,
                                        @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                                        RedirectAttributes ra) {
        // Garante que o TestimonialData recebe o arquivo (caso não seja setado automaticamente)
        if (avatarFile != null && !avatarFile.isEmpty()) {
            testimonialData.setAvatarFile(avatarFile);
        }
        if (!testimonialData.isAvatarValid()) {
            result.rejectValue("avatar", null, "É necessário fornecer uma foto (URL ou upload de arquivo).");
        }
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("admin/testimonial/new-testimonial");
            mv.addObject("testimonialData", testimonialData);
            return dispatchMv(mv);
        }
        try {
            testimonialService.saveTestimonial(testimonialData);
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("admin/testimonial/new-testimonial");
            mv.addObject("testimonialData", testimonialData);
            addNotification(NotificationType.error, e.getMessage());
            return dispatchMv(mv);
        }
        addNotification(NotificationType.success, "Operação realizada com sucesso!");
        return dispatchMv(new ModelAndView("redirect:/admin/testimonials"));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editTestimonialView(@PathVariable UUID id) {
        TestimonialData data = testimonialService.getTestimonialById(id);
        if (data == null) {
            addNotification(NotificationType.error, "Testemunha não encontrada.");
            return dispatchMv(new ModelAndView("redirect:/admin/testimonials"));
        }
        ModelAndView mv = new ModelAndView("admin/testimonial/edit-testimonial");
        mv.addObject("testimonialData", data);
        return dispatchMv(mv);
    }

    @PostMapping("/action/update/{id}")
    public ModelAndView updateTestimonial(@PathVariable UUID id, @ModelAttribute @Validated TestimonialData testimonialData, BindingResult result, RedirectAttributes ra) {
        if (!testimonialData.isAvatarValid()) {
            result.rejectValue("avatar", "400", "É necessário fornecer uma foto (URL ou upload de arquivo).");
        }
        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("admin/testimonial/edit-testimonial");
            mv.addObject("testimonialData", testimonialData);
            return dispatchMv(mv);
        }
        try {
            testimonialData.setId(id);
            TestimonialData updated = testimonialService.updateTestimonial(testimonialData);
            if (updated == null) {
                addNotification(NotificationType.error, "Ocorreu um erro ao atualizar depoimento.");
                ModelAndView mv = new ModelAndView("admin/testimonial/edit-testimonial");
                mv.addObject("testimonialData", testimonialData);
                return dispatchMv(mv);
            }
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("admin/testimonial/edit-testimonial");
            mv.addObject("testimonialData", testimonialData);
            addNotification(NotificationType.error, e.getMessage());
            return dispatchMv(mv);
        }
        addNotification(NotificationType.success, "Depoimento atualizado com sucesso!");
        return dispatchMv(new ModelAndView("redirect:/admin/testimonials"));
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteTestimonial(@PathVariable UUID id) {
        boolean deleted = testimonialService.deleteTestimonial(id);
        if (deleted) {
            addNotification(NotificationType.success, "Depoimento excluído com sucesso!");
        } else {
            addNotification(NotificationType.error, "Erro ao excluir depoimento.");
        }
        return dispatchMv(new ModelAndView("redirect:/admin/testimonials"));
    }
}

@RestController
class TestimonialImageController {
    @Autowired
    private TestimonialRepository testimonialRepository;

    @GetMapping("/testimonial/img")
    public ResponseEntity<byte[]> getTestimonialImage(
            @RequestParam(required = false, name = "id") UUID id) {
        Optional<TestimonialEntity> opt = testimonialRepository.findById(id);
        if (opt.isEmpty() || opt.get().getAvatarBytes() == null) {
            return ResponseEntity.notFound().build();
        }
        TestimonialEntity entity = opt.get();
        String contentType = entity.getAvatarContentType() != null 
                ? entity.getAvatarContentType() 
                : "image/jpeg";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(entity.getAvatarBytes());
    }
}
