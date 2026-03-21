package com.Merlin.Inventory.Management.System.Notification;

import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {


    public NotificationResponseDto toNotificationResponseDto(Notification notification){
        return new NotificationResponseDto(notification.getMessage(), notification.getNotificationType());
    }
}
