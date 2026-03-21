package com.Merlin.Inventory.Management.System.Category;

import com.Merlin.Inventory.Management.System.Product.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryName;

    @OneToMany(
            mappedBy = "category"
    )
    @JsonManagedReference
    private List<Product> products;

   public Category(){}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
