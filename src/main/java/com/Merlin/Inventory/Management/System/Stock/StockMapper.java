package com.Merlin.Inventory.Management.System.Stock;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StockMapper {

    public Stock toStock(StockDto dto){
        Stock stock = new Stock();
        stock.setArrivedQuantity(dto.arrivedQuantity());
        stock.setTotalAmount(dto.buyingPrice());
        stock.setTotalAmount(dto.buyingPrice().multiply(BigDecimal.valueOf(dto.arrivedQuantity())));

        return stock;
    }

    public StockDto toStockDto(Stock stock){
        return new StockDto(stock.getProduct().getId(), stock.getArrivedQuantity(),stock.getTotalAmount(),stock.getSupplier().getId());
    }

    public StockResponseDto toStockResponseDto(Stock stock){
        return  new StockResponseDto(stock.getProduct().getId(), stock.getArrivedQuantity(), stock.getTotalAmount(),stock.getSupplier().getSupplierName(), stock.getAddedBy().getFirstName(),stock.getApprovedBy().getFirstName(),stock.getApprovalDate(),stock.getStatus());
    }
}
