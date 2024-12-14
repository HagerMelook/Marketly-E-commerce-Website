package com.market.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.market.backend.Services.JWTService;

@RestController
@RequestMapping("/auth")
public class SignInController {

    @Autowired
    private JWTService jwtService;

    @PostMapping("/signin")
    public String signIn() {
        // Get the authenticated user from the Security Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract username (principal)
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate and return the JWT
            return jwtService.generateToken(userDetails.getUsername());
        }

        throw new IllegalStateException("User is not authenticated");
    }
}
