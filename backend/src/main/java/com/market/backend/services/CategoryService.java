package com.market.backend.services;

import com.market.backend.models.Category;
import com.market.backend.models.Product;
import com.market.backend.repositories.CategoryRepository;
import com.market.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    public List<Category> listAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Product> listCategoriesProducts(String categoryName) {
        return productRepository.findByCategory(categoryName);
    }
}
