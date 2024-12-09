package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.Admin;

import com.market.backend.models.Client;
import com.market.backend.models.ShippingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminInfoDTO {
    // Fields from Account
    private Long accountId;
    private String email;
    private String password;
    private boolean isActive;
    private String type;
    private String username;

    // Fields from admin
    private String firstName;
    private String lastName;

    //fields from ShippingInfo
    private String address;
    private String phone;

    public AdminInfoDTO(Account account, Admin admin, ShippingInfo shippingInfo) {
        if (account!=null) {
            this.accountId = account.getId();
            this.email = account.getEmail();
            this.password = account.getPassword();
            this.isActive = account.isActive();
            this.type = account.getType();
            this.username = account.getUsername();
        }
        if (admin!=null) {
            this.firstName = admin.getFirstName();
            this.lastName = admin.getLastName();
        }
        if (shippingInfo!=null) {
            this.address = shippingInfo.getAddress();
            this.phone = shippingInfo.getPhone();
        }
    }
}
