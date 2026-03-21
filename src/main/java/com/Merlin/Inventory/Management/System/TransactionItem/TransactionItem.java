package com.Merlin.Inventory.Management.System.TransactionItem;

import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class TransactionItem {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(
            name = "product_id"
    )
    @JsonBackReference
    private Product product;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    @ManyToOne
    @JoinColumn(
            name = "transaction_Id"
    )
    @JsonBackReference
    private Transaction transaction;

    protected TransactionItem(){ }

    public TransactionItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.subtotal = product.getSellingPrice()
                .multiply(BigDecimal.valueOf(quantity));
    }
}
