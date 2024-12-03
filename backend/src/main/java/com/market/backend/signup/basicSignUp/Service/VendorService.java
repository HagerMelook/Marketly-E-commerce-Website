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
        if (String.valueOf(vendor.getTaxnumber()).length() != 9) {
            System.out.println(String.valueOf(vendor.getTaxnumber()).length());
            return "The tax number must be of 9 numbers only";
        }
        if (basicVendorRepo.existsByBusinessname(vendor.getBusinessname())) {
            return "The business name is already exist";
        }
        if (basicVendorRepo.existsByTaxnumber(vendor.getTaxnumber()))
            return "The tax number is already exist";

        basicVendorRepo.save(vendor);
        return "Successfully registered";
    }
}