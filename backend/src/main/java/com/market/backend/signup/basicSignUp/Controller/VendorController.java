package com.market.backend.signup.basicSignUp.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.market.backend.signup.basicSignUp.Model.BasicVendor;
import com.market.backend.signup.basicSignUp.Service.VendorService;

public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // Basic SignUp for Vendor
    @PostMapping("/VendorBasicSignUp")
    public String vendorBasicSignUp(@RequestBody BasicVendor vendor) {
        System.out.println("Received User: " + vendor.getBusinessname() + ", " + vendor.getPassword() + ", "
                + vendor.getTaxnumber());
        String resultMsg;
        if (vendor.getBusinessname() == null)
            resultMsg = "The business name can't be empty";
        if (vendor.getBusinessname().length() > 80)
            resultMsg = "The business name can't be more than 80 character";
        if (vendor.getPassword() == null)
            resultMsg = "The password can't be empty";
        if (vendor.getPassword().length() > 80)
            resultMsg = "The password can't be more than 80 character";
        if (String.valueOf(vendor.getTaxnumber()).length() == 0)
            resultMsg = "The tax number can't be empty";
        if (String.valueOf(vendor.getTaxnumber()).length() != 9)
            resultMsg = "The tax number must be of 9 numbers only";
        resultMsg = vendorService.insertBasicVendor(vendor);
        System.out.println(resultMsg);
        return resultMsg;
    }
}
