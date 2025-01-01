package com.market.backend.repositories;

import com.market.backend.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ShoppingCartProductRepositoryTest {

    @Autowired
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VendorRepository vendorRepository;

    private Account account;
    private ShoppingCart shoppingCart;
    private Product product;
    private Vendor vendor;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setUsername("username");
        account.setAuthType("oauth");
        account.setType("client");
        account = accountRepository.save(account);

        shoppingCart = new ShoppingCart();
        shoppingCart.setUserAccount(account);
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        vendor = new Vendor();
        vendor.setId(account.getId());
        vendor.setAccount(account);
        vendor.setTaxNumber(1235456545L);
        vendor.setOrganizationName("Test Organization");
        vendor = vendorRepository.save(vendor);

        product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setCategory("Electronics");
        product.setDescription("Test Product Description");
        product.setVendor(vendor);
        product = productRepository.save(product);
    }

    @Test
    void testFindByShoppingCartAndProduct() {
        ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
        shoppingCartProduct.setShoppingCart(shoppingCart);
        shoppingCartProduct.setProduct(product);
        shoppingCartProductRepository.save(shoppingCartProduct);

        Optional<ShoppingCartProduct> retrievedProduct = shoppingCartProductRepository.findByShoppingCartAndProduct(shoppingCart, product);

        assertTrue(retrievedProduct.isPresent());
        assertEquals(product.getId(), retrievedProduct.get().getProduct().getId());
        assertEquals(shoppingCart.getId(), retrievedProduct.get().getShoppingCart().getId());
    }

    @Test
    void testDeleteByShoppingCartAndProductId() {
        ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
        shoppingCartProduct.setShoppingCart(shoppingCart);
        shoppingCartProduct.setProduct(product);
        shoppingCartProductRepository.save(shoppingCartProduct);

        shoppingCartProductRepository.deleteByShoppingCartAndProductId(shoppingCart, product.getId());

        Optional<ShoppingCartProduct> deletedProduct = shoppingCartProductRepository.findByShoppingCartAndProduct(shoppingCart, product);
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    void testFindByShoppingCart() {
        ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
        shoppingCartProduct.setShoppingCart(shoppingCart);
        shoppingCartProduct.setProduct(product);
        shoppingCartProductRepository.save(shoppingCartProduct);

        List<ShoppingCartProduct> products = shoppingCartProductRepository.findByShoppingCart(shoppingCart);

        assertEquals(1, products.size());
        assertEquals(product.getId(), products.get(0).getProduct().getId());
    }
}

