package com.market.backend.services;

import com.market.backend.dtos.ProductDTO;
import com.market.backend.models.Product;
import com.market.backend.models.Vendor;
import com.market.backend.repositories.CategoryRepository;
import com.market.backend.repositories.ProductImageRepository;
import com.market.backend.repositories.ProductRepository;
import com.market.backend.repositories.VendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceTest {
    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @InjectMocks
    private VendorService vendorService;

    @Test
    void testGetAllCategories() {
        List<String> categories = Arrays.asList("Fashion", "Sports", "Electronics");
        when(categoryRepository.findAllCategoryNames()).thenReturn(categories);

        List<String> actualCategories = vendorService.getAllCategories();

        verify(categoryRepository, times(1)).findAllCategoryNames();

        assertEquals(categories, actualCategories);
    }

    @Test
    void testCreateNewProductSuccess() {
        Long vendorId = 1L;
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);

        ProductDTO productDTO = ProductDTO.builder()
                .vendorId(vendorId)
                .name("Test Product")
                .description("A test product description")
                .quantity(10)
                .price(99.99)
                .category("Electronics")
                .images(Collections.emptyList())
                .build();

        when(vendorRepository.findById(vendorId)).thenReturn(Optional.of(vendor));

        assertDoesNotThrow(() -> vendorService.createNewProduct(productDTO));

        verify(vendorRepository, times(1)).findById(vendorId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateNewProductVendorNotFound() {
        Long vendorId = 1L;
        ProductDTO productDTO = ProductDTO.builder()
                .vendorId(vendorId)
                .name("Test Product")
                .description("A test product description")
                .quantity(10)
                .price(99.99)
                .category("Electronics")
                .images(Collections.emptyList())
                .build();

        when(vendorRepository.findById(vendorId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> vendorService.createNewProduct(productDTO));
        assertEquals("Vendor not found", exception.getMessage());

        verify(vendorRepository, times(1)).findById(vendorId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProductSuccess() {
        Long productId = 1L;

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(50.0);
        existingProduct.setCategory("Old Category");
        existingProduct.setQuantity(5);

        ProductDTO updatedProductDTO = ProductDTO.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(99.99)
                .category("Updated Category")
                .quantity(10)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        assertDoesNotThrow(() -> vendorService.updateProduct(updatedProductDTO, productId));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);

        assertEquals("Updated Product", existingProduct.getName());
        assertEquals("Updated Description", existingProduct.getDescription());
        assertEquals(99.99, existingProduct.getPrice());
        assertEquals("Updated Category", existingProduct.getCategory());
        assertEquals(10, existingProduct.getQuantity());
    }

    @Test
    void testUpdateProductNotFound() {
        Long productId = 1L;

        ProductDTO updatedProductDTO = ProductDTO.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(99.99)
                .category("Updated Category")
                .quantity(10)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> vendorService.updateProduct(updatedProductDTO, productId));
        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }
}
