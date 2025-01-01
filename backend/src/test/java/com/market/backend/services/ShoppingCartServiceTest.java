//package com.market.backend.services;
//
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.market.backend.dtos.ShoppingCartDTO;
//import com.market.backend.models.*;
//import com.market.backend.repositories.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.ArrayList;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class ShoppingCartServiceTest {
//
//    @Mock
//    private ShoppingCartRepository shoppingCartRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private ShoppingCartProductRepository shoppingCartProductRepository;
//
//    @InjectMocks
//    private ShoppingCartService shoppingCartService;
//
//    private ShoppingCart shoppingCart;
//    private Product product;
//    private Account account;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        account = new Account();
//        account.setId(1L);
//
//        product = new Product();
//        product.setId(101L);
//        product.setName("Product1");
//        product.setPrice(20.0);
//        product.setQuantity(10);
//
//        shoppingCart = new ShoppingCart();
//        shoppingCart.setId(1L);
//        shoppingCart.setUserAccount(account);
//        shoppingCart.setCartProducts(new ArrayList<>());
//    }
//
//    @Test
//    void testAddProductToCart() {
//        Long userId = 1L;
//        Long productId = 101L;
//        int quantity = 2;
//
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//        when(shoppingCartRepository.findByUserAccountId(userId)).thenReturn(Optional.of(shoppingCart));
//        when(shoppingCartProductRepository.findByShoppingCartAndProduct(any(), any())).thenReturn(Optional.empty());
//
//        shoppingCartService.addProductToCart(productId, userId, quantity);
//
//        assertEquals(1, shoppingCart.getCartProducts().size());
//        verify(shoppingCartProductRepository, times(1)).save(any());
//    }
//
//    @Test
//    void testRemoveProductFromCart() {
//        Long userId = 1L;
//        Long productId = 101L;
//
//        ShoppingCartProduct cartProduct = new ShoppingCartProduct();
//        cartProduct.setProduct(product);
//        cartProduct.setQuantity(2);
//        cartProduct.setPrice(40.0);
//        shoppingCart.getCartProducts().add(cartProduct);
//
//        when(shoppingCartRepository.findByUserAccountId(userId)).thenReturn(Optional.of(shoppingCart));
//
//        shoppingCartService.removeProductFromCart(productId, userId);
//
//        assertTrue(shoppingCart.getCartProducts().isEmpty());
//        verify(shoppingCartProductRepository, times(1)).deleteByShoppingCartAndProductId(any(), eq(productId));
//    }
//
//    @Test
//    void testGetCartProducts() {
//        Long userId = 1L;
//
//        ShoppingCartProduct cartProduct = new ShoppingCartProduct();
//        cartProduct.setProduct(product);
//        cartProduct.setQuantity(2);
//        cartProduct.setPrice(40.0);
//        shoppingCart.getCartProducts().add(cartProduct);
//
//        when(shoppingCartRepository.findByUserAccountId(userId)).thenReturn(Optional.of(shoppingCart));
//
//        ShoppingCartDTO shoppingCartDTO = shoppingCartService.getCartProducts(userId);
//
//        assertNotNull(shoppingCartDTO);
//        assertEquals(1, shoppingCartDTO.getProducts().size());
//        assertEquals(40.0, shoppingCartDTO.getTotalPrice());
//    }
//
//    @Test
//    void testUpdateProductQuantityInCart() {
//        Long userId = 1L;
//        Long productId = 101L;
//        int newQuantity = 3;
//
//        ShoppingCartProduct cartProduct = new ShoppingCartProduct();
//        cartProduct.setProduct(product);
//        cartProduct.setQuantity(2);
//        cartProduct.setPrice(40.0);
//        shoppingCart.getCartProducts().add(cartProduct);
//
//        when(shoppingCartRepository.findByUserAccountId(userId)).thenReturn(Optional.of(shoppingCart));
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//
//        shoppingCartService.updateProductQuantityInCart(productId, userId, newQuantity);
//
//        assertEquals(newQuantity, cartProduct.getQuantity());
//        assertEquals(60.0, cartProduct.getPrice());
//        verify(shoppingCartProductRepository, times(1)).save(any());
//    }
//}
//
