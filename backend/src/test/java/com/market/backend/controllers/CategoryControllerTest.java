package com.market.backend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.market.backend.models.Account;
import com.market.backend.models.Category;
import com.market.backend.models.Product;
import com.market.backend.models.Vendor;
import com.market.backend.services.CategoryService;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private CategoryService categoryService;

    @Test
    void testHomeCategorieswithElements() throws Exception {
        List<Category> categoryTest = new ArrayList<>();

        Category category = Category.builder()
                .categoryName("Electronics")
                .categoryImagePath("D:\\subjects\\SWE\\milestone1\\marketly\\frontend\\src\\assets\\p1.jpg")
                .build();

        categoryTest.add(category);
        category = Category.builder()
                .categoryName("Furniture")
                .categoryImagePath("D:\\subjects\\SWE\\milestone1\\marketly\\frontend\\src\\assets\\p2.jpg")
                .build();

        categoryTest.add(category);
        Mockito.when(categoryService.listAllCategories()).thenReturn(categoryTest);
        mockMvc.perform(get("/Home"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value("Electronics"))
                .andExpect(jsonPath("$[1].categoryName").value("Furniture"))
                .andExpect(jsonPath("$[0].categoryImagePath")
                        .value("D:\\subjects\\SWE\\milestone1\\marketly\\frontend\\src\\assets\\p1.jpg"))
                .andExpect(jsonPath("$[1].categoryImagePath")
                        .value("D:\\subjects\\SWE\\milestone1\\marketly\\frontend\\src\\assets\\p2.jpg"));
    }

    @Test
    void testHomeCategorieswithEmptyList() throws Exception {
        List<Category> categoryTest = new ArrayList<>();
        Mockito.when(categoryService.listAllCategories()).thenReturn(categoryTest);
        mockMvc.perform(get("/Home"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testCategoryProductsExist() throws Exception {
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
                .name("Earbuds")
                .description("noise cancelation")
                .category("Electronics")
                .price(1290.00)
                .quantity(15)
                .rating(4.5)
                .vendor(vendor)
                .build();

        productTest.add(product);

        product = Product.builder()
                .name("Flash Drive")
                .description("Flash Drive with 8GB")
                .category("Electronics")
                .price(496.00)
                .quantity(10)
                .rating(3.5)
                .vendor(vendor)
                .build();

        productTest.add(product);
        Mockito.when(categoryService.listCategoriesProducts("Electronics")).thenReturn(productTest);
        mockMvc.perform(get("/Category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Earbuds"))
                .andExpect(jsonPath("$[1].name").value("Flash Drive"))
                .andExpect(jsonPath("$[0].vendor.account.username").value("noon"))
                .andExpect(jsonPath("$[1].vendor.account.username").value("noon"))
                .andExpect(jsonPath("$[0].vendor.organizationName").value("Noon"))
                .andExpect(jsonPath("$[1].vendor.organizationName").value("Noon"))
                .andExpect(jsonPath("$[0].vendor.taxNumber").value("356987412"))
                .andExpect(jsonPath("$[1].vendor.taxNumber").value("356987412"));
    }

    @Test
    void testCategoryProductsNotExist() throws Exception {
        List<Product> productTest = new ArrayList<>();
        Mockito.when(categoryService.listCategoriesProducts("Furniture")).thenReturn(productTest);
        mockMvc.perform(get("/Category/Furniture"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}