package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;

import java.util.List;

public record TransactionDTO(
        List<TransactionItem> items,
        PaymentMethod paymentMethod,
        String phoneNumber

) {
}
