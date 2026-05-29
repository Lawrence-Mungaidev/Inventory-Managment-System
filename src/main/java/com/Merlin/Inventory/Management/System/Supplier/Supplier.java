package com.Merlin.Inventory.Management.System.Supplier;

import com.Merlin.Inventory.Management.System.Product.Product;
import com.Merlin.Inventory.Management.System.Stock.Stock;
import com.Merlin.Inventory.Management.System.User.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String supplierName;
    private String contactName;
    @Column(unique = true)
    private String contactNumber;
    private String address;
    private boolean isActive;
    private LocalDateTime createdAt;



    @OneToMany(
            mappedBy = "supplier"
           )
    @JsonManagedReference
    private List<Product> product;

    @OneToMany(
            mappedBy = "supplier"
    )
    @JsonManagedReference
    private List<Stock> stock;

    public Supplier() {}

    public Supplier(String supplierName, String contactName, String contactNumber, String address) {
        this.supplierName = supplierName;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }
}
