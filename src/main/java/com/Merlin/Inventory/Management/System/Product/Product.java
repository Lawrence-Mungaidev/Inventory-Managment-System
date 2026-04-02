package com.Merlin.Inventory.Management.System.Product;

import com.Merlin.Inventory.Management.System.Category.Category;
import com.Merlin.Inventory.Management.System.Stock.Stock;
import com.Merlin.Inventory.Management.System.StockAdjustment.StockAdjustment;
import com.Merlin.Inventory.Management.System.Supplier.Supplier;
import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String description;
    private Double currentStock;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;
    private double minimumQuantity;
    private boolean countable;
    private boolean isActive;
    @ManyToOne
    @JoinColumn(
            name = "supplier_Id"
    )
    @JsonBackReference
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(
            name = "category_id"
    )
    @JsonBackReference
    private Category category;

    @OneToMany(
            mappedBy = "product"
    )
    @JsonManagedReference
    private List<TransactionItem>  transactionItem;

    @OneToMany(
            mappedBy = "product"
    )
    @JsonManagedReference
    private List<Stock> stock;

    @OneToMany(
            mappedBy = "product"
    )
    @JsonManagedReference
    private List<StockAdjustment> stockAdjustment;

   protected Product(){

    }

    public Product(String productName, String description,BigDecimal sellingPrice, double minimumQuantity, boolean countable ,Supplier supplier, Category category) {
        this.productName = productName;
        this.description = description;
        this.buyingPrice = BigDecimal.ZERO;
        this.currentStock = 0.0;
        this.sellingPrice = sellingPrice;
        this.minimumQuantity = minimumQuantity;
        this.countable = countable;
        this.supplier = supplier;
        this.category = category;
        this.isActive = true;
    }

}
