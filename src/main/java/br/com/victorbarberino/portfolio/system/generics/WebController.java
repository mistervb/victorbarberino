package br.com.victorbarberino.portfolio.system.generics;

import br.com.victorbarberino.portfolio.system.dto.NotificationData;
import br.com.victorbarberino.portfolio.system.enums.NotificationType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

public abstract class WebController {
    private List<NotificationData> notifications;

    public WebController() {
        this.notifications = new ArrayList<>();
    }

    public void addNotification(NotificationType type, String message) {
        if(notifications == null) {
            return;
        }
        this.notifications.add(new NotificationData(type, message));
    }

    public List<NotificationData> getAllNotifications() {
        return this.notifications;
    }

    /* ModelAndView And Web Classes Features */
    public ModelAndView dispatchMv(ModelAndView mv) {
        mv.addObject("notifications", getAllNotifications());
        System.out.println("olha ae:" + getAllNotifications());
        /* Todos os objetos fixos do mv entram aqui. */

        // evitar memory leak, o cliente agradece :D
        this.notifications.clear();
        return mv;
    }
    public ModelAndView dispatchMv(ModelAndView mv, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("notifications", getAllNotifications());
        return mv;
    }
}
