package com.Merlin.Inventory.Management.System.Notification;

public record NotificationResponseDto(
        String message,
        NotificationType notificationType
) {
}
