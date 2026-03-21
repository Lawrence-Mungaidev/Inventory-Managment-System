package com.Merlin.Inventory.Management.System.Notification;

public record NotificationDto(
        Long receiverId,
        String message,
        NotificationType notificationType
) {
}
