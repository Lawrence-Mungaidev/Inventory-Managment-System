package com.Merlin.Inventory.Management.System.StockAdjustment;

import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class StockAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    @JsonBackReference
    private Product product;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private AdjustmentType adjustmentType;
    private String reason;
    private LocalDate reportedDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(
            name = "reportedBy_id"
    )
    @JsonBackReference
    private User reportedBy;

    @ManyToOne
    @JoinColumn(
            name = "approvedBy_id"
    )
    private User approvedBy;
    private LocalDate approvalDate;

   protected StockAdjustment(){ }

    public StockAdjustment(Product product, int quantity, AdjustmentType adjustmentType, String reason) {
        this.product = product;
        this.quantity = quantity;
        this.adjustmentType = adjustmentType;
        this.reason = reason;
        this.reportedDate = LocalDate.now();
        this.status = Status.PENDING;
    }
}
