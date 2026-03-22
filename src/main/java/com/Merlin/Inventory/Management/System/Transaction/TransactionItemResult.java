package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;

import java.math.BigDecimal;
import java.util.List;

public record TransactionItemResult(
        List<TransactionItem> transactionItemList,
        BigDecimal totalAmount
) {
}
