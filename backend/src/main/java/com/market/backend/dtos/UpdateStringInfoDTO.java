package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.Client;

import com.market.backend.models.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateStringInfoDTO {
    private String newData;

}
