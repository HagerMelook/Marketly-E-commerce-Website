package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.Product;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private Account account;
    private String status;
    private LocalDateTime date;
    private double checkoutPrice;
    private List<Product> products;
    private Long account_id;
    private List<OrderProductDTO> order_products;

}
