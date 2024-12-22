package com.market.backend.controllers;

import com.market.backend.dtos.ProductDTO;
import com.market.backend.models.Product;
import com.market.backend.services.VendorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@WebMvcTest(VendorController.class)
@AutoConfigureMockMvc(addFilters = false)
class VendorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private VendorService vendorService;

    @Test
    void testGetCategories() throws Exception {
        List<String> categories = Arrays.asList("Electronics", "Food", "Fashion", "Sports");

        when(vendorService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/vendor/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(categories.size())))
                .andExpect(jsonPath("$[0]").value(categories.get(0)))
                .andExpect(jsonPath("$[1]").value(categories.get(1)))
                .andExpect(jsonPath("$[2]").value(categories.get(2)))
                .andExpect(jsonPath("$[3]").value(categories.get(3)));

        verify(vendorService, times(1)).getAllCategories();
    }

    @Test
    void testCreateProductSuccess() throws Exception {
        doNothing().when(vendorService).createNewProduct(any(ProductDTO.class));

        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/vendor/add-product")
                        .file(image)
                        .param("vendorId", "1")
                        .param("name", "Laptop")
                        .param("description", "A high-end laptop")
                        .param("quantity", "10")
                        .param("price", "1200.50")
                        .param("category", "Electronics")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().string("Product created successfully"));

        verify(vendorService, times(1)).createNewProduct(any(ProductDTO.class));
    }

    @Test
    void testGetCategoriesEmptyList() throws Exception {
        when(vendorService.getAllCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/vendor/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(vendorService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoriesLargeList() throws Exception {
        List<String> categories = IntStream.range(0, 1000)
                .mapToObj(i -> "Category" + i)
                .collect(Collectors.toList());

        when(vendorService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/vendor/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1000)));

        verify(vendorService, times(1)).getAllCategories();
    }



    @Test
    void testCreateProductIOException() throws Exception {
        doThrow(new IOException("Failed to save product image"))
                .when(vendorService).createNewProduct(any(ProductDTO.class));

        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/vendor/add-product")
                        .file(image)
                        .param("vendorId", "1")
                        .param("name", "Laptop")
                        .param("description", "A high-end laptop")
                        .param("quantity", "10")
                        .param("price", "1200.50")
                        .param("category", "Electronics")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error uploading images: Failed to save product image"));

        verify(vendorService, times(1)).createNewProduct(any(ProductDTO.class));
    }

    @Test
    void testCreateProductNoSuchElementException() throws Exception {
        doThrow(new NoSuchElementException("Vendor not found"))
                .when(vendorService).createNewProduct(any(ProductDTO.class));

        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/vendor/add-product")
                        .file(image)
                        .param("vendorId", "1")
                        .param("name", "Laptop")
                        .param("description", "A high-end laptop")
                        .param("quantity", "10")
                        .param("price", "1200.50")
                        .param("category", "Electronics")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Vendor not found"));

        verify(vendorService, times(1)).createNewProduct(any(ProductDTO.class));
    }

    @Test
    void testUpdateProductSuccess() throws Exception {
        Long id = 1L;

        doNothing().when(vendorService).updateProduct(any(ProductDTO.class), eq(id));

        mockMvc.perform(put("/vendor/update-product")
                        .param("id", String.valueOf(id))
                        .param("name", "Updated Name")
                        .param("description", "Updated Description")
                        .param("quantity", "10")
                        .param("price", "99.99")
                        .param("category", "Updated Category"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully"));

        verify(vendorService, times(1)).updateProduct(any(ProductDTO.class), eq(id));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        Long id = 1L;

        doThrow(new NoSuchElementException("Product not found"))
                .when(vendorService)
                .updateProduct(any(ProductDTO.class), eq(id));

        mockMvc.perform(put("/vendor/update-product")
                        .param("id", String.valueOf(id))
                        .param("name", "Updated Name")
                        .param("description", "Updated Description")
                        .param("quantity", "10")
                        .param("price", "99.99")
                        .param("category", "Updated Category"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));

        verify(vendorService, times(1)).updateProduct(any(ProductDTO.class), eq(id));
    }

//    @Test
//    public void testGetVendorProducts() throws Exception {
//        long vendorId = 1L;
//
//        ProductDTO productDTO = ProductDTO.builder()
//                .id(1L)
//                .vendorId(vendorId)
//                .name("Product1")
//                .description("Description1")
//                .price(10.0)
//                .rating(5)
//                .quantity(10)
//                .category("Category1")
//                .imagePaths(Arrays.asList("img1.jpg"))
//                .build();
//
//        Mockito.when(vendorService.getAllVendorProducts(vendorId))
//                .thenReturn(Collections.singletonList(productDTO));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/products/{vendorId}", vendorId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].name").value("Product1"))
//                .andExpect(jsonPath("$[0].imagePaths[0]").value("img1.jpg"));
//    }
//
//
//    @Test
//    public void testDeleteVendorProduct() throws Exception {
//        long productId = 1L;
//
//        Mockito.doNothing().when(vendorService).deleteProduct(productId);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/delete/{id}", productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
//
//        Mockito.verify(vendorService).deleteProduct(productId);
//    }
//
//    @Test
//    public void testDeleteVendorProductWithException() throws Exception {
//        long productId = 1L;
//
//        Mockito.doThrow(new IllegalArgumentException("Product not found")).when(vendorService).deleteProduct(productId);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/delete/{id}", productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Product not found"));
//
//        Mockito.verify(vendorService).deleteProduct(productId);
//    }
//
//    @Test
//    public void testSearchForProduct() throws Exception {
//        long vendorId = 1L;
//        String name = "Product";
//
//        ProductDTO productDTO = ProductDTO.builder()
//                .id(1L)
//                .vendorId(vendorId)
//                .name("Product1")
//                .description("Description1")
//                .price(10.0)
//                .rating(5)
//                .quantity(10)
//                .category("Category1")
//                .imagePaths(Arrays.asList("img1.jpg"))
//                .build();
//
//        Mockito.when(vendorService.getProductByName(vendorId, name))
//                .thenReturn(Collections.singletonList(productDTO));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/search/{vendorId}/{name}", vendorId, name)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].name").value("Product1"))
//                .andExpect(jsonPath("$[0].imagePaths[0]").value("img1.jpg"));
//    }
}

