package com.market.backend.repositories;

import com.market.backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    @Query("SELECT p FROM Product p WHERE p.vendor.id = :vendorId")
    List<Product> findByVendorId(Long vendorId);
    @Query("SELECT p FROM Product p WHERE p.vendor.id = :vendorId AND p.name LIKE %:name%")
    List<Product> findByVendorIdAndNameContaining(Long vendorId, String name);
}
