package com.Merlin.Inventory.Management.System.StockAdjustment;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StockAdjustmentMapper {

    public StockAdjustment toStockAdjustment(StockAdjustmentDto dto) {
        StockAdjustment stockAdjustment = new StockAdjustment();
        stockAdjustment.setQuantity(dto.quantity());
        stockAdjustment.setAdjustmentType(dto.adjustmentType());
        stockAdjustment.setReason(dto.reason());
        stockAdjustment.setReportedDate(LocalDate.now());

        return stockAdjustment;
    }

    public StockAdjustmentResponse toStockAdjustmentResponse(StockAdjustment  stockAdjustment) {
        return new StockAdjustmentResponse(stockAdjustment.getId(), stockAdjustment.getProduct().getProductName() , stockAdjustment.getQuantity(), stockAdjustment.getAdjustmentType(), stockAdjustment.getReason(),stockAdjustment.getStatus());
    }
}
