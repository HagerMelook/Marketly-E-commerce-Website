package com.market.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market.backend.Services.JWTService;
import com.market.backend.repositories.AccountRepository;

@RestController
@RequestMapping("/auth")
public class SignInController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/signin")
    public String signIn() {
        // Get the authenticated user from the Security Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract username (principal)
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Extract user roles as a list of strings
            String roles = authentication.getAuthorities()
                    .stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .toList()
                    .toString(); // Convert to a string or pass as a list
            // extract account_id
            long id = accountRepository.findIdByUsername(userDetails.getUsername());
            // Generate and return the JWT
            return jwtService.generateToken(userDetails.getUsername(), roles, id);
        }

        throw new IllegalStateException("User is not authenticated");
    }

    @GetMapping("/signin/google")
    public String googleSignIn(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || principal.getAttributes() == null) {
            return "Google Sign-In failed";
        }

        // Extract email and details from Google
        String email = principal.getAttribute("email");
        System.out.println("Google Sign-In successful for email: " + email);
        // extract account_id
        long id = accountRepository.findIdByUsername(email);
        //extract roles
        String roles = accountRepository.getTypeByUserName(email);
        // Generate a JWT for the authenticated user
        return jwtService.generateToken(email, roles,id); // You can assign roles accordingly
        
    }
}
