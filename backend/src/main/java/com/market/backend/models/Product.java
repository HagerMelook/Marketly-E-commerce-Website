package com.market.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "product_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "account_id", nullable = false)
    private Vendor vendor;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_description", nullable = false)
    private String description;

    @Column(name = "product_price", nullable = false)
    private double price;

    @Column(name = "product_rating", nullable = false)
    private double rating;

    @Column(name = "product_quantity", nullable = false)
    private int quantity;

    @Column(name = "product_category", nullable = false)
    private String category;
}
