package com.market.backend.signup.basicSignUp.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market.backend.signup.basicSignUp.Model.BasicVendor;
import com.market.backend.signup.basicSignUp.Service.VendorService;

@RestController
@CrossOrigin
@RequestMapping("/SignUp")
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
            return "The business name can't be empty";
        if (vendor.getBusinessname().length() > 80)
            return "The business name can't be more than 80 character";
        if (vendor.getPassword() == null)
            return "The password can't be empty";
        if (vendor.getPassword().length() > 80)
            return "The password can't be more than 80 character";
        if (vendor.getTaxnumber() == -1)
            return "The tax number can't be empty";
        if (String.valueOf(vendor.getTaxnumber()).length() != 9){
            System.out.println(String.valueOf(vendor.getTaxnumber()).length());
            return"The tax number must be of 9 numbers only";
        }
        resultMsg = vendorService.insertBasicVendor(vendor);
        System.out.println(resultMsg);
        return resultMsg;
    }
}
