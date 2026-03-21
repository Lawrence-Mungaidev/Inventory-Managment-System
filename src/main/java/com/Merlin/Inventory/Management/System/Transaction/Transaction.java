package com.Merlin.Inventory.Management.System.Transaction;

import com.Merlin.Inventory.Management.System.TransactionItem.TransactionItem;
import com.Merlin.Inventory.Management.System.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String phoneNumber;
    private String mpesaReference;
    private LocalDateTime transactionDate;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    @JsonBackReference
    private User user;

    @OneToMany(
            mappedBy = "transaction",
            cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<TransactionItem> transactionItems;

    protected Transaction() {}

    public Transaction(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.transactionDate = LocalDateTime.now();
    }
}
