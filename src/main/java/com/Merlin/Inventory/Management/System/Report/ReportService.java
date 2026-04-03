package com.Merlin.Inventory.Management.System.Report;

import com.Merlin.Inventory.Management.System.Stock.StockRepository;
import com.Merlin.Inventory.Management.System.StockAdjustment.StockAdjustment;
import com.Merlin.Inventory.Management.System.StockAdjustment.StockAdjustmentRepository;
import com.Merlin.Inventory.Management.System.Transaction.Status;
import com.Merlin.Inventory.Management.System.Transaction.Transaction;
import com.Merlin.Inventory.Management.System.Transaction.TransactionRepository;
import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final StockRepository stockRepository;
    private final StockAdjustmentRepository adjustmentRepository;

    public DailyReportDto getDailyReport() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23,59,59);
        LocalDate today = LocalDate.now();

        List<Transaction> transactionList = transactionRepository.findByTransactionDateBetween(start, end);

        BigDecimal totalSales = transactionRepository.getTotalSalesByDateBetween(start, end);

        if(totalSales == null) totalSales = BigDecimal.ZERO;

        int numberOfTransactions = transactionRepository.countByTransactionDateBetweenAndStatus(start,end, Status.COMPLETED);
        List<ItemsSold> soldItems = buildItemsSold(transactionList);

        return new DailyReportDto(today, totalSales,numberOfTransactions,soldItems);
    }

    public MonthlyReportDto getMonthlyReport() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23,59,59);

        List<Transaction> transactionList = transactionRepository.findByTransactionDateBetween(startOfMonth, endOfMonth);

        BigDecimal totalSales = transactionRepository.getTotalSalesByDateBetween(startOfMonth, endOfMonth);
        if(totalSales == null) totalSales = BigDecimal.ZERO;
        int numberOfTransactions = transactionRepository.countByTransactionDateBetweenAndStatus(startOfMonth,endOfMonth,Status.COMPLETED);
        List<ItemsSold> soldItems = buildMostSoldItems(transactionList);

        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        BigDecimal totalStockSales = stockRepository.getTotalSalesByApprovalDateBetween(start,end);

        BigDecimal profit = totalSales.subtract(totalStockSales);

        BigDecimal loss = getLoss(start,end);

        return new MonthlyReportDto(LocalDate.now().toString(), totalSales,numberOfTransactions,soldItems,profit,loss);


    }

    private List<ItemsSold> buildMostSoldItems(List<Transaction> transactions) {
        Map<String, Double> mostSoldItems = new HashMap<>();

        for(Transaction transaction : transactions){
            for(TransactionItem item : transaction.getTransactionItems()){
                String productName = item.getProduct().getProductName();
                mostSoldItems.merge(productName, item.getQuantity(), Double::sum);
            }
        }

        return mostSoldItems.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .map(entry -> new ItemsSold(entry.getKey(), entry.getValue()))
                .toList();
    }

    private List<ItemsSold> buildItemsSold(List<Transaction> transactions) {
        List<TransactionItem> transactionItems = new ArrayList<>();

        for(Transaction transaction : transactions){
            transactionItems.addAll(transaction.getTransactionItems());
        }

        return transactionItems
                .stream()
                .sorted((a,b) -> Double.compare(b.getQuantity(), a.getQuantity()))
                .map(items -> new ItemsSold(items.getProduct().getProductName(), items.getQuantity()))
                .toList();
    }

    private BigDecimal getLoss(LocalDate startDate, LocalDate endDate) {
        List<StockAdjustment> stockAdjustments = adjustmentRepository.findAllStockAdjustmentBetweenReportedDate(startDate, endDate);

        BigDecimal loss = BigDecimal.ZERO;

        for(StockAdjustment stockAdjustment : stockAdjustments){
           BigDecimal total = stockAdjustment.getProduct().getBuyingPrice()
                    .multiply(BigDecimal.valueOf(stockAdjustment.getQuantity()));
            loss = loss.add(total);
        }

        return loss;
    }

    public DateRangeReportDto getDateRangeReportDto(LocalDate start, LocalDate end){
        List<Transaction> transactionList = transactionRepository.findByTransactionDateBetween(start.atStartOfDay(), end.atTime(23,59,59));

        BigDecimal totalSales = transactionRepository.getTotalSalesByDateBetween(start.atStartOfDay(), end.atTime(23,59,59));
        if(totalSales == null) totalSales = BigDecimal.ZERO;
        int numberOfTransactions = transactionRepository.countByTransactionDateBetweenAndStatus(start.atStartOfDay(), end.atTime(23,59,59),Status.COMPLETED);

        List<ItemsSold> soldItems = buildMostSoldItems(transactionList);

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        BigDecimal totalStockSales = stockRepository.getTotalSalesByApprovalDateBetween(startOfMonth,endOfMonth);

        BigDecimal profit = totalSales.subtract(totalStockSales);

        BigDecimal loss = getLoss(startOfMonth,endOfMonth);

        return new DateRangeReportDto(startOfMonth,endOfMonth, totalSales,numberOfTransactions,soldItems,profit,loss);

    }


}
