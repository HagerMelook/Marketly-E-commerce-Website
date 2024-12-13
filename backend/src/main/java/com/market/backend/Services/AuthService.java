package com.market.backend.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.PasswordRepository;
@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordRepository passwordRepository;
    
    // Constructor Injection for Repository
    public AuthService(AccountRepository accountRepository, PasswordRepository passwordRepository) {
        this.accountRepository = accountRepository;
        this.passwordRepository = passwordRepository;
    }

    public boolean authenticate(String username, String password) {
        // Try to find the account by username
        Optional<com.market.backend.models.Account> account = accountRepository.findByUsername(username);
        // If account not found, return null
       
        if (!account.isPresent())
            return false; // Authentication failed

        com.market.backend.models.Account acc = account.get();
        String pass = passwordRepository.getAccountPasswordById(acc.getId());
        // Check if the password matches
        if (pass.equals(password))
            // Authentication succeeded, return the username or token as required
            return true; // or return account.getId() or token

        return false; // Password incorrect
    }



}
