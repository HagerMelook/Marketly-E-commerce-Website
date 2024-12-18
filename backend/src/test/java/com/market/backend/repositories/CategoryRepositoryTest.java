package com.market.backend.repositories;

import com.market.backend.models.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTest {

    CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Test
    void findAll_ReturnsAllCategories() {
        Category category1 = Category.builder()
                .categoryName("Electronics").
                categoryImagePath("../path1")
                .build();
        Category category2 = Category.builder()
                .categoryName("Sports").
                categoryImagePath("../path2")
                .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories.size()).isEqualTo(2);
        categories.forEach(category -> {
            assertThat(category).isNotNull();
            assertThat(category.getCategoryName()).isNotNull();
            assertThat(category.getCategoryImagePath()).isNotNull();
        });
    }

    @Test
    void findAll_EmptyDB_ReturnsEmptyList() {
        List<Category> categories = categoryRepository.findAll();
        assertTrue(categories.isEmpty());
    }

    @Test
    void findByName_CorrectName_ReturnsOneCategory() {
        Category category1 = Category.builder()
                .categoryName("Electronics").
                categoryImagePath("../path1")
                .build();
        Category category2 = Category.builder()
                .categoryName("Sports").
                categoryImagePath("../path2")
                .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        Category electronicsCategory = categoryRepository.findByCategoryName("Electronics");
        assertThat(electronicsCategory).isNotNull();
        assertThat(electronicsCategory.getCategoryName()).isNotNull();
        assertThat(electronicsCategory.getCategoryImagePath()).isNotNull();
    }

    @Test
    void findByName_NoCaseSensitivity_ReturnsNull() {
        Category category1 = Category.builder()
                .categoryName("Electronics").
                categoryImagePath("../path1")
                .build();
        Category category2 = Category.builder()
                .categoryName("Sports").
                categoryImagePath("../path2")
                .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        Category electronicsCategory = categoryRepository.findByCategoryName("sports");
//        Sports != sports
        assertThat(electronicsCategory).isNull();
    }

    @Test
    void findByName_AbsentName_ReturnsNull() {
        Category category1 = Category.builder()
                .categoryName("Electronics").
                categoryImagePath("../path1")
                .build();
        Category category2 = Category.builder()
                .categoryName("Sports").
                categoryImagePath("../path2")
                .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        Category electronicsCategory = categoryRepository.findByCategoryName("Clothes");
        assertThat(electronicsCategory).isNull();
    }

    @Test
    void deleteByName_CorrectName_ReturnsDBWithRemovedCategory() {
        Category category1 = Category.builder()
                .categoryName("Electronics").
                categoryImagePath("../path1")
                .build();
        Category category2 = Category.builder()
                .categoryName("Sports").
                categoryImagePath("../path2")
                .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        categoryRepository.delete(category1);

        Category electronicsCategory = categoryRepository.findByCategoryName("Electronics");
        assertThat(electronicsCategory).isNull();
    }

    @Test
    void deleteAll_ReturnsEmptyDB() {
        Category category1 = Category.builder()
                .categoryName("Electronics").
                categoryImagePath("../path1")
                .build();
        Category category2 = Category.builder()
                .categoryName("Sports").
                categoryImagePath("../path2")
                .build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        categoryRepository.deleteAll();

        List<Category> categories = categoryRepository.findAll();
        assertTrue(categories.isEmpty());
    }
}
