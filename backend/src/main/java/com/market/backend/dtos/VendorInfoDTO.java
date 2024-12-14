package com.market.backend.dtos;

import com.market.backend.models.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorInfoDTO {
    // Fields from Account
    private Long accountId;
    private String password;
    private boolean isActive;
    private String type;
    private String username;
    private String authType;

    // Fields from vendor
    private String organizationName;
    private Long taxNumber;

    //fields from ShippingInfo
    private String address;
    private String phone;

    public VendorInfoDTO(Account account, Password password, Vendor vendor, ShippingInfo shippingInfo) {
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
        if (vendor!=null) {
            this.organizationName = vendor.getOrganizationName();
            this.taxNumber = vendor.getTaxNumber();
        }
        if (shippingInfo!=null) {
            this.address = shippingInfo.getAddress();
            this.phone = shippingInfo.getPhoneNumber();
        }
    }
}


