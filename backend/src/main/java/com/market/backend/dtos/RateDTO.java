package com.market.backend.dtos;

import com.market.backend.models.Rate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateDTO {
    private Long id;
    private Long accountId;
    private Long productId;
    private Integer rating;

    public RateDTO(Rate rate) {
        this.id = rate.getId();
        this.rating  = rate.getRating();
        this.accountId = rate.getAccount().getId();
        this.productId = rate.getProduct().getId();
    }
}
