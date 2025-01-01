package com.market.backend.repositories;

import com.market.backend.models.Product;
import com.market.backend.models.ShoppingCart;
import com.market.backend.models.ShoppingCartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, Long> {
    Optional<ShoppingCartProduct> findByShoppingCartAndProduct(ShoppingCart cart, Product product);

    void deleteByShoppingCartAndProductId(ShoppingCart cart, Long productId);

    List<ShoppingCartProduct> findByShoppingCart(ShoppingCart cart);
}

