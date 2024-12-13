package com.market.backend.services;

import com.market.backend.models.VendorRequest;
import com.market.backend.repositories.PasswordRepository;
import com.market.backend.repositories.VendorRequestRepository;
import com.market.backend.models.Account;
import com.market.backend.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    VendorRequestRepository vendorRequestRepository;
    @Autowired
    PasswordRepository passwordRepository;

    public boolean checkUsernameAvailability(String username) {
        return !vendorRequestRepository.existsByUsername(username)
                && !accountRepository.existsByUsername(username);
    }

    private boolean checkVendorAvailability(String org, long tax) {
        return !vendorRequestRepository.existsByOrganizationName(org)
                && !vendorRequestRepository.existsBytaxNumber(tax);
    }

    public String insertClientByGoogleAccount(String email) {
        String username = email.split("@")[0];
        if (checkUsernameAvailability(username)) {
            Account acc = Account.builder()
                .isActive(true)
                .authType("oauth")
                .username(username)
                .type("client")
                .build();

            accountRepository.save(acc);
            return "Client Registered Successfully";
        }
        return "Google Account is Already Registered";
    }

    public String registerVendorRequest(String email, String org, long tax) {
        String username = email.split("@")[0];
        if(checkUsernameAvailability(username) && checkVendorAvailability(org, tax)){
            VendorRequest ven = VendorRequest.builder()
                .organizationName(org)
                .username(username)
                .taxNumber(tax)
                .build();
            vendorRequestRepository.save(ven);
            return "Request Registered Successfully";
        }
        return "Google Account is Already Registered";
    }

    @Transactional
    public String insertBasicClient(Account client, String password) {
        if (client.getUsername() == null)
            return "The username can't be empty";
        if (client.getUsername().length() > 80)
            return "The username can't be more than 80 character";
        if (password == null)
            return "The password can't be empty";
        if (password.length() > 80)
            return "The password can't be more than 80 character";
        if (accountRepository.existsByUsername(client.getUsername())) {
            return "The username is already exist";
        }

        client.setActive(true);
        client.setType("client");
        client.setAuthType("basic");

        Account acc = accountRepository.save(client);
        long id  = acc.getId();
        System.out.println("His id is "+id);
//        passwordRepository.save(new Password(id, password, acc));

        return "Successfully registered";
    }

    public String insertBasicVendor(VendorRequest vendor) {
        System.out.println("Inserting Basic Vendor Request");
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
        if (vendorRequestRepository.existsByOrganizationName(vendor.getOrganizationName())) {
            return "The business name is already exist";
        }
        if (vendorRequestRepository.existsBytaxNumber(vendor.getTaxNumber()))
            return "The tax number is already exist";

        vendorRequestRepository.save(vendor);
        return "Successfully registered";
    }



}
