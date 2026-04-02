package com.Merlin.Inventory.Management.System.Stock;

import com.Merlin.Inventory.Management.System.User.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    @PostMapping("/create")
    public ResponseEntity<StockDto> createStock(@Valid @RequestBody StockDto stockDto, @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.createStock(stockDto, authenticatedUser));
    }

    @PatchMapping("/approve/{stockId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveStock(@PathVariable("stockId") Long stockId, @AuthenticationPrincipal User authenticatedUser) {
        stockService.approveStock(stockId, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reject/{stockId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectStock(@PathVariable("stockId") Long stockId, @AuthenticationPrincipal User authenticatedUser) {
        stockService.rejectStock(stockId, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponseDto>> getAllStocks() {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.getAllStocks());
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponseDto>> getAllStocksByProduct(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.getAllStocksByProduct(productId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponseDto>> getAllStocksByStatus(@PathVariable Status status) {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.getStockByStatus(status));
    }

    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponseDto>> getAllStocksBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.status(HttpStatus.OK).body(stockService.getAllStocksBySupplier(supplierId));
    }
}
