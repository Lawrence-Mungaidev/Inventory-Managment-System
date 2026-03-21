package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toTransaction(TransactionDTO dto){
        Transaction transaction = new Transaction();
        transaction.setTransactionItems(dto.items());
        transaction.setPaymentMethod(dto.paymentMethod());
        transaction.setPhoneNumber(dto.phoneNumber());

        return transaction;
    }

    public TransactionResponse toTransactionResponse(TransactionItem transactionItem){
        return new TransactionResponse(transactionItem.getProduct().getProductName(),transactionItem.getQuantity(),transactionItem.getPrice(),transactionItem.getSubtotal());
    }


}
