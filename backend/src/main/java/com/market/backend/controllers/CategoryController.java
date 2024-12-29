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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.market.backend.models.Category;
import com.market.backend.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 1. Get all categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    // 2. Get category by name
    @GetMapping("/{categoryName}")
    public ResponseEntity<Category> getCategory(@PathVariable String categoryName) {
        Optional<Category> result = categoryService.getCategory(categoryName);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Add a new category
    @PostMapping
    public ResponseEntity<Object> addCategory(@RequestParam("name") String CategoryName,
            @RequestParam("image") MultipartFile CategoryImage) {
        return categoryService.addCategory(CategoryName, CategoryImage);
    }

    // 4. Update an existing category
    @PutMapping("/{categoryName}")
    public ResponseEntity<String> updateCategory(
            @PathVariable String categoryName,
            @RequestParam String newName) {

                System.out.println("Category name: " + categoryName+ " New name: " + newName);

        return categoryService.updateCategory(categoryName, newName);
    }

    // 5. Delete a category
    @DeleteMapping("/{categoryName}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryName) {
        return categoryService.deleteCategory(categoryName);
    }

}
