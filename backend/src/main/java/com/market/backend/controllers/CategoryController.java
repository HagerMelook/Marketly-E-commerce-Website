package com.market.backend.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.market.backend.dtos.CategoryDTO;
import com.market.backend.models.Category;
import com.market.backend.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final String IMAGE_UPLOAD_DIR = System.getProperty("user.dir")
            + "/marketly/backend/src/main/resources";

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

        if (result.isPresent())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get());
    }

    // 3. Add a new category
    @PostMapping
    public ResponseEntity<Object> addCategory(@RequestParam("name") String CategoryName,
            @RequestParam("image") MultipartFile CategoryImage) {
        return categoryService.addCategory(CategoryName, CategoryImage);
    }

    // 4. Update an existing category
    @PutMapping("/{categoryName}")
    public ResponseEntity<String> updateCategory(@PathVariable String categoryName,
            @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryName, categoryDTO);
    }

    // 5. Delete a category
    @DeleteMapping("/{categoryName}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryName) {
        return categoryService.deleteCategory(categoryName);
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
