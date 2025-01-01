package com.market.backend.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.market.backend.dtos.ShoppingCartDTO;
import com.market.backend.models.*;
import com.market.backend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ShoppingCartProductRepository shoppingCartProductRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private ShoppingCart shoppingCart;
    private Product product;
    private Account account;
    private ShoppingCartProduct cartProduct;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);

        product = new Product();
        product.setId(101L);
        product.setName("Product1");
        product.setPrice(20.0);
        product.setQuantity(10);

        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUserAccount(account);
        shoppingCart.setCartProducts(new ArrayList<>());

        cartProduct = new ShoppingCartProduct();
        cartProduct.setId(1L);
        cartProduct.setProduct(product);
        cartProduct.setShoppingCart(shoppingCart);
        cartProduct.setQuantity(2);
        cartProduct.setPrice(40.0);
    }

    @Test
    void testAddProductToCart() {
        Long userId = 1L;
        Long productId = 101L;
        int quantity = 2;

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(shoppingCartRepository.findByUserAccountId(userId)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartProductRepository.findByShoppingCartAndProduct(shoppingCart, product))
                .thenReturn(Optional.empty());
        when(shoppingCartProductRepository.save(any(ShoppingCartProduct.class)))
                .thenReturn(cartProduct);

        shoppingCartService.addProductToCart(productId, userId, quantity);

        verify(shoppingCartProductRepository).save(any(ShoppingCartProduct.class));
    }

    @Test
    void testRemoveProductFromCart() {
        Long userId = 1L;
        Long productId = 101L;

        shoppingCart.getCartProducts().add(cartProduct);

        when(shoppingCartRepository.findByUserAccountId(userId)).thenReturn(Optional.of(shoppingCart));
        doNothing().when(shoppingCartProductRepository).deleteByShoppingCartAndProductId(shoppingCart, productId);

        shoppingCartService.removeProductFromCart(productId, userId);

        verify(shoppingCartProductRepository).deleteByShoppingCartAndProductId(shoppingCart, productId);
    }

    @Test
    void testGetCartProducts() {
        Long userId = 1L;
        List<ShoppingCartProduct> cartProducts = List.of(cartProduct);

        when(shoppingCartRepository.findByUserAccountId(userId)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartProductRepository.findByShoppingCart(shoppingCart)).thenReturn(cartProducts);

        ShoppingCartDTO shoppingCartDTO = shoppingCartService.getCartProducts(userId);

        assertNotNull(shoppingCartDTO);
        assertEquals(1, shoppingCartDTO.getProducts().size());
        assertEquals(40.0, shoppingCartDTO.getTotalPrice());
        verify(shoppingCartRepository).findByUserAccountId(userId);
        verify(shoppingCartProductRepository).findByShoppingCart(shoppingCart);
    }

    @Test
    void testUpdateProductQuantityInCart() {
        Long userId = 1L;
        Long productId = 101L;
        int newQuantity = 3;

        when(shoppingCartRepository.findByUserAccountId(userId)).thenReturn(Optional.of(shoppingCart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(shoppingCartProductRepository.findByShoppingCartAndProduct(shoppingCart, product))
                .thenReturn(Optional.of(cartProduct));
        when(shoppingCartProductRepository.save(any(ShoppingCartProduct.class)))
                .thenReturn(cartProduct);

        shoppingCartService.updateProductQuantityInCart(productId, userId, newQuantity);

        assertEquals(newQuantity, cartProduct.getQuantity());
        verify(shoppingCartProductRepository).save(cartProduct);
    }

    @Test
    void testAddProductToCart_ProductNotFound() {
        Long userId = 1L;
        Long productId = 101L;
        int quantity = 2;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                shoppingCartService.addProductToCart(productId, userId, quantity));
    }

    @Test
    void testAddProductToCart_ExceedsStock() {
        Long userId = 1L;
        Long productId = 101L;
        int quantity = 20; // Product only has 10 in stock

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () ->
                shoppingCartService.addProductToCart(productId, userId, quantity));
    }
}