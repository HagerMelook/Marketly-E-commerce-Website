package com.market.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.market.backend.models.Category;

public interface CategoryRepository extends JpaRepository<Category, String>  {
    
}
