package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TransactionDTO(
        @NotNull(message = "Item List is required")
        @Size(min=1, message = "At least one item is required")
        List<TransactionItemRequest> items,
        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,
        @Pattern(regexp = "^(07|01)\\d{8}$", message = "Invalid number format")
        String phoneNumber

) {
}
