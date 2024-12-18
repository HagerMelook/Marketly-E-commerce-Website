package com.market.backend.services;

import com.market.backend.models.Product;
import com.market.backend.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    SearchService searchService;

    @Test
    public void searchByExistingKeyword_ReturnsProductsContainTheWord(){
        List<Product> mockAllProducts = List.of(
                Product.builder()
                        .name("Smart Watch").description("Provides pulse tracing, ...")
                        .build(),
                Product.builder()
                        .name("Samsung Curved Screen").description("Smart Screen interaction")
                        .build(),
                Product.builder()
                        .name("Real Madrid 24/25 Shirt").description("Real madrid first kit")
                        .build()
        );
        when(productRepository.findAll()).thenReturn(mockAllProducts);

        List<Product> productsContainWordSmart = searchService.searchWithKey("Smart");
        assertEquals(2, productsContainWordSmart.size());
        productsContainWordSmart.forEach(product -> {
            assertTrue(product.getDescription().contains("Smart")
            || product.getName().contains("Smart"));
        });
    }

    @Test
    public void searchByAbsentKeyword_ReturnsEmptyList(){
        List<Product> mockAllProducts = List.of(
                Product.builder()
                        .name("Smart Watch").description("Provides pulse tracing, ...")
                        .build(),
                Product.builder()
                        .name("Samsung Curved Screen").description("Smart Screen interaction")
                        .build(),
                Product.builder()
                        .name("Real Madrid 24/25 Shirt").description("Real madrid first kit")
                        .build()
        );
        when(productRepository.findAll()).thenReturn(mockAllProducts);

        List<Product> productsContainWordApple = searchService.searchWithKey("Apple");
        assertEquals(0, productsContainWordApple.size());
    }

    @Test
    public void searchByExistingKeyword_CheckRepetition_ReturnsNoDuplicates(){
//        As the search mechanism searches the name and the description of the product,
//        if a product matches the keyword in both the name and description,
//        the product must be returned once, no duplication
        List<Product> mockAllProducts = List.of(
                Product.builder()
                        .name("Smart Watch").description("Smart Apple Watch provides pulse tracing")
                        .build(),
                Product.builder()
                        .name("Samsung Smart Curved Screen").description("Smart Screen interaction")
                        .build(),
                Product.builder()
                        .name("Real Madrid 24/25 Shirt").description("Real madrid first kit")
                        .build()
        );
        when(productRepository.findAll()).thenReturn(mockAllProducts);

        List<Product> productsContainWordSmart = searchService.searchWithKey("Smart");
        assertEquals(2, productsContainWordSmart.size());
        assertNotEquals(4, productsContainWordSmart.size());
    }

    @Test
    public void searchByExistingKeyword_CheckCaseSensitivity_ReturnsAllMatchesIgnoringCase(){
//        Search mechanism must handle case sensitivity cases, no matter the case entered.
        List<Product> mockAllProducts = List.of(
                Product.builder()
                        .name("SMART Watch").description("SMART Apple Watch provides pulse tracing")
                        .build(),
                Product.builder()
                        .name("Samsung smart Curved Screen").description("smart Screen interaction")
                        .build(),
                Product.builder()
                        .name("Real Madrid 24/25 Shirt").description("Real madrid first kit")
                        .build()
        );
        when(productRepository.findAll()).thenReturn(mockAllProducts);

        List<Product> productsContainWordSmart = searchService.searchWithKey("SmArT");
        assertEquals(2, productsContainWordSmart.size());
        assertTrue(productsContainWordSmart.get(0).getName().equalsIgnoreCase("smart watch"));
        assertTrue(productsContainWordSmart.get(1).getName().equalsIgnoreCase("Samsung smart Curved Screen"));
    }

    @Test
    public void searchByExistingKeyword_MoreThanOneWord_ReturnsAllMatchesAnyWord(){
//        Search mechanism must split keyword when there are spaces and search for each word alone.
        List<Product> mockAllProducts = List.of(
                Product.builder()
                        .name("Apple Watch").description("Apple Watch provides pulse tracing")
                        .build(),
                Product.builder()
                        .name("Samsung Curved Screen").description("smart Screen interaction")
                        .build()
        );
        when(productRepository.findAll()).thenReturn(mockAllProducts);

        List<Product> productsContainWordOrApple = searchService.searchWithKey("Smart Apple");
        assertEquals(2, productsContainWordOrApple.size());
        assertTrue(productsContainWordOrApple.get(0).getName().equalsIgnoreCase("apple watch"));
        assertTrue(productsContainWordOrApple.get(1).getName().equalsIgnoreCase("Samsung Curved Screen"));
    }

    @Test
    public void searchByExistingKeyword_ProductMatchesMoreThanOneWord_ReturnsProductOnce(){
//        Search mechanism must not return a product more than once if two words
//        from the search key exist in the name or/and the description
        List<Product> mockAllProducts = List.of(
                Product.builder()
                        .name("Apple Watch").description("Apple Watch provides pulse tracing")
                        .build(),
                Product.builder()
                        .name("Samsung Curved Screen").description("smart Screen interaction")
                        .build()
        );
        when(productRepository.findAll()).thenReturn(mockAllProducts);

//        Both "apple" and "pulse" are found in the above product, so it must be returned only once.
        List<Product> productsContainAppleOrPulse = searchService.searchWithKey("Apple pulse");
        assertEquals(1, productsContainAppleOrPulse.size());
        assertNotEquals(2, productsContainAppleOrPulse.size());
    }
}