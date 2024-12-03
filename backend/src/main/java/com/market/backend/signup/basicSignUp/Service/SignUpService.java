package com.market.backend.signup.basicSignUp.Service;

//import com.market.backend.signup.basicSignUp.Model.BasicClient;
//import com.market.backend.signup.basicSignUp.Model.BasicVendor;
import com.market.backend.signup.basicSignUp.Model.VendorRequests;
//import com.market.backend.signup.basicSignUp.Repository.AccountRepo;
import com.market.backend.signup.basicSignUp.Model.Account;
import com.market.backend.signup.basicSignUp.Repository.AccountRepository;
//import com.market.backend.signup.basicSignUp.Repository.BasicClientRepo;
//import com.market.backend.signup.basicSignUp.Repository.BasicVendorRepo;
//import com.market.backend.signup.basicSignUp.Repository.VendorRequestsRepo;
//import com.market.backend.signup.basicSignUp.Repository.VendorRequestsRepository;
import com.market.backend.signup.basicSignUp.Repository.VendorRequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SignUpService {

//    Mainly for clients as they are approved immediately
    @Autowired
    AccountRepository accRepo;

//    To Log vendor requests waiting approval from admins
    @Autowired
    VendorRequestsRepository vendorRequestsRepo;

//    @Autowired
////    private BasicClientRepo accRepo;

//    @Autowired
//    private BasicVendorRepo vendorRequestsRepo;

    public boolean checkGoogleAccountExistance(Map<String, Object> attributes, boolean vendor) {

        if (accRepo.existsByEmail(attributes.get("email").toString()))
            return false;
        if (vendor && vendorRequestsRepo.existsByEmail(attributes.get("email").toString())){
            return false;
        }
        return true;
    }

    public String insertClientByGoogleAccount(Map<String, Object> attributes) {
        if (checkGoogleAccountExistance(attributes, false)) {
            Account acc = new Account((String) attributes.get("email"), (String) attributes.get("name"), "client", true);
            accRepo.save(acc);
            acc.toString();
            return "Client Registered Successfully";
        }
        return "Google Account is Already Registered";
    }

    public String insertBasicClient(Account client) {
        if (client.getUsername() == null)
            return "The username can't be empty";
        if (client.getUsername().length() > 80)
            return "The username can't be more than 80 character";
        if (client.getPassword() == null)
            return "The password can't be empty";
        if (client.getPassword().length() > 80)
            return "The password can't be more than 80 character";
        if (accRepo.existsByUsername(client.getUsername())) {
            return "The username is already exist";
        }
        client.setActive(true);
        client.setType("client");
        accRepo.save(client);
        return "Successfully registered";
    }

    public String registerVendorRequest(Map<String, Object> attributes, String org, long tax) {
        if(checkGoogleAccountExistance(attributes, true)){
            VendorRequests ven = new VendorRequests((String) attributes.get("email"), (String) attributes.get("name"), org, tax);
            vendorRequestsRepo.save(ven);
            return "Request Registered Successfully";
        }
        return "Already Used Gmail Account";
    }

    public String insertBasicVendor(VendorRequests vendor) {
        if (vendor.getOrganizationName() == null)
            return "The business name can't be empty";
        if (vendor.getOrganizationName().length() > 80)
            return "The business name can't be more than 80 character";
        if (vendor.getPassword() == null)
            return "The password can't be empty";
        if (vendor.getPassword().length() > 80)
            return "The password can't be more than 80 character";
        if (vendor.getTaxNumber() == -1)
            return "The tax number can't be empty";
        if (String.valueOf(vendor.getTaxNumber()).length() != 9) {
            System.out.println(String.valueOf(vendor.getTaxNumber()).length());
            return "The tax number must be of 9 numbers only";
        }
        if (vendorRequestsRepo.existsByorganizationName(vendor.getOrganizationName())) {
            return "The business name is already exist";
        }
        if (vendorRequestsRepo.existsBytaxNumber(vendor.getTaxNumber()))
            return "The tax number is already exist";

        vendorRequestsRepo.save(vendor);
        return "Successfully registered";
    }

}
