package com.market.backend.dtos;

import com.market.backend.models.Product;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ShoppingCartProductDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
    private double price;
}
