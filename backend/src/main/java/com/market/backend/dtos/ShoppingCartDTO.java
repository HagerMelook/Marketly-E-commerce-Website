package com.market.backend.dtos;

import com.market.backend.models.ShoppingCartProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO {
    private List<ShoppingCartProductDTO> products;
    private double totalPrice;
}
