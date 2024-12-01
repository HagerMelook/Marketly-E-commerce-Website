package com.market.backend.signup.basicSignUp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.market.backend.signup.basicSignUp.Model.BasicVendor;
import com.market.backend.signup.basicSignUp.Repository.BasicVendorRepo;

@Service
public class VendorService {
    @Autowired
    private BasicVendorRepo basicVendorRepo;

    public String insertBasicVendor(BasicVendor vendor) {
        if (basicVendorRepo.existsByBusinessname(vendor.getBusinessname())) {
            return "The business name is already exist";
        }
        if (basicVendorRepo.existsByTaxnumber(vendor.getTaxnumber()))
            return "The tax number is already exist";

        basicVendorRepo.save(vendor);
        return "Successfully registered";
    }
}