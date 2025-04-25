package br.com.victorbarberino.portfolio.system.dto;

import br.com.victorbarberino.portfolio.system.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationData {
    private NotificationType type;
    private String message;
}
