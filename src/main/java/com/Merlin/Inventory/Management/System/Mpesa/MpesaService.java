package com.Merlin.Inventory.Management.System.Mpesa;

import com.Merlin.Inventory.Management.System.Exception.PaymentException;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Notification.NotificationService;
import com.Merlin.Inventory.Management.System.Notification.NotificationType;
import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Product.ProductRepository;
import com.Merlin.Inventory.Management.System.Transaction.Status;
import com.Merlin.Inventory.Management.System.Transaction.Transaction;
import com.Merlin.Inventory.Management.System.Transaction.TransactionRepository;
import com.Merlin.Inventory.Management.System.Transaction.TransactionService;
import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class MpesaService {

    private final WebClient webClient;
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;

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

    @Autowired
    private MpesaService(WebClient webClient, TransactionRepository transactionRepository, ProductRepository productRepository, NotificationService notificationService) {
        this.webClient = webClient;
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }


    private String getAccessToken(){
        String credentials = consumerKey + ":" + consumerSecret;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());

        return webClient.get()
                .uri("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .header("Authorization", "Basic " + encoded)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"))
                .block();
    }

    public String initialiseSTKPush(String phoneNumber, BigDecimal amount){
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String password = Base64.getEncoder().encodeToString((shortCode + passkey + timeStamp).getBytes());
        String formattedPhone = "254" + phoneNumber.substring(1);

        Map<String,Object> request = new HashMap<>();
        request.put("BusinessShortCode", shortCode);
        request.put("Password", password);
        request.put("Timestamp", timeStamp);
        request.put("TransactionType", "CustomerPayBillOnline");
        request.put("Amount", amount.intValue());
        request.put("PartyA", phoneNumber);
        request.put("PartyB", shortCode);
        request.put("PhoneNumber", formattedPhone);
        request.put("CallBackURL", callbackUrl);
        request.put("AccountReference","Inventory Management System");
        request.put("TransactionDesc", "payment");

        return webClient.post()
                .uri("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                .header("Authorization", "Bearer " + getAccessToken())
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response ->
                        response.bodyToMono(String.class)
                                .doOnNext(body-> System.out.println("STK PUSH Error " + body))
                                .then(Mono.error(new PaymentException("STK PUSH FAILED"))))
                .bodyToMono(Map.class)
                .doOnNext(response -> System.out.println("STK Response: " + response))
                .map(response ->(String) response.get("CheckoutRequestID"))
                .block();
    }

    public void handleCallBack(Map<String, Object> callbackData){
        Map<String, Object> body =(Map<String, Object>) callbackData.get("Body");
        Map<String, Object> stkCallback =(Map<String, Object>) body.get("stkCallback");
        String checkoutRequestId = (String) stkCallback.get("CheckoutRequestID");
        int resultCode = (int) stkCallback.get("ResultCode");



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

        String message = "Mpesa Payment of " + transaction.getTotalAmount() + " received from " + transaction.getPhoneNumber() + " for transaction Id " + transaction.getId() + " is a Success";

        notificationService.createNotification(transaction.getCreatedBy(),message, NotificationType.TRANSACTION_SUCCESS);

        for(TransactionItem item : transaction.getTransactionItems())
        {
            Product product = item.getProduct();
            product.setCurrentStock(product.getCurrentStock() - item.getQuantity());
            productRepository.save(product);
        }

        transactionRepository.save(transaction);

    }

}
