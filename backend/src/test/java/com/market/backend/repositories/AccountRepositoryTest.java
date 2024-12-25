package com.market.backend.repositories;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.market.backend.models.Account;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountRepositoryTest {
    
    AccountRepository accountRepository;
    
    @Autowired
    void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Test
    void FindAll_ReturnsAccountsList() {
        Account account1 =  Account.builder()
                .username("omarAhmed").type("client")
                .isActive(true).authType("oauth")
                .build();
        Account account2 =  Account.builder()
                .username("hazem75").type("vendor")
                .isActive(true).authType("basic")
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findAll();
        assertFalse(accounts.isEmpty());
        accounts.forEach(account -> {
            assertThat(account.getId()).isNotNull();
            assertNotEquals(account.getId(), 0);
        });
    }

    @Test
    void FindAll_EmptyDB_ReturnsEmptyList() {
        List<Account> accounts = accountRepository.findAll();
        assertTrue(accounts.isEmpty());
    }

    @Test
    void existsByUsername_CorrectUsername_ReturnsTrue() {
        Account account1 =  Account.builder()
                .username("ahmed321").type("client")
                .isActive(true).authType("oauth")
                .build();
        Account account2 =  Account.builder()
                .username("omar7").type("vendor")
                .isActive(true).authType("basic")
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        boolean result =
                accountRepository.existsByUsername("ahmed321")
                && accountRepository.existsByUsername("omar7");
        assertTrue(result);
    }

    @Test
    void existsByUsername_AbsentUsername_ReturnsFalse() {
        Account account1 =  Account.builder()
                .username("ahmed321").type("client")
                .isActive(true).authType("oauth")
                .build();
        Account account2 =  Account.builder()
                .username("omar7").type("vendor")
                .isActive(true).authType("basic")
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        boolean result = accountRepository.existsByUsername("AhmedAshraf");
        assertFalse(result);
    }

    @Test
    void findByUsername_ExistingUsername_ReturnsCorrectAccount() {
        Account account1 =  Account.builder()
                .username("omarAhmed").type("client")
                .isActive(true).authType("oauth")
                .build();
        Account account2 =  Account.builder()
                .username("hazem75").type("vendor")
                .isActive(true).authType("basic")
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        Optional<Account> accountWithUsernameOmar = accountRepository.findByUsername("omarAhmed");
        assertTrue(accountWithUsernameOmar.isPresent());
        assertEquals("omarAhmed", accountWithUsernameOmar.get().getUsername());
        assertNotEquals(accountWithUsernameOmar.get().getId(), 0);
    }

    @Test
    void findByUsername_AbsentUsername_ReturnsNull() {
        Account account1 =  Account.builder()
                .username("omarAhmed").type("client")
                .isActive(true).authType("oauth")
                .build();
        Account account2 =  Account.builder()
                .username("hazem75").type("vendor")
                .isActive(true).authType("basic")
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        Optional<Account> accountWithUsernameMuhammad = accountRepository.findByUsername("Muhammad");
        assertFalse(accountWithUsernameMuhammad.isPresent());
    }

    @Test
    void deleteAll_ReturnsEmptyDB() {
        Account account1 =  Account.builder()
                .username("omarAhmed").type("client")
                .isActive(true).authType("oauth")
                .build();
        Account account2 =  Account.builder()
                .username("hazem75").type("vendor")
                .isActive(true).authType("basic")
                .build();

        accountRepository.save(account1);
        accountRepository.save(account2);

        accountRepository.deleteAll();

        List<Account> accounts = accountRepository.findAll();
        assertTrue(accounts.isEmpty());
    }
}
