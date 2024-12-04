package com.market.backend.dtos.models_dtos;

import com.market.backend.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Account account;
}
