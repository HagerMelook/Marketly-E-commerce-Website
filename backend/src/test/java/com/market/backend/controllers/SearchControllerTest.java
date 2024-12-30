package com.market.backend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import com.market.backend.configurations.JWTFilter;
import com.market.backend.services.JWTService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.market.backend.models.Account;
import com.market.backend.models.Product;
import com.market.backend.models.Vendor;
import com.market.backend.services.SearchService;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({JWTService.class, JWTFilter.class})
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private SearchService searchService;

    @Test
    void testSearchExist() throws Exception {
        List<Product> productTest = new ArrayList<>();

        Account accountTest = Account.builder()
                .id(1L)
                .isActive(true)
                .username("noon")
                .type("vendor")
                .authType("basic")
                .build();

        Vendor vendor = Vendor.builder()
                .account(accountTest)
                .organizationName("Noon")
                .taxNumber(356987412L)
                .build();

        Product product = Product.builder()
                .name("Earbuds1")
                .description("noise cancellation")
                .category("Electronics")
                .price(1290.00)
                .quantity(15)
                .rating(4.5)
                .vendor(vendor)
                .build();

        productTest.add(product);

        product = Product.builder()
                .name("Earbuds2")
                .description("noise cancellation")
                .category("Electronics")
                .price(496.00)
                .quantity(10)
                .rating(3.5)
                .vendor(vendor)
                .build();

        productTest.add(product);
        Mockito.when(searchService.searchWithKey("Ear")).thenReturn(productTest);
        mockMvc.perform(get("/Search/Ear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Earbuds1"))
                .andExpect(jsonPath("$[1].name").value("Earbuds2"))
                .andExpect(jsonPath("$[0].vendor.account.username").value("noon"))
                .andExpect(jsonPath("$[1].vendor.account.username").value("noon"))
                .andExpect(jsonPath("$[0].vendor.organizationName").value("Noon"))
                .andExpect(jsonPath("$[1].vendor.organizationName").value("Noon"))
                .andExpect(jsonPath("$[0].vendor.taxNumber").value("356987412"))
                .andExpect(jsonPath("$[1].vendor.taxNumber").value("356987412"));

    }

    @Test
    void testSearchNotExist() throws Exception {
        List<Product> productTest = new ArrayList<>();
        Mockito.when(searchService.searchWithKey("Earbuds")).thenReturn(productTest);
        mockMvc.perform(get("/Search/Earbuds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }
}
