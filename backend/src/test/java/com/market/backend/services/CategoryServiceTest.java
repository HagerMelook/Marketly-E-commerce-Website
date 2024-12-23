package com.market.backend.services;

import com.market.backend.models.Category;
import com.market.backend.models.Product;
import com.market.backend.repositories.CategoryRepository;
import com.market.backend.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    public void listAllCategories_CorrectAssertion_ReturnsAllCategories() {
        List<Category> mockCategoriess = List.of(
                new Category("Sports", "./sportsImg.png"),
                new Category("Food", "./foodImg.png")
        );
        Mockito.when(categoryRepository.findAll()).thenReturn(mockCategoriess);

        List<Category> returnedCategories = categoryService.listAllCategories();
        assertEquals(2, returnedCategories.size());
        assertEquals("Sports", returnedCategories.get(0).getCategoryName());
        assertEquals("Food", returnedCategories.get(1).getCategoryName());
    }

    @Test
    public void listAllCategories_WrongAssertion_ReturnsDifferentCategories() {
        List<Category> mockCategoriess = List.of(
                new Category("Sports", "./sportsImg.png"),
                new Category("Food", "./foodImg.png")
        );
        Mockito.when(categoryRepository.findAll()).thenReturn(mockCategoriess);

        List<Category> returnedCategories = categoryService.listAllCategories();
        assertNotEquals(1, returnedCategories.size());
        assertNotEquals("Clothes", returnedCategories.get(0).getCategoryName());
        assertNotEquals("Electronics", returnedCategories.get(1).getCategoryName());
    }

    @Test
    public void listAllCategoryProducts_CorrectAssertion_ReturnsAllCategoryProducts() {
        List<Product> mockProducts = List.of(
                Product.builder().id(1L).category("Electronics").build(),
                Product.builder().id(5L).category("Electronics").build(),
                Product.builder().id(6L).category("Electronics").build()
        );
        Mockito.when(productRepository.findByCategory("Electronics")).thenReturn(mockProducts);

        List<Product> returnedProducts = categoryService.listCategoriesProducts("Electronics");
        assertEquals(3, returnedProducts.size());
        assertEquals("Electronics", returnedProducts.get(0).getCategory());
        assertEquals("Electronics", returnedProducts.get(1).getCategory());
        assertEquals("Electronics", returnedProducts.get(2).getCategory());
    }

    @Test
    public void listAllCategoryProducts_WrongAssertion_ReturnsDifferentProducts() {
        List<Product> mockProducts = List.of(
                Product.builder().id(1L).category("Electronics").build(),
                Product.builder().id(5L).category("Electronics").build()
        );
        Mockito.when(productRepository.findByCategory("Electronics")).thenReturn(mockProducts);

        List<Product> returnedProducts = categoryService.listCategoriesProducts("Electronics");
        assertNotEquals(3, returnedProducts.size());
        assertNotEquals("Books", returnedProducts.get(0).getCategory());
        assertNotEquals("Sports", returnedProducts.get(1).getCategory());
    }
}
