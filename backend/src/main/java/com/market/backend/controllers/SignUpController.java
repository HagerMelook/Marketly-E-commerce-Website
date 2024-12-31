package com.market.backend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market.backend.models.Account;
import com.market.backend.models.VendorRequest;
import com.market.backend.services.JWTService;
import com.market.backend.services.SignUpService;

@RestController
@CrossOrigin
@RequestMapping("/SignUp")
public class SignUpController {
        @Autowired
    private JWTService jwtService;

    private final SignUpService service;

    public SignUpController(SignUpService service) {
        this.service = service;
    }

    @GetMapping("/Google/Client")
    public String googleOauthClient(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println("Register a client with gmail " + principal.getAttributes().get("email"));
        return service.insertClientByGoogleAccount(principal.getAttributes().get("email").toString());
    }

    @GetMapping("/Google/Vendor/{org}/{tax}")
    public String googleOauthVendor(@AuthenticationPrincipal OAuth2User request, @PathVariable String org,
            @PathVariable long tax) {
        System.out.println("Initiating a Vendor Request");
        return service.registerVendorRequest(request.getAttributes().get("email").toString(), org, tax);
    }

    @PostMapping("/ClientBasicSignUp/{password}")
    public ResponseEntity<Map<String, Object>> clientBasicSignUp(@RequestBody Account client,
            @PathVariable String password) {
        System.out.println("Received User: " + client.getUsername() + ", " + password);
        String result = service.insertBasicClient(client, password);
        Map<String, Object> response = new HashMap<>();
        if (result.contains("Successfully registered")) {
            String roles = "[client]";
            String token = jwtService.generateToken(client.getUsername());
            System.out.println(token); // You can assign roles accordingly
            response.put("token",token);
            response.put("id",result.split(",")[1]);
            response.put("driverId",result.split(",")[1]);
            response.put("role",roles);
        }
        response.put("msg",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/VendorBasicSignUp")
    public String vendorBasicSignUp(@RequestBody VendorRequest vendor) {
        System.out.println("Received User: " + vendor.getOrganizationName() + ", " + vendor.getPassword() + ", "
                + vendor.getTaxNumber() + ", " + vendor.getUsername());
        return service.insertBasicVendor(vendor);
    }

}
