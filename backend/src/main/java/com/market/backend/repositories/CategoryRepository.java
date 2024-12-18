package com.market.backend.repositories;

import com.market.backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query("SELECT c.categoryName FROM Category c")
    List<String> findAllCategoryNames();
}

