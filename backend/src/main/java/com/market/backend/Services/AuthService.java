package com.market.backend.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.market.backend.repositories.AccountRepository;

@Service
public class AuthService {

    private final AccountRepository accountRepository;

    // Constructor Injection for Repository
    public AuthService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean authenticate(String username, String password) {
        // Try to find the account by username
        Optional<com.market.backend.models.Account> account = accountRepository.findByUsername(username);
            
        // If account not found, return null
        if (!account.isPresent())
            return false; // Authentication failed

        com.market.backend.models.Account acc = account.get();
        // Check if the password matches
        if (acc.getPassword().equals(password))
            // Authentication succeeded, return the username or token as required
            return true; // or return account.getId() or token

        return false; // Password incorrect
    }
}
