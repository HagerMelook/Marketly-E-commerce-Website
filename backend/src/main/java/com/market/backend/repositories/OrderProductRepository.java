package com.market.backend.repositories;

import com.market.backend.models.Order;
import com.market.backend.models.OrderProducts;
import com.market.backend.models.OrderProductsId;
import com.market.backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProducts, OrderProductsId> {
    @Query("SELECT op.product FROM OrderProducts op WHERE op.order.id= :order_id")
    List<Product> findProductByOrderId(@Param("order_id") Long order_id);
}
