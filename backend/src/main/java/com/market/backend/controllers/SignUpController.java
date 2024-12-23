package com.market.backend.controllers;

import com.market.backend.models.Account;
import com.market.backend.models.VendorRequest;
import com.market.backend.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/SignUp")
public class SignUpController {

    private final SignUpService service;

    public SignUpController(SignUpService service) {
        this.service = service;
    }

    @GetMapping("/Google/Client")
    public String googleOauthClient(@AuthenticationPrincipal OAuth2User principal){
        System.out.println("Register a client with gmail "+ principal.getAttributes().get("email"));
        return service.insertClientByGoogleAccount(principal.getAttributes().get("email").toString());
    }

    @GetMapping("/Google/Vendor/{org}/{tax}")
    public String googleOauthVendor(@AuthenticationPrincipal OAuth2User request, @PathVariable String org, @PathVariable long tax){
        System.out.println("Initiating a Vendor Request");
        return service.registerVendorRequest(request.getAttributes().get("email").toString(), org, tax);
    }

    @PostMapping("/ClientBasicSignUp/{password}")
    public String clientBasicSignUp(@RequestBody Account client, @PathVariable String password) {
        System.out.println("Received User: " + client.getUsername() + ", " + password);
        return service.insertBasicClient(client, password);
    }

    @PostMapping("/VendorBasicSignUp")
    public String vendorBasicSignUp(@RequestBody VendorRequest vendor) {
        System.out.println("Received User: " + vendor.getOrganizationName() + ", " + vendor.getPassword() + ", "
                + vendor.getTaxNumber()+ ", " + vendor.getUsername());
        return service.insertBasicVendor(vendor);
    }


}
