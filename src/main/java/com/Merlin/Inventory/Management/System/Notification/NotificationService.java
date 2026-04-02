package com.Merlin.Inventory.Management.System.Notification;

import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.User.User;
import com.Merlin.Inventory.Management.System.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    public NotificationResponseDto createNotification(User receiver,String message,NotificationType notificationType){
        Notification notification = new Notification(receiver,message,notificationType);

        notification.setReceiver(receiver);
        var savedNotification = notificationRepository.save(notification);

        return notificationMapper.toNotificationResponseDto(savedNotification);
    }

    public void markNotificationAsRead(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDto> getNotificationByUserId(User authenticatedUser){
        Long userId = authenticatedUser.getUserId();

        return notificationRepository.findByReceiverOrderByCreationDateDesc(userId)
                .stream()
                .map(notificationMapper :: toNotificationResponseDto)
                .toList();
    }

    public NotificationResponseDto getNotificationById(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        return notificationMapper.toNotificationResponseDto(notification);
    }


}
