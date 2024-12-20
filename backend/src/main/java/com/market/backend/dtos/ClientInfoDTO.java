package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.Client;

import com.market.backend.models.Password;
import com.market.backend.models.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoDTO {
    // Fields from Account
    private Long accountId;
    private String password;
    private boolean isActive;
    private String type;
    private String username;
    private String authType;

    // Fields from Client
    private String firstName;
    private String lastName;

    //fields from ShippingInfo
    private String address;
    private String phone;
    private String postalCode;

    public ClientInfoDTO(Account account, Password password, Client client, ShippingInfo shippingInfo) {
        if (account!=null) {
            this.accountId = account.getId();
            this.isActive = account.isActive();
            this.type = account.getType();
            this.username = account.getUsername();
            this.authType = account.getAuthType();
        }
        if (password!=null) {
            this.password = password.getAccountPassword();
        }
        if (client!=null) {
            this.firstName = client.getFirstName();
            this.lastName = client.getLastName();
        }
        if (shippingInfo!=null) {
            this.address = shippingInfo.getAddress();
            this.phone = shippingInfo.getPhoneNumber();
            this.postalCode = shippingInfo.getPostalCode();
        }
    }
}


