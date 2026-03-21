package com.Merlin.Inventory.Management.System.Notification;

import com.Merlin.Inventory.Management.System.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "receiver_id"
    )
    @JsonBackReference
    private User receiver;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private boolean isRead;
    private LocalDate creationDate;

   public Notification() {}

    public Notification(User receiver, String message, NotificationType notificationType) {
       this.receiver = receiver;
        this.message = message;
        this.notificationType = notificationType;
        this.isRead = false;
        this.creationDate = LocalDate.now();
    }
}
