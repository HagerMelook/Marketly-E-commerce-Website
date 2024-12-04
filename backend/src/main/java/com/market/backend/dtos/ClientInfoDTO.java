package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.Client;

import com.market.backend.models.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientInfoDTO {
    private Long accountId;
    private String email;
    private String password;
    private boolean isActive;
    private String type;
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public ClientInfoDTO(Account account, Client client, ShippingInfo shippingInfo) {
        this.accountId = account.getId();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.isActive = account.isActive();
        this.type = account.getType();
        this.username = account.getUsername();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.address = shippingInfo.getAddress();
        this.phone = shippingInfo.getPhone();
    }
}


