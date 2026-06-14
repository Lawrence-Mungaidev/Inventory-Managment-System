package com.Merlin.Inventory.Management.System.ChatBot;

import com.Merlin.Inventory.Management.System.Product.ProductService;
import com.Merlin.Inventory.Management.System.Report.ReportService;
import com.Merlin.Inventory.Management.System.Stock.StockService;
import com.Merlin.Inventory.Management.System.StockAdjustment.StockAdjustmentService;
import com.Merlin.Inventory.Management.System.Transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/chatbot")
public class ChatBotController {

    private final ProductService productService;
    private final TransactionService transactionService;
    private final StockService stockService;
    private final StockAdjustmentService stockAdjustmentService;
    private final ReportService reportService;

    @Value("${chatbot.api.key}")
    private String chatbotApiKey;

    @GetMapping("/context")
    private ResponseEntity<?> getContext(
            @RequestHeader("X-Chatbot-Key") String key,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (!key.equals(chatbotApiKey)) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        Map<String, Object> context = new HashMap<>();

        context.put("products", productService.findAll());
        context.put("Transactions", transactionService.getAllTransactions(page, size));
        context.put("StockRequest", stockService.getAllStocks(page, size));
        context.put("StockAdjustments", stockAdjustmentService.getAllStockAdjustments(page, size));
        context.put("Daily Report", reportService.getDailyReport());
        context.put("Monthly Report", reportService.getMonthlyReport());


        return ResponseEntity.ok(context);

    }



}
