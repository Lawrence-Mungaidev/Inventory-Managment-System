package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;

import java.math.BigDecimal;
import java.util.List;

public record TransactionResponseDto(
        Long transactionId,
        String cashierName,
        List<TransactionResponse> items,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        String mpesaMessage,
        Status status
) {
}
