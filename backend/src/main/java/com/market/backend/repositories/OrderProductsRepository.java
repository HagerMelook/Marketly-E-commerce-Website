package com.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.market.backend.models.OrderProducts;
import com.market.backend.models.OrderProductsId;

public interface OrderProductsRepository extends JpaRepository<OrderProducts,OrderProductsId>{
    
}
