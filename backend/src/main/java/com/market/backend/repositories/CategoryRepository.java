package com.market.backend.repositories;


import com.market.backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query("SELECT c.categoryName FROM Category c")
    List<String> findAllCategoryNames();
    @Override
    List<Category> findAll();
    Category findByCategoryName(String name);
    void deleteByCategoryName(String name);
  Optional<Category> findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);

}
