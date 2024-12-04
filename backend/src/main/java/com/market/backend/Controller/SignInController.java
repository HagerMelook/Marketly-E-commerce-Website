package com.market.backend.Controller;

import org.springframework.web.bind.annotation.*;

import com.market.backend.DTOs.AuthRequest;
import com.market.backend.DTOs.AuthResponse;
import com.market.backend.Services.AuthService;

@RestController
@RequestMapping("/auth")
public class SignInController {

    private final AuthService authService;

    public SignInController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public AuthResponse signIn(@RequestBody AuthRequest authRequest) {
        boolean role =authService.authenticate(authRequest.getUsername(), authRequest.getPassword()) ;
        if (role) {
            return new AuthResponse("Authentication Successful", "valid");
        } else {
            return new AuthResponse("Authentication Failed", null);
        }
    }

    
    
}
