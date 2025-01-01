package com.market.backend.repositories;

import com.market.backend.models.Account;
import com.market.backend.models.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Account account;
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setUsername("username");
        account.setAuthType("oauth");
        account.setType("client");
        account = accountRepository.save(account);

        shoppingCart = new ShoppingCart();
        shoppingCart.setUserAccount(account);
        shoppingCartRepository.save(shoppingCart);
    }

    @Test
    void testFindByUserAccountId() {
        Optional<ShoppingCart> retrievedCart = shoppingCartRepository.findByUserAccountId(account.getId());

        assertTrue(retrievedCart.isPresent());
        assertEquals(account.getId(), retrievedCart.get().getUserAccount().getId());
    }

    @Test
    void testFindByNonExistentUserAccountId() {
        Optional<ShoppingCart> retrievedCart = shoppingCartRepository.findByUserAccountId(999L);

        assertFalse(retrievedCart.isPresent());
    }
}
