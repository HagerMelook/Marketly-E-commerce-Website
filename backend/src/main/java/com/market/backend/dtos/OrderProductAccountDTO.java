package com.market.backend.dtos;

import com.market.backend.models.OrderProducts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductAccountDTO {
    private String productName;
    private Long accountId;
    private String username;
    private LocalDateTime date;
    private String status;

    OrderProductAccountDTO (OrderProducts orderProducts, Long accountId, String username) {
        this.productName = orderProducts.getProduct().getName();
        this.accountId = accountId;
        this.username = username;
        this.date = orderProducts.getOrder().getDate();
        this.status = orderProducts.getOrder().getStatus();
    }
}

