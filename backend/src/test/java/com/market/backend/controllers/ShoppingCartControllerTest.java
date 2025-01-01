package com.market.backend.controllers;

import com.market.backend.configurations.JWTFilter;
import com.market.backend.dtos.ProductDTO;
import com.market.backend.dtos.ShoppingCartDTO;
import com.market.backend.dtos.ShoppingCartProductDTO;
import com.market.backend.services.JWTService;
import com.market.backend.services.ShoppingCartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({JWTService.class, JWTFilter.class})
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private ShoppingCartService shoppingCartService;

    @Test
    void testAddProductToCart() throws Exception {
        long accountId = 1L;
        long productId = 101L;
        int quantity = 2;

        doNothing().when(shoppingCartService).addProductToCart(productId, accountId, quantity);

        mockMvc.perform(post("/ShoppingCart/Add/{accountId}", accountId)
                        .param("productId", String.valueOf(productId))
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added to cart successfully."));

        verify(shoppingCartService, times(1)).addProductToCart(productId, accountId, quantity);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testAddProductToCartWithInvalidQuantity() throws Exception {
        long accountId = 1L;
        long productId = 101L;
        int invalidQuantity = -1;

        doThrow(new IllegalArgumentException("Quantity must be positive"))
                .when(shoppingCartService).addProductToCart(productId, accountId, invalidQuantity);

        mockMvc.perform(post("/ShoppingCart/Add/{accountId}", accountId)
                        .param("productId", String.valueOf(productId))
                        .param("quantity", String.valueOf(invalidQuantity)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Quantity must be positive"));

        verify(shoppingCartService, times(1)).addProductToCart(productId, accountId, invalidQuantity);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testAddProductToCartWithInvalidProduct() throws Exception {
        long accountId = 1L;
        long invalidProductId = -1L;
        int quantity = 1;

        doThrow(new IllegalArgumentException("Product not found"))
                .when(shoppingCartService).addProductToCart(invalidProductId, accountId, quantity);

        mockMvc.perform(post("/ShoppingCart/Add/{accountId}", accountId)
                        .param("productId", String.valueOf(invalidProductId))
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Product not found"));

        verify(shoppingCartService, times(1)).addProductToCart(invalidProductId, accountId, quantity);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testGetCartProducts() throws Exception {
        long accountId = 1L;

        ProductDTO productDTO = ProductDTO.builder()
                .id(101L)
                .vendorId(1L)
                .name("Product1")
                .description("Description of Product1")
                .price(20.0)
                .rating(4.5)
                .quantity(10)
                .category("Category1")
                .imagePaths(List.of("path1", "path2"))
                .build();

        ShoppingCartProductDTO shoppingCartProductDTO = new ShoppingCartProductDTO(
                101L, productDTO, 2, 40.0);

        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO(
                List.of(shoppingCartProductDTO), 40.0);

        when(shoppingCartService.getCartProducts(accountId)).thenReturn(shoppingCartDTO);

        mockMvc.perform(get("/ShoppingCart/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").exists())
                .andExpect(jsonPath("$.products[0].id").value(101))
                .andExpect(jsonPath("$.products[0].product.name").value("Product1"))
                .andExpect(jsonPath("$.products[0].quantity").value(2))
                .andExpect(jsonPath("$.products[0].price").value(40.0))
                .andExpect(jsonPath("$.totalPrice").value(40.0));

        verify(shoppingCartService, times(1)).getCartProducts(accountId);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testGetEmptyCart() throws Exception {
        long accountId = 1L;
        ShoppingCartDTO emptyCart = new ShoppingCartDTO(List.of(), 0.0);

        when(shoppingCartService.getCartProducts(accountId)).thenReturn(emptyCart);

        mockMvc.perform(get("/ShoppingCart/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isEmpty())
                .andExpect(jsonPath("$.totalPrice").value(0.0));

        verify(shoppingCartService, times(1)).getCartProducts(accountId);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testDeleteProductFromCart() throws Exception {
        long productId = 101L;
        long accountId = 1L;

        doNothing().when(shoppingCartService).removeProductFromCart(productId, accountId);

        mockMvc.perform(delete("/ShoppingCart/Delete/{productId}/{accountId}", productId, accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product removed from cart."));

        verify(shoppingCartService, times(1)).removeProductFromCart(productId, accountId);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testDeleteNonExistentProductFromCart() throws Exception {
        long nonExistentProductId = 999L;
        long accountId = 1L;

        doThrow(new IllegalArgumentException("Product not found in cart"))
                .when(shoppingCartService).removeProductFromCart(nonExistentProductId, accountId);

        mockMvc.perform(delete("/ShoppingCart/Delete/{productId}/{accountId}", nonExistentProductId, accountId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Product not found in cart"));

        verify(shoppingCartService, times(1)).removeProductFromCart(nonExistentProductId, accountId);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testUpdateProductQuantityInCart() throws Exception {
        long productId = 101L;
        long accountId = 1L;
        int newQuantity = 3;

        doNothing().when(shoppingCartService).updateProductQuantityInCart(productId, accountId, newQuantity);

        mockMvc.perform(post("/ShoppingCart/Update/{productId}/{accountId}", productId, accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(newQuantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product quantity updated."));

        verify(shoppingCartService, times(1)).updateProductQuantityInCart(productId, accountId, newQuantity);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testUpdateProductQuantityWithInvalidQuantity() throws Exception {
        long productId = 101L;
        long accountId = 1L;
        int invalidQuantity = 0;

        doThrow(new IllegalArgumentException("Quantity must be positive"))
                .when(shoppingCartService).updateProductQuantityInCart(productId, accountId, invalidQuantity);

        mockMvc.perform(post("/ShoppingCart/Update/{productId}/{accountId}", productId, accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(invalidQuantity)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Quantity must be positive"));

        verify(shoppingCartService, times(1)).updateProductQuantityInCart(productId, accountId, invalidQuantity);
        verifyNoMoreInteractions(shoppingCartService);
    }

    @Test
    void testUpdateNonExistentProductQuantity() throws Exception {
        long nonExistentProductId = 999L;
        long accountId = 1L;
        int newQuantity = 1;

        doThrow(new IllegalArgumentException("Product not found in cart"))
                .when(shoppingCartService).updateProductQuantityInCart(nonExistentProductId, accountId, newQuantity);

        mockMvc.perform(post("/ShoppingCart/Update/{productId}/{accountId}", nonExistentProductId, accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(newQuantity)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Product not found in cart"));

        verify(shoppingCartService, times(1)).updateProductQuantityInCart(nonExistentProductId, accountId, newQuantity);
        verifyNoMoreInteractions(shoppingCartService);
    }
}