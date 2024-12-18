package com.market.backend.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market.backend.models.Category;
import com.market.backend.repositories.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. Get all categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 2. Get category by name
    @GetMapping("/{categoryName}")
    public ResponseEntity<Category> getCategory(@PathVariable String categoryName) {
        Optional<Category> category = categoryRepository.findById(categoryName);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        }
        return ResponseEntity.notFound().build();
    }

    // 3. Add a new category
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        // Ensure category name is unique
        if (categoryRepository.existsById(category.getCategoryName())) {
            return ResponseEntity.badRequest().build(); // Category with this name already exists
        }
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    // 4. Update an existing category
    @PutMapping("/{categoryName}")
    public ResponseEntity<Category> updateCategory(@PathVariable String categoryName, @RequestBody Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(categoryName);
        if (!existingCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Update the category
        category.setCategoryName(categoryName);  // Set the categoryName as it's used as the primary key
        Category updatedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    // 5. Delete a category
    @DeleteMapping("/{categoryName}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryName) {
        if (!categoryRepository.existsById(categoryName)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(categoryName);
        return ResponseEntity.noContent().build();
    }
}
