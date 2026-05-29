package com.Merlin.Inventory.Management.System.Notification;

import com.Merlin.Inventory.Management.System.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByReceiverOrderByCreationDateDesc(User user);

}

