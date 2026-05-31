package com.Merlin.Inventory.Management.System.Stock;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class StockMapper {

    public Stock toStock(StockDto dto){
        Stock stock = new Stock();
        stock.setArrivedQuantity(dto.arrivedQuantity());
        stock.setTotalAmount(dto.buyingPrice().multiply(BigDecimal.valueOf(dto.arrivedQuantity())));
        stock.setExpiryDate(dto.expiryDate());
        stock.setArrivalDate(LocalDate.now());

        return stock;
    }

    public StockDto toStockDto(Stock stock){
        return new StockDto(stock.getProduct().getId(), stock.getArrivedQuantity(),stock.getTotalAmount(),stock.getSupplier().getId(),stock.getExpiryDate());
    }

    public StockResponseDto toStockResponseDto(Stock stock){
        return  new StockResponseDto(stock.getId(), stock.getProduct().getProductName(), stock.getArrivedQuantity(), stock.getTotalAmount(),stock.getSupplier().getSupplierName(), stock.getAddedBy().getFirstName(),stock.getApprovedBy().getFirstName(),stock.getApprovalDate(),stock.getStatus());
    }
}
