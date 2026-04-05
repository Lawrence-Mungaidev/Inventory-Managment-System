package com.Merlin.Inventory.Management.System.StockAdjustment;

import com.Merlin.Inventory.Management.System.Exception.InvalidProductOperationException;
import com.Merlin.Inventory.Management.System.Exception.ResourceNotFoundException;
import com.Merlin.Inventory.Management.System.Notification.NotificationService;
import com.Merlin.Inventory.Management.System.Notification.NotificationType;
import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Product.ProductRepository;
import com.Merlin.Inventory.Management.System.User.ROLE;
import com.Merlin.Inventory.Management.System.User.User;
import com.Merlin.Inventory.Management.System.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockAdjustmentService {

    private final StockAdjustmentRepository stockAdjustmentRepository;
    private final StockAdjustmentMapper stockAdjustmentMapper;
    private final ProductRepository productRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public StockAdjustmentResponse adjustmentRequest(StockAdjustmentDto dto, User authenticatedUser) {
        StockAdjustment stockAdjustment = stockAdjustmentMapper.toStockAdjustment(dto);

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        if(product.isCountable() && dto.quantity() != Math.floor(dto.quantity())){
            throw new InvalidProductOperationException(product.getProductName() + " must be inputted as a whole numbers");
        }

        stockAdjustment.setProduct(product);

        StockAdjustment savedStockAdjustment;

        if (authenticatedUser.getRole().equals(ROLE.ADMIN)){
            stockAdjustment.setReportedBy(authenticatedUser);
            stockAdjustment.setApprovedBy(authenticatedUser);
            stockAdjustment.setApprovalDate(LocalDate.now());
            stockAdjustment.setStatus(Status.APPROVED);
            product.setCurrentStock(product.getCurrentStock() -  stockAdjustment.getQuantity());
            productRepository.save(product);

            savedStockAdjustment = stockAdjustmentRepository.save(stockAdjustment);
        }else {
            stockAdjustment.setReportedBy(authenticatedUser);

            User admin = userRepository.findByRole(ROLE.ADMIN)
                    .orElseThrow(()-> new ResourceNotFoundException("User not found"));

            String message = "Requesting approval for stock adjustment from " + authenticatedUser.getUsername() + "\n For: " + product.getProductName() + "\n Reason: " + dto.adjustmentType() + "\n What Occurred: " + dto.reason();

            notificationService.createNotification(admin, message, NotificationType.STOCK_ADJUSTMENT_REQUEST );

            savedStockAdjustment = stockAdjustmentRepository.save(stockAdjustment);
        }

        return stockAdjustmentMapper.toStockAdjustmentResponse(savedStockAdjustment);
    }


    public void approveStockAdjustment(Long adjustmentId, User authenticatedUser) {
        StockAdjustment stockAdjustment = stockAdjustmentRepository.findById(adjustmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Adjustment not found"));

        stockAdjustment.setStatus(Status.APPROVED);
        stockAdjustment.setApprovedBy(authenticatedUser);
        stockAdjustment.setApprovalDate(LocalDate.now());

        Product product = stockAdjustment.getProduct();

        product.setCurrentStock(product.getCurrentStock() -  stockAdjustment.getQuantity());
        productRepository.save(product);

        if(product.getCurrentStock() < product.getMinimumQuantity()){
            String lowStockMessage = product.getProductName() + " is below the minimum quantity Please Restock";
            notificationService.createNotification(authenticatedUser, lowStockMessage , NotificationType.RESTOCK_REQUEST);
        }

        String message = "Your Stock Adjustment for " + stockAdjustment.getProduct() + " made on " + stockAdjustment.getReportedDate() +" has been approved";

        notificationService.createNotification(stockAdjustment.getReportedBy(),message,NotificationType.STOCK_ADJUSTMENT_APPROVED);

        stockAdjustmentRepository.save(stockAdjustment);
    }

    public void rejectStockAdjustment(Long adjustmentId, User authenticatedUser) {
        StockAdjustment stockAdjustment = stockAdjustmentRepository.findById(adjustmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Adjustment not found"));

        String message = "Your Stock Adjustment for " + stockAdjustment.getProduct() + " made on " + stockAdjustment.getReportedDate() +" has been Rejected";

        if(stockAdjustment.getStatus().equals(Status.PENDING)){
            stockAdjustment.setStatus(Status.REJECTED);
            stockAdjustment.setApprovedBy(authenticatedUser);
            stockAdjustment.setApprovalDate(LocalDate.now());

            notificationService.createNotification(stockAdjustment.getReportedBy(), message ,NotificationType.STOCK_ADJUSTMENT_REJECTED);

            stockAdjustmentRepository.save(stockAdjustment);
        }
    }

    public List<StockAdjustmentResponse> getAllStockAdjustments(){
        return stockAdjustmentRepository.findAll()
                .stream()
                .map(stockAdjustmentMapper :: toStockAdjustmentResponse)
                .toList();
    }

    public List<StockAdjustmentResponse> getAllStockAdjustmentsByProduct(Long productId){
        return stockAdjustmentRepository.findAllStockAdjustmentByProductId(productId)
                .stream()
                .map(stockAdjustmentMapper :: toStockAdjustmentResponse)
                .toList();
    }

    public List<StockAdjustmentResponse> getAllStockAdjustmentsByStatus(Status status){
        return stockAdjustmentRepository.findAllStockAdjustmentByStatus(status)
                .stream()
                .map(stockAdjustmentMapper :: toStockAdjustmentResponse)
                .toList();
    }
}
