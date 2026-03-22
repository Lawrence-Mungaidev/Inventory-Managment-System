package com.Merlin.Inventory.Management.System.Mpesa;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Base64;

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

    public MpesaService(WebClient webClient) {
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

}
