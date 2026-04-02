package com.Merlin.Inventory.Management.System.User;

import com.Merlin.Inventory.Management.System.Notification.Notification;
import com.Merlin.Inventory.Management.System.Stock.Stock;
import com.Merlin.Inventory.Management.System.StockAdjustment.StockAdjustment;
import com.Merlin.Inventory.Management.System.Transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private ROLE role;
    private boolean mustChangePassword = false;

    @OneToMany(
            mappedBy = "createdBy")
    @JsonManagedReference
    private List<Transaction> transaction;


    private LocalDate createdAt;

    @Column(nullable = false)
    boolean isActive;

    @OneToMany(
            mappedBy = "addedBy"
    )
    @JsonManagedReference
    private List<Stock> addedByStocks;

    @OneToMany(
            mappedBy = "approvedBy"
    )
    @JsonManagedReference
    private List<Stock> approvedByStocks;

    @OneToMany(
            mappedBy = "reportedBy"
    )
    @JsonManagedReference
    private List<StockAdjustment> reportedByStockAdjustments;

    @OneToMany(
            mappedBy = "approvedBy"
    )
    @JsonManagedReference
    private List<StockAdjustment> approvedByStockAdjustments;

    @OneToMany(
            mappedBy = "receiver"
    )
    @JsonManagedReference
    private List<Notification> notifications;



    public User(){

    }
    public User(String firstName, String lastName, String email, String password, String phoneNumber, ROLE role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+ role.name()));
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
