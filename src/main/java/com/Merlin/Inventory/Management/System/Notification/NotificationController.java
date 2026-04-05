package com.Merlin.Inventory.Management.System.Notification;

import com.Merlin.Inventory.Management.System.User.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService  notificationService;

    @PatchMapping("/markasread/{notificationId}")
    public ResponseEntity<Void> markAsRead(@Valid @PathVariable Long notificationId){
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getAllNotifications(@AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationByUserId(authenticatedUser));
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponseDto> getNotification(@PathVariable Long notificationId){
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationById(notificationId));
    }
}
