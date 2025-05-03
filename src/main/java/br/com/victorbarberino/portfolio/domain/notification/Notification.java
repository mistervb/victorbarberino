package br.com.victorbarberino.portfolio.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String message;
    private NotificationType type;
    private long timestamp;
    
    public Notification(String message, NotificationType type) {
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
}
