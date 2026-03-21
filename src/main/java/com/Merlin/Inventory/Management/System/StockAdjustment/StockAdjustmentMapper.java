package com.Merlin.Inventory.Management.System.StockAdjustment;

import org.springframework.stereotype.Component;

@Component
public class StockAdjustmentMapper {

    public StockAdjustment toStockAdjustment(StockAdjustmentDto dto) {
        StockAdjustment stockAdjustment = new StockAdjustment();
        stockAdjustment.setQuantity(dto.quantity());
        stockAdjustment.setAdjustmentType(dto.adjustmentType());
        stockAdjustment.setReason(dto.reason());

        return stockAdjustment;
    }

    public StockAdjustmentResponse toStockAdjustmentResponse(StockAdjustment  stockAdjustment) {
        return new StockAdjustmentResponse(stockAdjustment.getProduct().getProductName() , stockAdjustment.getQuantity(), stockAdjustment.getAdjustmentType(), stockAdjustment.getReason());
    }
}
