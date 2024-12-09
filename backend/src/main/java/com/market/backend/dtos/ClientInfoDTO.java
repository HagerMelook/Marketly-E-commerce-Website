package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.Client;

import com.market.backend.models.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientInfoDTO {
    // Fields from Account
    private Long accountId;
    private String email;
    private String password;
    private boolean isActive;
    private String type;
    private String username;

    // Fields from Client
    private String firstName;
    private String lastName;

    //fields from ShippingInfo
    private String address;
    private String phone;

    public ClientInfoDTO(Account account, Client client, ShippingInfo shippingInfo) {
        if (account!=null) {
            this.accountId = account.getId();
            this.email = account.getEmail();
            this.password = account.getPassword();
            this.isActive = account.isActive();
            this.type = account.getType();
            this.username = account.getUsername();
        }
        if (client!=null) {
            this.firstName = client.getFirstName();
            this.lastName = client.getLastName();
        }
        if (shippingInfo!=null) {
            this.address = shippingInfo.getAddress();
            this.phone = shippingInfo.getPhone();
        }
    }
}


