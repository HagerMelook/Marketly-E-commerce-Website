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
        resultMsg = vendorService.insertBasicVendor(vendor);
        System.out.println(resultMsg);
        return resultMsg;
    }
}
