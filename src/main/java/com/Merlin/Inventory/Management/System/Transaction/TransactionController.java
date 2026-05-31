package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.User.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/Transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionDTO dto, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(dto, authenticatedUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size));
    }


    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long transactionId){
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/today")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactionsToday(){
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTodayTransactions());
    }

    @GetMapping("/range")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactionsRange(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.getTransactionByDateRange(start.atStartOfDay(), end.atTime(23, 59, 59)));
    }
}
