package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Product.ProductRepository;
import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

   private final ProductRepository productRepository;

    public Transaction toTransaction(TransactionDTO dto){
        Transaction transaction = new Transaction();

        transaction.setPaymentMethod(dto.paymentMethod());
        transaction.setPhoneNumber(dto.phoneNumber());
        transaction.setTransactionDate(LocalDateTime.now());

        return transaction;
    }

    public TransactionResponse toTransactionResponse(TransactionItem transactionItem){
        return new TransactionResponse(transactionItem.getProduct().getProductName(), transactionItem.getQuantity(), transactionItem.getPrice(), transactionItem.getSubtotal());
    }

    public TransactionResponseDto toTransactionResponseDto(Transaction transaction){
        String usersName =  transaction.getCreatedBy().getFirstName() + " " + transaction.getCreatedBy().getLastName();

        List<TransactionResponse> transactionResponses = transaction.getTransactionItems()
                .stream()
                .map(this :: toTransactionResponse)
                .toList();

        return new TransactionResponseDto(transaction.getId(), usersName, transactionResponses, transaction.getTotalAmount(), transaction.getPaymentMethod(), transaction.getMpesaReference(), transaction.getStatus());
    }
}
