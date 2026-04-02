package com.Merlin.Inventory.Management.System.Stock;


import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Notification.NotificationService;
import com.Merlin.Inventory.Management.System.Notification.NotificationType;
import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Product.ProductRepository;
import com.Merlin.Inventory.Management.System.Supplier.Supplier;
import com.Merlin.Inventory.Management.System.Supplier.SupplierRepository;
import com.Merlin.Inventory.Management.System.User.ROLE;
import com.Merlin.Inventory.Management.System.User.User;
import com.Merlin.Inventory.Management.System.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private  final UserRepository userRepository;
    private final NotificationService notificationService;

    public StockDto createStock(StockDto dto, User authenticatedUser) {
        Product product = productRepository.findById(dto.productId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Supplier supplier = supplierRepository.findById(dto.supplierId())
                        .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));


        Stock stock = stockMapper.toStock(dto);

        stock.setProduct(product);
        stock.setSupplier(supplier);

        stock.setAddedBy(authenticatedUser);

        Stock savedStock;

        if (authenticatedUser.getRole().equals(ROLE.ADMIN)) {
            stock.setStatus(Status.APPROVED);
            stock.setApprovedBy(authenticatedUser);
            stock.setApprovalDate(LocalDate.now());
            product.setCurrentStock(product.getCurrentStock() + dto.arrivedQuantity());
            product.setBuyingPrice(dto.buyingPrice());
            productRepository.save(product);

            savedStock = stockRepository.save(stock);

        }else {
            stock.setStatus(Status.PENDING);
            savedStock = stockRepository.save(stock);

            User admin = userRepository.findByRole(ROLE.ADMIN)
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

           String message ="Requesting to update the new stock arrival on " + stock.getArrivalDate();

           notificationService.createNotification(admin,message,NotificationType.RESTOCK_REQUEST);
        }

        return stockMapper.toStockDto(savedStock);

    }

    public void approveStock(Long stockId, User authenticatedUser) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found"));

        Product product = stock.getProduct();

        if (stock.getStatus().equals(Status.PENDING)) {
            stock.setStatus(Status.APPROVED);
            stock.setApprovedBy(authenticatedUser);
            stock.setApprovalDate(LocalDate.now());
            product.setCurrentStock(product.getCurrentStock() + stock.getArrivedQuantity());
            product.setBuyingPrice(stock.getBuyingPrice());
            productRepository.save(product);

            String approvedMessage="Your Stock request of " + stock.getProduct().getProductName() + " on " + stock.getArrivalDate()  + " is approved";

            notificationService.createNotification(stock.getAddedBy(), approvedMessage,NotificationType.STOCK_APPROVED);

            if (product.getCurrentStock() < product.getMinimumQuantity()) {
                String lowStock = "Product: " + product.getProductName() +  " is still below the minimum quantity";
                notificationService.createNotification(authenticatedUser,"Stock is still low" , NotificationType.LOW_STOCK);
            }

        }
        stockRepository.save(stock);
    }

    public void rejectStock(Long stockId, User authenticatedUser) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found"));

        String message= "The stock request of "+ stock.getProduct().getProductName() +" made on "+ stock.getArrivalDate() +" was Rejected, please confirm the restock and make another request";

        if (stock.getStatus().equals(Status.PENDING)) {
            stock.setStatus(Status.REJECTED);
            notificationService.createNotification(stock.getAddedBy(), message, NotificationType.STOCK_REJECTED);
            stock.setApprovalDate(LocalDate.now());
            stock.setApprovedBy(authenticatedUser);
        }

        stockRepository.save(stock);
    }

    public List<StockResponseDto> getAllStocks(){
        return stockRepository.findAll()
                .stream()
                .map(stockMapper :: toStockResponseDto)
                .toList();
    }

    public List<StockResponseDto> getStockByStatus(Status status){
        return stockRepository.findAllByStatus(status)
                .stream()
                .map(stockMapper :: toStockResponseDto)
                .toList();
    }

    public List<StockResponseDto> getAllStocksByProduct(Long productId){
        return stockRepository.findByProductId(productId)
                .stream()
                .map(stockMapper ::toStockResponseDto)
                .toList();
    }

    public List<StockResponseDto> getAllStocksBySupplier(Long supplierId){
        return stockRepository.findBySupplierId(supplierId)
                .stream()
                .map(stockMapper :: toStockResponseDto)
                .toList();
    }
}
