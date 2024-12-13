package com.market.backend.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class OrderProductsId implements Serializable {
    private Long orderId;
    private Long productId;
}
