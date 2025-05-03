package br.com.victorbarberino.portfolio.domain.notification;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NotificationService {
    
    private final List<Notification> notifications = new ArrayList<>();
    
    public void addNotification(String message, NotificationType type) {
        Notification notification = new Notification(message, type);
        notifications.add(notification);
    }
    
    public List<Notification> getNotifications() {
        List<Notification> currentNotifications = Collections.unmodifiableList(notifications);
        notifications.clear();
        return currentNotifications;
    }
    
    public boolean hasNotifications() {
        return !notifications.isEmpty();
    }
    
    public void clearNotifications() {
        notifications.clear();
    }
}
