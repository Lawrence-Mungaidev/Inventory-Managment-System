package com.Merlin.Inventory.Management.System.ChatBot;

import com.Merlin.Inventory.Management.System.Product.ProductService;
import com.Merlin.Inventory.Management.System.Report.ReportService;
import com.Merlin.Inventory.Management.System.Stock.StockService;
import com.Merlin.Inventory.Management.System.StockAdjustment.StockAdjustmentService;
import com.Merlin.Inventory.Management.System.Transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

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
    public ResponseEntity<?> getContext(
            @RequestParam String message,
             @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            String n8nUrl = "https://automations.xtremelemiso.site/webhook-test/b1a62c65-ef52-4efb-8200-6d34fbe3dc2a";

            Map<String, Object> inventoryContext = new HashMap<>();
            inventoryContext.put("products", productService.findAll());
            inventoryContext.put("transactions", transactionService.getAllTransactions(page, size));
            inventoryContext.put("dailyReport", reportService.getDailyReport());
            inventoryContext.put("stock request", stockService.getAllStocks(page, size));
            inventoryContext.put("Stock Adjustments", stockAdjustmentService.getAllStockAdjustments(page, size));
            inventoryContext.put("monthlyReport", reportService.getMonthlyReport());

            Map<String, Object> body = new HashMap<>();
            body.put("message", message);
            body.put("sessionId", "quick-save-session");
            body.put("context", inventoryContext);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Chatbot-Key", chatbotApiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> n8nResponse = restTemplate.exchange(n8nUrl, HttpMethod.POST, request, String.class);
            System.out.println("n8n raw response: " + n8nResponse.getBody());
            Map<String, String> result = new HashMap<>();
            result.put("reply", n8nResponse.getBody());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("reply", "Sorry, the assistant is unavailable right now.");
            return ResponseEntity.ok(error);
        }
    }



}
