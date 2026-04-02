package com.Merlin.Inventory.Management.System.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DateRangeReportDto(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalSales,
        int numberOfTransactions,
        List<ItemsSold> topSellingItems,
        BigDecimal profit,
        BigDecimal loss
) {
}
