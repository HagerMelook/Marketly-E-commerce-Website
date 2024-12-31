package com.market.backend.dtos;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {
    private Long account_id;
    private int checkoutPrice;
    private List<OrderProductDTO> order_products;
}
