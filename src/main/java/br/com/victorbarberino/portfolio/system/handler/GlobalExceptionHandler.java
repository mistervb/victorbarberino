package br.com.victorbarberino.portfolio.system.handler;

import br.com.victorbarberino.portfolio.system.generics.WebController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import br.com.victorbarberino.portfolio.system.enums.NotificationType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.FieldError;

@ControllerAdvice
public class GlobalExceptionHandler extends WebController {
    public GlobalExceptionHandler() {
        super();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationException(MethodArgumentNotValidException ex) {
        ModelAndView mv = new ModelAndView("/admin/project/new-project");

        Object target = ex.getBindingResult().getTarget();
        if (target != null) {
            mv.addObject("projectData", target);
        }

        StringBuilder errorMessages = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessages.append(fieldError.getDefaultMessage()).append("<br/>");
        }

        addNotification(NotificationType.error, errorMessages.toString());
        return dispatchMv(mv); // Sem RedirectAttributes
    }

}
