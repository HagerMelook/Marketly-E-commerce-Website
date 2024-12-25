package com.market.backend.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.market.backend.models.Account;
import com.market.backend.models.Password;

@SpringBootTest
@Transactional
public class PasswordRepositoryTest {
    @Autowired
    private PasswordRepository passwordRepository;

    @Test
    void TestFindByAccountId() {
        Account account = Account.builder()
                .isActive(true)
                .username("Yara1")
                .authType("basic")
                .type("client")
                .build();

        Password password = Password.builder()
                .account(account)
                .accountPassword("123new")
                .build();

        passwordRepository.save(password);
        Password storedPassword = passwordRepository.findByAccountId(account.getId());
        assertEquals(password, storedPassword);
    }

    @Test
    void TestNotFindByAccountId() {
        Password storedPassword = passwordRepository.findByAccountId(100L);
        assertNull(storedPassword);
    }
}
