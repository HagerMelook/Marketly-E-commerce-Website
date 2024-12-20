package com.market.backend.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.market.backend.models.Category;
import com.market.backend.repositories.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class CategoryController {
   
    private static final String IMAGE_UPLOAD_DIR = System.getProperty("user.dir") + "/marketly/backend/src/main/resources";

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
        //delete old category
        categoryRepository.deleteById(categoryName);
        return addCategory(category);
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

    @PostMapping("/upload-image")
public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image,
                                     @RequestParam("imagePath") String imagePath) {
    try {
        // Save the image to the specified directory
        File uploadDir = new File(IMAGE_UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the image with the category name as the filename
        Path path = new File(IMAGE_UPLOAD_DIR, imagePath).toPath();
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok().body("Image uploaded successfully");

    } catch (IOException e) {
        return ResponseEntity.status(500).body("Failed to upload image");
    }
}


}
