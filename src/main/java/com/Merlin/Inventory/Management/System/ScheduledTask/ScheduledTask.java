package com.Merlin.Inventory.Management.System.ScheduledTask;

import com.Merlin.Inventory.Management.System.Email.EmailService;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Notification.NotificationService;
import com.Merlin.Inventory.Management.System.Notification.NotificationType;
import com.Merlin.Inventory.Management.System.Stock.Stock;
import com.Merlin.Inventory.Management.System.Stock.StockRepository;
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
    private final StockRepository stockRepository;

    @Scheduled(cron = "0 30 22 * * *", zone = "Africa/Nairobi")
    public void scheduledTask(){
        List<Transaction> transactions = transactionRepository.findByStatus(Status.CANCELLED);
        transactions.forEach(transactionRepository::delete);

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        BigDecimal totalSales = transactionRepository.getTotalSalesByDateBetween(start, end);

        List<User> admins = userRepository.findByRole(ROLE.ADMIN);

        for (User admin :  admins){
            String message = LocalDate.now().toString() + " TotalSales " + totalSales;

            notificationService.createNotification(admin, message, NotificationType.DAILY_SALES);
            emailService.sendEmail(admin.getEmail(), " Quick Save Inventory System", message);
        }

    }

    @Scheduled(cron = "0 0 8 * * *", zone = "Africa/Nairobi")
    public void checkExpiringProduct(){
        LocalDate today = LocalDate.now();

        List<Stock> expired = stockRepository.findByExpiryDateBefore(today);

        List<Stock> aboutToExpire = stockRepository.findByExpiryDateBetween(today , today.plusDays(7));

        List<User> allUsers = userRepository.findAll();


        for(Stock stock : expired){
            String expiredMessage = stock.getProduct().getProductName() + " batch that arrived on "+ stock.getArrivalDate() + ", has reached its expiration date ";
            for(User user : allUsers){
                notificationService.createNotification(user, expiredMessage, NotificationType.EXPIRED);
            }
        }

        for(Stock stock : aboutToExpire){
            String aboutToExpireMessage = stock.getProduct().getProductName() + " batch that arrived on "+ stock.getArrivalDate() + ", expires in 7 days ";
            for(User user : allUsers){
                notificationService.createNotification(user, aboutToExpireMessage, NotificationType.ITEMS_ABOUT_TO_EXPIRE);
            }
        }
    }

}
