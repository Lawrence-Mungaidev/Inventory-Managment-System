package com.Merlin.Inventory.Management.System.Mpesa;


import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Notification.NotificationService;
import com.Merlin.Inventory.Management.System.Notification.NotificationType;
import com.Merlin.Inventory.Management.System.Transaction.Status;
import com.Merlin.Inventory.Management.System.Transaction.Transaction;
import com.Merlin.Inventory.Management.System.Transaction.TransactionRepository;
import com.Merlin.Inventory.Management.System.Transaction.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Service
public class MpesaService {

    private final WebClient webClient;

    @Value("${mpesa.consumer-key}")
    String consumerKey;

    @Value("${mpesa.consumer-secret}")
    String consumerSecret;

    @Value("${mpesa.short-code}")
    String shortCode;

    @Value("${mpesa.passkey}")
    String passkey;

    @Value("${mpesa.callback-url}")
    String callbackUrl;

    private MpesaService(WebClient webClient) {
        this.webClient = webClient;
    }
    private TransactionRepository transactionRepository;
    private TransactionService transactionService;
    private NotificationService notificationService;

    private String getAccessToken(){
        String credentials = consumerKey + ":" + consumerSecret;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());

        return webClient.get()
                .uri("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .header("Authorization", "Basic " + encoded)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> response.get("access_token").asText())
                .block();
    }

    public String initialiseSTKPush(String phoneNumber, BigDecimal amount){
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String password = Base64.getEncoder().encodeToString((shortCode + passkey + timeStamp).getBytes());

        Map<String,Object> request = new HashMap<>();
        request.put("BusinessShortCode", shortCode);
        request.put("Password", password);
        request.put("Timestamp", timeStamp);
        request.put("TransactionType", "CustomerPayBillOnline");
        request.put("Amount", amount);
        request.put("PartyA", phoneNumber);
        request.put("PartyB", shortCode);
        request.put("PhoneNumber", phoneNumber);
        request.put("CallbackUrl", callbackUrl);
        request.put("AccountReference","Inventory Management System");
        request.put("TransactionDesc", "payment");

        return webClient.post()
                .uri("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                .header("Authorization", "Bearer " + getAccessToken())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> response.get("CheckoutRequestID").asText())
                .block();
    }

    public void handleCallBack(JsonNode callbackData){
        String checkoutRequestId = callbackData
                .path("Body")
                .path("stkCallback")
                .path("CheckoutRequestID")
                .asText();

        int resultCode = callbackData
                .path("Body")
                .path("stkCallback")
                .path("ResultCode")
                .asInt();

        Transaction transaction = transactionRepository.findByMpesaReference(checkoutRequestId)
                .orElseThrow(()-> new ResourceNotFoundException("Checkout request wasn't found. Please try again"));


        if(resultCode != 0){
            transaction.setStatus(Status.CANCELLED);
            transactionRepository.save(transaction);
            String message = "Transaction Failed for " + transaction.getPhoneNumber() + " request the customer to try again";
            notificationService.createNotification(transaction.getCreatedBy(), message, NotificationType.TRANSACTION_FAILED);
            return;
        }

        transaction.setStatus(Status.COMPLETED);
        transactionService.deductStock(transaction.getTransactionItems());
        transactionRepository.save(transaction);

    }

}
