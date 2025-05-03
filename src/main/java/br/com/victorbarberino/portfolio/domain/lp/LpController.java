package br.com.victorbarberino.portfolio.domain.lp;

import br.com.victorbarberino.portfolio.domain.project.ProjectService;
import br.com.victorbarberino.portfolio.domain.section.SectionService;
import br.com.victorbarberino.portfolio.domain.testimonial.TestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@RequestMapping("/")
public class LpController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TestimonialService testimonialService;
    @Autowired
    private SectionService sectionService;

    @GetMapping
    public ModelAndView dispatchLandingPageView() {
        ModelAndView mv = new ModelAndView("index");
        Integer actualYear = LocalDate.now().getYear();

        mv.addObject("actualYear", actualYear);
        mv.addObject("projects", projectService.getAllProjects());
        mv.addObject("testimonials", testimonialService.getAllTestimonials());
        
        // Adiciona as seções visíveis para controlar o que aparece no menu e na página
        mv.addObject("sections", sectionService.getVisibleSections());
        
        return mv;
    }
}
