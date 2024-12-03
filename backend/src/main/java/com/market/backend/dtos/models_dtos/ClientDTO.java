package com.market.backend.dtos.models_dtos;

import com.market.backend.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Account account;
}
