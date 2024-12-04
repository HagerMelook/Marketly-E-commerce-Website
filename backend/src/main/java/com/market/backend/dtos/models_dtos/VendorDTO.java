package com.market.backend.dtos.models_dtos;

import com.market.backend.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorDTO {
    private Long id;
    private String organisationName;
    private String taxNumber;
    private Account account;
}
