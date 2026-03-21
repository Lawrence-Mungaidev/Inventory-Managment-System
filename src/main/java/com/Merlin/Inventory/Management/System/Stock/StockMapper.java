package com.Merlin.Inventory.Management.System.Stock;

import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    public Stock toStock(StockDto dto){
        Stock stock = new Stock();
        stock.setArrivedQuantity(dto.arrivedQuantity());
        stock.setBoughtPrice(dto.boughtPrice());

        return stock;
    }

    public StockDto toStockDto(Stock stock){
        return new StockDto(stock.getProduct().getId(), stock.getArrivedQuantity(),stock.getBoughtPrice(),stock.getSupplier().getId());
    }

    public StockResponseDto toStockResponseDto(Stock stock){
        return  new StockResponseDto(stock.getProduct().getId(), stock.getArrivedQuantity(), stock.getBoughtPrice(),stock.getSupplier().getSupplierName(), stock.getAddedBy().getFirstName(),stock.getApprovedBy().getFirstName(),stock.getApprovalDate(),stock.getStatus());
    }
}
