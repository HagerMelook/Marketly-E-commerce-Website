package com.market.backend.signup.basicSignUp.Repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.signup.basicSignUp.Model.Account;

@SpringBootTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepo;
    
    @Test
    void testExistsByEmailExist() {
        Account account =  new Account("hagermelook321@gmail.com","hager ashraf","client",true);
        accountRepo.save(account);
        boolean result = accountRepo.existsByEmail("hagermelook321@gmail.com");
        assertTrue(result,"The Account should exist");
    }

    @Test
    void testExistsByEmailNotExist() {
        boolean result = accountRepo.existsByEmail("ahmed123@gmail.com");
        assertFalse(result,"The Account should not exist");
    }

    @Test
    void testExistsByUsernameExist() {
        Account account =  new Account("ahmed321@gmail.com","ahmed ashraf","client",true);
        accountRepo.save(account);
        boolean result = accountRepo.existsByUsername("ahmed ashraf");
        assertTrue(result,"The Account should exist");
    }

    @Test
    void testExistsByUsernameNotExist() {
        boolean result = accountRepo.existsByUsername("ali");
        assertFalse(result,"The Account should not exist");
    }

    @Test
    void testFindByEmailExist() {
        Account account =  new Account("momen123@gmail.com","momen hany","client",true);
        accountRepo.save(account);

        Optional<Account> account_demo = accountRepo.findByEmail("momen123@gmail.com");
        assertTrue(account_demo.isPresent());
        assertEquals("momen123@gmail.com", account_demo.get().getEmail());
        assertEquals("momen hany", account_demo.get().getUsername());
    }

    @Test
    void testFindByEmailNotExist() {
        Optional<Account> account_demo = accountRepo.findByEmail("mohammed321@gmail.com");
        assertFalse(account_demo.isPresent());
    }

    @Test
    void testFindByUsernameExist() {
        Account account =  new Account("ziad123@gmail.com","Ziad Ahmed","client",true);
        accountRepo.save(account);

        Optional<Account> account_demo = accountRepo.findByUsername("Ziad Ahmed");
        assertTrue(account_demo.isPresent());
        assertEquals("ziad123@gmail.com", account_demo.get().getEmail());
        assertEquals("Ziad Ahmed", account_demo.get().getUsername());
    }

    @Test
    void testFindByUsernameNotExist() {
        Optional<Account> account_demo = accountRepo.findByUsername("Ahmed Ali");
        assertFalse(account_demo.isPresent());
    }
}
