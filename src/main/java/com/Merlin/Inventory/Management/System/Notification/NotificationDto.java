package com.Merlin.Inventory.Management.System.Notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationDto(
        @NotNull(message = "receiver is required")
        Long receiverId,
        @NotBlank(message = "message is required")
        String message,
        @NotNull(message = "Notification type is required")
        NotificationType notificationType
) {
}
