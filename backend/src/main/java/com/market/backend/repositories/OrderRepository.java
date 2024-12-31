package com.market.backend.repositories;

import com.market.backend.models.Order;
import com.market.backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT Order FROM Order WHERE Order.account.id :account_id")
    List<Order> findOrderByAccountId(@Param("account_id") Long account_id);
    void deleteById(long id);
}
