package com.Merlin.Inventory.Management.System.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DailyReportDto(
        LocalDate date,
        BigDecimal totalSales,
        int numberOfTransactions,
        List<ItemsSold> itemsSoldList
) {
}
