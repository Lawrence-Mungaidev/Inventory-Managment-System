package com.Merlin.Inventory.Management.System.Mpesa;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/mpesa")
@RequiredArgsConstructor
public class MpesaController {

    private final MpesaService mpesaService;

    @PostMapping("/callback")
    public ResponseEntity<Void> handleCallBack(@RequestBody Map<String, Object> callbackData){
        mpesaService.handleCallBack(callbackData);
        return ResponseEntity.ok().build();
    }
}
