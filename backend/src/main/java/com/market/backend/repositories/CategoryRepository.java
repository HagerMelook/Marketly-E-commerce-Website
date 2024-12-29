package com.market.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.market.backend.models.Category;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);
}
