package com.market.backend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import com.market.backend.models.Product;
import com.market.backend.repositories.CategoryRepository;
import com.market.backend.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.market.backend.models.Category;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> listAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Product> listCategoriesProducts(String categoryName) {
        return productRepository.findByCategory(categoryName);
    }

    @Transactional
    public ResponseEntity<String> updateCategory(String categoryName, String newName) {
        Category existingCategory = categoryRepository.findByCategoryName(categoryName);
        
        if (existingCategory != null) {
            // Check if new name already exists and it's not the same category
            if (!categoryName.equals(newName)) {
                Category newCategory = new Category();
                newCategory.setCategoryName(newName);
                newCategory.setCategoryImagePath(existingCategory.getCategoryImagePath());
                categoryRepository.save(newCategory);
                categoryRepository.deleteById(categoryName);
                return ResponseEntity.ok("Category updated successfully");
            }
            
            return ResponseEntity.badRequest().body("Category with new name already exists");
            
        }
        
        return ResponseEntity.notFound().build();
    }
    
    public Optional<Category> getCategory(String CategoryName) {
        Optional<Category> category = categoryRepository.findById(CategoryName);

        if (category.isPresent())
            return category;

        return Optional.empty();
    }

    @Transactional
    public ResponseEntity<Object> addCategory(String CategoryName, MultipartFile CategoryImage) {
        if (categoryRepository.existsById(CategoryName)) {
            return ResponseEntity.badRequest().build();
        }

        Category category = new Category();
        category.setCategoryName(CategoryName);

        try {
            String imagePath = saveCategoryImage(CategoryImage);
            category.setCategoryImagePath(imagePath);
        } catch (IOException e) {
            category.setCategoryImagePath("default-category.png");
        }

        categoryRepository.save(category);
        return ResponseEntity.ok().body(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public String saveCategoryImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return "default-category.png";
        }

        Path path = Paths.get("images"); // Changed from src/main/resources/static/images
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path imagePath = path.resolve(fileName);
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // Return only the filename
    }

    public ResponseEntity<Void> deleteCategory(String categoryName) {
        if (!categoryRepository.existsById(categoryName)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(categoryName);
        return ResponseEntity.noContent().build();

    }
}
