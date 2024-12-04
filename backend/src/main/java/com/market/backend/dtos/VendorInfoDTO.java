package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.ShippingInfo;
import com.market.backend.models.Vendor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VendorInfoDTO {
    private Long accountId;
    private String email;
    private String password;
    private boolean isActive;
    private String type;
    private String username;
    private Long vendorId;
    private String organisationName;
    private String taxNumber;
    private Long shippingId;
    private String address;
    private String phone;

    public VendorInfoDTO(Account account, Vendor vendor, ShippingInfo shippingInfo) {
        if (account!=null) {
            this.accountId = account.getId();
            this.email = account.getEmail();
            this.password = account.getPassword();
            this.isActive = account.isActive();
            this.type = account.getType();
            this.username = account.getUsername();
        }
        if (vendor!=null) {
            this.organisationName = vendor.getOrganisationName();
            this.taxNumber = vendor.getTaxNumber();
        }
        if (shippingInfo!=null) {
            this.address = shippingInfo.getAddress();
            this.phone = shippingInfo.getPhone();
        }
    }
}


