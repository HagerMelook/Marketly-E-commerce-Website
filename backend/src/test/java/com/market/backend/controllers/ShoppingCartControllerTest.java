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

        doReturn(shoppingCartDTO).when(shoppingCartService).getCartProducts(accountId);

        mockMvc.perform(get("/ShoppingCart/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").exists())
                .andExpect(jsonPath("$.products[0].id").value(101))
                .andExpect(jsonPath("$.products[0].product.name").value("Product1"))
                .andExpect(jsonPath("$.products[0].quantity").value(2))
                .andExpect(jsonPath("$.products[0].price").value(40.0))
                .andExpect(jsonPath("$.totalPrice").value(40.0));

        verify(shoppingCartService, times(1)).getCartProducts(accountId);
    }


    @Test
    void testDeleteProductFromCart() throws Exception {
        long productId = 101L;
        long accountId = 1L;

        mockMvc.perform(delete("/ShoppingCart/Delete/{productId}/{accountId}", productId, accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product removed from cart."));

        verify(shoppingCartService, times(1)).removeProductFromCart(productId, accountId);
    }

    @Test
    void testUpdateProductQuantityInCart() throws Exception {
        long productId = 101L;
        long accountId = 1L;
        int newQuantity = 3;

        mockMvc.perform(post("/ShoppingCart/Update/{productId}/{accountId}", productId, accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(newQuantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product quantity updated."));

        verify(shoppingCartService, times(1)).updateProductQuantityInCart(productId, accountId, newQuantity);
    }
}