//package com.market.backend.signup.basicSignUp.Controller;
//
//import com.market.backend.signup.basicSignUp.Model.BasicVendor;
//import com.market.backend.signup.basicSignUp.Service.VendorService;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/SignUp")
//public class VendorController {
//    private final VendorService vendorService;
//
//    public VendorController(VendorService vendorService) {
//        this.vendorService = vendorService;
//    }
//
//    // Basic SignUp for Vendor
//    @PostMapping("/VendorBasicSignUp")
//    public String vendorBasicSignUp(@RequestBody BasicVendor vendor) {
//        System.out.println("Received User: " + vendor.getBusinessname() + ", " + vendor.getPassword() + ", "
//                + vendor.getTaxnumber());
//        String resultMsg;
//        resultMsg = vendorService.insertBasicVendor(vendor);
//        System.out.println(resultMsg);
//        return resultMsg;
//    }
//}
