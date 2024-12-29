package com.market.backend.dtos;

import com.market.backend.models.Product;
import com.market.backend.models.ShippingInfo;
import com.market.backend.models.Vendor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoDTO {
    //fields from Product
    private Long id;
    private String name;
    private String description;
    private double price;
    private double rating;
    private int quantity;

    //fields from Vendor
    private String organizationName;
    private Long taxNumber;

    //fields from ShippingInfo
    private String address;
    private String phoneNumber;
    private String postalCode;

    public ProductInfoDTO(Product product, ShippingInfo shippingInfo) {
        if(product!=null) {
            this.id = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice();
            this.rating = product.getRating();
            this.quantity = product.getQuantity();
            Vendor vendor = product.getVendor();
            this.organizationName = vendor.getOrganizationName();
            this.taxNumber = vendor.getTaxNumber();
        }

        if (shippingInfo!=null) {
            this.address = shippingInfo.getAddress();
            this.phoneNumber = shippingInfo.getPhoneNumber();
            this.postalCode = shippingInfo.getPostalCode();
        }
    }
}
