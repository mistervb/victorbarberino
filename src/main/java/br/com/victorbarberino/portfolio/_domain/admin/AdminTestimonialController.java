package br.com.victorbarberino.portfolio._domain.admin;

import br.com.victorbarberino.portfolio._system.generics.WebController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/testimonials")
public class AdminTestimonialController extends WebController {
    @GetMapping
    public ModelAndView dispatchTestimonialsView() {
        ModelAndView mv = new ModelAndView("admin/testimonial/testimonial");
        return dispatchMv(mv);
    }

    @GetMapping("/new")
    public ModelAndView dispatchNewTestimonialsView() {
        ModelAndView mv = new ModelAndView("admin/testimonial/new-testimonial");
        return dispatchMv(mv);
    }

}
