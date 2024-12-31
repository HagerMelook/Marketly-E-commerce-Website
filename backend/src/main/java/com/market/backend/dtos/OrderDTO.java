package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.Product;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private Account account;
    private String status;
    private Date date;
    private double checkoutPrice;
    private List<Product> products;

}
