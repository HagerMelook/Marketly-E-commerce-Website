package com.market.backend.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CartProductsId implements Serializable {
    private Long accountId;
    private Long productId;
}
