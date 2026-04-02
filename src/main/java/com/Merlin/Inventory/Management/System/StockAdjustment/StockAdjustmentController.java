package com.Merlin.Inventory.Management.System.StockAdjustment;

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
@RequestMapping("/api/stockAdjustments")
@RequiredArgsConstructor
public class StockAdjustmentController {

    private final StockAdjustmentService stockAdjustmentService;

    @PostMapping("/adjustRequest")
    public ResponseEntity<StockAdjustmentResponse> adjustRequest(@Valid @RequestBody StockAdjustmentDto dto, @AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(stockAdjustmentService.adjustmentRequest(dto,authenticatedUser));
    }

    @PatchMapping("/approve/{adjustmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveStockAdjust(@PathVariable("adjustmentId") Long adjustmentId, @AuthenticationPrincipal User authenticatedUser){
        stockAdjustmentService.approveStockAdjustment(adjustmentId,authenticatedUser);
       return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reject/{adjustmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectStockAdjust(@PathVariable("adjustmentId") Long adjustmentId, @AuthenticationPrincipal User authenticatedUser){
        stockAdjustmentService.rejectStockAdjustment(adjustmentId,authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockAdjustmentResponse>> getAllStockAdjustments()
    {
        return ResponseEntity.status(HttpStatus.OK).body(stockAdjustmentService.getAllStockAdjustments());
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockAdjustmentResponse>> getStocksByProductId(@PathVariable Long productId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(stockAdjustmentService.getAllStockAdjustmentsByProduct(productId));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockAdjustmentResponse>> getStockByStatus(@PathVariable Status status){
        return ResponseEntity.status(HttpStatus.OK).body(stockAdjustmentService.getAllStockAdjustmentsByStatus(status));
    }

}
