package com.Merlin.Inventory.Management.System.Notification;

import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.User.ROLE;
import com.Merlin.Inventory.Management.System.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    NotificationRepository notificationRepository;

    @Mock
    NotificationMapper notificationMapper;



    @InjectMocks
    NotificationService notificationService;

    @Test
    void create_notification(){
        User receiver = new User(
                "John",
                "Mwangi",
                "test2@gmail.com",
                "123456789QM",
                "0723 344 454", ROLE.ADMIN);
        receiver.setUserId(3L);

        String message = "product A has expired";

        notificationService.createNotification(receiver, message, NotificationType.EXPIRED);

        verify(notificationRepository).save(argThat((Notification n) -> n.getReceiver().equals(receiver) &&
                        n.getMessage().equals(message) &&
                        n.getNotificationType() == NotificationType.EXPIRED
        ));
    }

    @Test
    void mark_Notification_as_Read(){
        User receiver = new User(
                "John",
                "Mwangi",
                "test2@gmail.com",
                "123456789QM",
                "0723 344 454", ROLE.ADMIN);
        receiver.setUserId(3L);

        String message = "product A has expired";

        Notification notification = new Notification(receiver, message, NotificationType.EXPIRED);
        notification.setId(4L);
        notification.setRead(true);

        when(notificationRepository.findById(4L)).thenReturn(Optional.of(notification));

        notificationService.markNotificationAsRead(notification.getId());

        verify(notificationRepository).save(argThat((Notification n ) ->  n.isRead() == true)) ;

    }

    @Test
    void return_Resource_NotFound_for_missing_notification(){
        User receiver = new User(
                "John",
                "Mwangi",
                "test2@gmail.com",
                "123456789QM",
                "0723 344 454", ROLE.ADMIN);
        receiver.setUserId(3L);

        String message = "product A has expired";

        Notification notification = new Notification(receiver, message, NotificationType.EXPIRED);
        notification.setId(4L);

        when(notificationRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> notificationService.markNotificationAsRead(notification.getId())  );

        verify(notificationRepository, never()).save(any());

    }

    @Test
    void get_List_of_Notifications_By_UserId(){
        User user = new User(
                "John",
                "Mwangi",
                "test2@gmail.com",
                "123456789QM",
                "0723 344 454", ROLE.ADMIN);
        user.setUserId(3L);

        String message = "product A has expired";

        Notification notification = new Notification(user, message, NotificationType.EXPIRED);
        notification.setId(4L);

        List<Notification> notifications = new ArrayList<>();
        notifications.add(notification);

        when(notificationRepository.findByReceiverOrderByCreationDateDesc(user)).thenReturn(notifications);
        when(notificationMapper.toNotificationResponseDto(any())).thenReturn(new NotificationResponseDto( 4L, "product A has expired",NotificationType.EXPIRED,notification.isRead()));


        List<NotificationResponseDto> responseList = notificationService.getNotificationByUserId(user);

        assertEquals(1, responseList.size());
    }

    @Test
    void get_Notifications_By_Id(){
        User user = new User(
                "John",
                "Mwangi",
                "test2@gmail.com",
                "123456789QM",
                "0723 344 454", ROLE.ADMIN);
        user.setUserId(3L);

        String message = "product A has expired";

        Notification notification = new Notification(user, message, NotificationType.EXPIRED);
        notification.setId(4L);

        when(notificationRepository.findById(notification.getId())).thenReturn(Optional.of(notification));
        when(notificationMapper.toNotificationResponseDto(any())).thenReturn(new NotificationResponseDto( 4L, "product A has expired",NotificationType.EXPIRED,notification.isRead()));

        NotificationResponseDto responseDto = notificationService.getNotificationById(notification.getId());

        assertEquals(notification.getMessage(), responseDto.message());
        assertEquals(notification.getId(), responseDto.Id());

    }

    @Test
    void return_Resource_Not_Found_for_missing_notification(){
        Long notificationId = 4L;
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> notificationService.markNotificationAsRead(notificationId));

    }


}