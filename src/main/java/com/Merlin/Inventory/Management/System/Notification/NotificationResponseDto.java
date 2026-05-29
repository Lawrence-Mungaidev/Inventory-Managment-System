package com.Merlin.Inventory.Management.System.Notification;

public record NotificationResponseDto(
        Long Id,
        String message,
        NotificationType notificationType,
        boolean isRead
) {
}
