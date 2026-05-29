package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TransactionResponseDto(
        Long Id,
        Long transactionId,
        String receiptNumber,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime  createdAt,
        String cashierName,
        List<TransactionResponse> items,
        BigDecimal totalAmount,
        BigDecimal balance,
        PaymentMethod paymentMethod

) {
}
