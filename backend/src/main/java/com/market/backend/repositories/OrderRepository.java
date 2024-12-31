package com.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.market.backend.models.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{

    
} 