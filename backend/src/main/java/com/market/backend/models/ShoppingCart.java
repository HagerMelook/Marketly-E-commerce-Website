package com.market.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;

    @Column(name = "total_cost", nullable = false)
    private double totalCost;
}
