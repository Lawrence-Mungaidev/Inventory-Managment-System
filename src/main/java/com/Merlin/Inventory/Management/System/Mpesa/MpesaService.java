package com.Merlin.Inventory.Management.System.Mpesa;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDate;
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

}
