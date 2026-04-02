package com.Merlin.Inventory.Management.System.Stock;

import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Supplier.Supplier;
import com.Merlin.Inventory.Management.System.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    @JsonBackReference
    private Product product;
    private double arrivedQuantity;
    private BigDecimal buyingPrice;
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(
            name = "supplier_id"
    )
    @JsonBackReference
    private Supplier supplier;
    private LocalDate arrivalDate;


    @ManyToOne
    @JoinColumn(
            name = "addedBy_id"
    )
    @JsonBackReference
    private User addedBy;

    @ManyToOne
    @JoinColumn(
            name = "approvedBy_id"
    )
    @JsonBackReference
    private User approvedBy;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate approvalDate;

   protected Stock(){}

    public Stock(Product product, double arrivedQuantity, BigDecimal buyingPrice, Supplier supplier) {
        this.product = product;
        this.arrivedQuantity = arrivedQuantity;
        this.buyingPrice = buyingPrice;
        this.supplier = supplier;
        this.arrivalDate = LocalDate.now();
        this.status = Status.PENDING;
    }
}
