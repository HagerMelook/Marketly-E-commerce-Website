package com.market.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        userDetails = new User("testuser", "password", new ArrayList<>());
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String token = jwtService.generateToken("testuser");
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void validateToken_ShouldReturnTrue_ForValidToken() {
        String token = jwtService.generateToken("testuser");
        boolean isValid = jwtService.validateToken(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = jwtService.generateToken("testuser");
        String username = jwtService.extractUserName(token);
        assertEquals("testuser", username);
    }

    @Test
    void validateToken_ShouldReturnFalse_ForInvalidUser() {
        String token = jwtService.generateToken("testuser");
        UserDetails wrongUser = new User("wronguser", "password", new ArrayList<>());
        boolean isValid = jwtService.validateToken(token, wrongUser);
        assertFalse(isValid);
    }
}
