package com.market.backend.signup.basicSignUp.Service;

import com.market.backend.signup.basicSignUp.Model.BasicClient;
import com.market.backend.signup.basicSignUp.Model.BasicVendor;
import com.market.backend.signup.basicSignUp.Model.VendorRequests;
import com.market.backend.signup.basicSignUp.Repository.AccountRepo;
import com.market.backend.signup.basicSignUp.Model.Account;
import com.market.backend.signup.basicSignUp.Repository.BasicClientRepo;
import com.market.backend.signup.basicSignUp.Repository.BasicVendorRepo;
import com.market.backend.signup.basicSignUp.Repository.VendorRequestsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SignUpService {

//    Mainly for clients as they are approved immediately
    @Autowired
    AccountRepo accRepo;

//    To Log vendor requests waiting approval from admins
    @Autowired
    VendorRequestsRepo vendorRequestsRepo;

    @Autowired
    private BasicClientRepo basicClientRepo;

    @Autowired
    private BasicVendorRepo basicVendorRepo;

    public boolean checkGoogleAccountExistance(Map<String, Object> attributes, boolean vendor) {
        List<Account> existingAccounts = accRepo.findAll();
        for (Account acc : existingAccounts) {
            if (acc.getEmail().equals((String)attributes.get("email")))
                return false;
        }

        if (vendor){
            List<VendorRequests> existingRequests = vendorRequestsRepo.findAll();
            for (VendorRequests req : existingRequests) {
                if (req.getEmail().equals((String)attributes.get("email")))
                    return false;
            }
        }

        return true;
    }

    public String registerClient(Map<String, Object> attributes) {
        if (checkGoogleAccountExistance(attributes, false)) {
            Account acc = new Account((String) attributes.get("email"), (String) attributes.get("name"), "client", "active");
            accRepo.save(acc);
            acc.toString();
            return "Client Registered Successfully";
        }
        return "Google Account is Already Registered";
    }

    public String registerVendorRequest(Map<String, Object> attributes, String org, long tax) {
        if(checkGoogleAccountExistance(attributes, true)){
            VendorRequests ven = new VendorRequests((String) attributes.get("email"), (String) attributes.get("name"), org, tax);
            vendorRequestsRepo.save(ven);
            return "Request Registered Successfully";
        }
        return "Already Used Gmail Account";
    }

    public String insertBasicClient(BasicClient client) {
        if (client.getUsername() == null)
            return "The username can't be empty";
        if (client.getUsername().length() > 80)
            return "The username can't be more than 80 character";
        if (client.getPassword() == null)
            return "The password can't be empty";
        if (client.getPassword().length() > 80)
            return "The password can't be more than 80 character";
        if (basicClientRepo.existsByUsername(client.getUsername())) {
            return "The username is already exist";
        }
        basicClientRepo.save(client);
        return "Successfully registered";
    }

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
