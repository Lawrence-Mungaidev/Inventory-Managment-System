package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Notification.NotificationService;
import com.Merlin.Inventory.Management.System.Notification.NotificationType;
import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Product.ProductRepository;
import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItemRepository;
import com.Merlin.Inventory.Management.System.User.ROLE;
import com.Merlin.Inventory.Management.System.User.User;
import com.Merlin.Inventory.Management.System.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final ProductRepository productRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public TransactionResponseDto create(TransactionDTO dto, User authenticatedUser) {
        Transaction transaction = transactionMapper.toTransaction(dto);

        Transaction savedTransaction;

        TransactionItemResult result = buildTransactionItem(dto.items());
        List<TransactionItem> transactionItems = result.transactionItemList();
        BigDecimal totalAmount = result.totalAmount();

        transaction.setTotalAmount(totalAmount);
        transaction.setCreatedBy(authenticatedUser);

        if (dto.paymentMethod().equals(PaymentMethod.CASH)) {
            transaction.setStatus(Status.COMPLETED);
            deductStock(transactionItems);

            savedTransaction = transactionRepository.save(transaction);
            transactionItems.forEach(item -> {
                item.setTransaction(savedTransaction);
                transactionItemRepository.save(item);
                savedTransaction.setTransactionItems(transactionItems);
            });
        } else if (dto.paymentMethod().equals(PaymentMethod.MPESA)) {
            transaction.setStatus(Status.PENDING);

            savedTransaction = transactionRepository.save(transaction);
            transactionItems.forEach(item -> {
                item.setTransaction(savedTransaction);
                transactionItemRepository.save(item);
                savedTransaction.setTransactionItems(transactionItems);
            });
        } else {
            savedTransaction = null;
        }

        return transactionMapper.toTransactionResponseDto(savedTransaction);
    }

    private TransactionItemResult buildTransactionItem(List<TransactionItemRequest> transactionItemsRequest) {
        List<TransactionItem> transactionItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for(TransactionItemRequest itemRequest: transactionItemsRequest){
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            if (product.getCurrentStock() < itemRequest.quantity()){
                throw new RuntimeException(product.getProductName() + " not enough stock");
            }

            TransactionItem transactionItem = new TransactionItem(product, itemRequest.quantity());
            transactionItems.add(transactionItem);
            totalAmount = totalAmount.add(transactionItem.getSubtotal());
        }

        return new TransactionItemResult(transactionItems, totalAmount);
    }

    private void deductStock(List<TransactionItem> transactionItem){
        for( TransactionItem itemRequest: transactionItem){
            Product product = itemRequest.getProduct();

            product.setCurrentStock(product.getCurrentStock() - itemRequest.getQuantity());
            productRepository.save(product);

            String message = product.getProductName() + " is running Low\nRemining quantity: " + product.getCurrentStock();

            if(product.getCurrentStock() < product.getMinimumQuantity()){
                User admin  = userRepository.findByRole(ROLE.ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                notificationService.createNotification(admin, message, NotificationType.LOW_STOCK);
            }
        }
    }

    public List<TransactionResponseDto> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper :: toTransactionResponseDto)
                .toList();
    }

    public TransactionResponseDto getTransactionById(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        return transactionMapper.toTransactionResponseDto(transaction);
    }

    public List<TransactionResponseDto> getTodayTransactions() {

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23,59,59);

      return transactionRepository.findByTransactionDateBetween(start,end)
              .stream()
              .map(transactionMapper ::toTransactionResponseDto)
              .toList();
    }

    public List<TransactionResponseDto> getTransactionByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTransactionDateBetween(start,end)
                .stream()
                .map(transactionMapper ::toTransactionResponseDto)
                .toList();
    }
}
