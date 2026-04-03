package com.Merlin.Inventory.Management.System.ScheduledTask;

import com.Merlin.Inventory.Management.System.Email.EmailService;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Notification.NotificationService;
import com.Merlin.Inventory.Management.System.Notification.NotificationType;
import com.Merlin.Inventory.Management.System.Transaction.Status;
import com.Merlin.Inventory.Management.System.Transaction.Transaction;
import com.Merlin.Inventory.Management.System.Transaction.TransactionRepository;
import com.Merlin.Inventory.Management.System.User.ROLE;
import com.Merlin.Inventory.Management.System.User.User;
import com.Merlin.Inventory.Management.System.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    @Scheduled(cron = "0 30 22 * * *")
    public void scheduledTask(){
        List<Transaction> transactions = transactionRepository.findByTransactionStatus(Status.CANCELLED);
        transactions.forEach(transactionRepository::delete);

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        BigDecimal totalSales = transactionRepository.getTotalSalesByDateBetween(start, end);

        User admin = userRepository.findByRole(ROLE.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("no Admin was found"));

        String message = LocalDate.now().toString() + " TotalSales " + totalSales;

        notificationService.createNotification(admin, message, NotificationType.DAILY_SALES);
        emailService.sendEmail(admin.getEmail(), "Blessing Inventory Management System", message);

    }

}
