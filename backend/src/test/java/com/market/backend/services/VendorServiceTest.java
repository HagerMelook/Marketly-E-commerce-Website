package com.market.backend.services;

import com.market.backend.dtos.ProductDTO;
import com.market.backend.models.Product;
import com.market.backend.models.ProductImage;
import com.market.backend.models.Vendor;
import com.market.backend.repositories.CategoryRepository;
import com.market.backend.repositories.ProductImageRepository;
import com.market.backend.repositories.ProductRepository;
import com.market.backend.repositories.VendorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Test
    public void testGetAllVendorProducts() {
        long vendorId = 1L;

        Product product1 = new Product(1L, Vendor.builder().id(vendorId).build(), "Product1", "Description1", 10.0, 5, 10, "Category1");
        Product product2 = new Product(2L, Vendor.builder().id(vendorId).build(), "Product2", "Description2", 16.0, 4, 15, "Category2");
        List<Product> products = Arrays.asList(product1, product2);
        List<String> images1 = Arrays.asList("img1.jpg", "img2.jpg");
        List<String> images2 = Arrays.asList("img3.jpg");

        Mockito.when(productRepository.findByVendorId(vendorId)).thenReturn(products);
        Mockito.when(productImageRepository.findByProductId(1L)).thenReturn(images1);
        Mockito.when(productImageRepository.findByProductId(2L)).thenReturn(images2);

        List<ProductDTO> result = vendorService.getAllVendorProducts(vendorId);
        Assertions.assertEquals(2, result.size());


        ProductDTO productDTO1 = result.get(0);
        Assertions.assertEquals(1L, productDTO1.getId());
        Assertions.assertEquals(vendorId, productDTO1.getVendorId());
        Assertions.assertEquals("Product1", productDTO1.getName());
        Assertions.assertEquals("Description1", productDTO1.getDescription());
        Assertions.assertEquals(10.0, productDTO1.getPrice());
        Assertions.assertEquals(5, productDTO1.getRating());
        Assertions.assertEquals(10, productDTO1.getQuantity());
        Assertions.assertEquals("Category1", productDTO1.getCategory());
        Assertions.assertEquals(images1, productDTO1.getImagePaths());

        ProductDTO productDTO2 = result.get(1);
        Assertions.assertEquals(2L, productDTO2.getId());
        Assertions.assertEquals(vendorId, productDTO2.getVendorId());
        Assertions.assertEquals("Product2", productDTO2.getName());
        Assertions.assertEquals("Description2", productDTO2.getDescription());
        Assertions.assertEquals(16.0, productDTO2.getPrice());
        Assertions.assertEquals(4, productDTO2.getRating());
        Assertions.assertEquals(15, productDTO2.getQuantity());
        Assertions.assertEquals("Category2", productDTO2.getCategory());
        Assertions.assertEquals(images2, productDTO2.getImagePaths());
    }

    @Test
    public void testGetProductByName() {
        long vendorId = 1L;
        String name = "Product";

        Product product = new Product(1L, Vendor.builder().id(vendorId).build(), "Product1", "Description1", 10.0, 5, 10, "Category1");
        List<Product> products = Collections.singletonList(product);

        List<String> images = Arrays.asList("img1.jpg");

        Mockito.when(productRepository.findByVendorIdAndNameContaining(vendorId, name)).thenReturn(products);
        Mockito.when(productImageRepository.findByProductId(1L)).thenReturn(images);

        List<ProductDTO> result = vendorService.getProductByName(vendorId, name);

        Assertions.assertEquals(1, result.size());

        ProductDTO productDTO = result.get(0);
        Assertions.assertEquals(1L, productDTO.getId());
        Assertions.assertEquals(vendorId, productDTO.getVendorId());
        Assertions.assertEquals("Product1", productDTO.getName());
        Assertions.assertEquals("Description1", productDTO.getDescription());
        Assertions.assertEquals(10.0, productDTO.getPrice());
        Assertions.assertEquals(5, productDTO.getRating());
        Assertions.assertEquals(10, productDTO.getQuantity());
        Assertions.assertEquals("Category1", productDTO.getCategory());
        Assertions.assertEquals(images, productDTO.getImagePaths());
    }



}



