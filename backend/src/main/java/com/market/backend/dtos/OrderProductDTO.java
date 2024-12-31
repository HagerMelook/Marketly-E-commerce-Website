package com.market.backend.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProductDTO {
    private Long id;
    private String name;
    private Double price;
}
