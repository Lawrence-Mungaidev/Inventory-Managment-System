package com.Merlin.Inventory.Management.System.Report;

import java.math.BigDecimal;
import java.util.List;

public record MonthlyReportDto(
        String month,
        BigDecimal totalSales,
        int numberOfTransactions,
        List<ItemsSold> mostSoldItem,
        BigDecimal profit,
        BigDecimal loss
) {
}
