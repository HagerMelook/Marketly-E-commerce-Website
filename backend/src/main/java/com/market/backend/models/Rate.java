package com.market.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;

    @Column(name = "rating", nullable = false)
    private double rating;
}
