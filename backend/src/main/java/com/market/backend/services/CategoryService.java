package com.market.backend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.market.backend.dtos.CategoryDTO;
import com.market.backend.models.Category;
import com.market.backend.repositories.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<String> updateCategory(String categoryName, CategoryDTO categoryDTO) throws IOException {
        // Retrieve the existing category from the database
        Optional<Category> existingCategory = categoryRepository.findById(categoryName);
        
        // If the category exists, update its details
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            
            // Update category name
            category.setCategoryName(categoryDTO.getCategoryName());
            
            // Update image path if an image is provided
            if (categoryDTO.getImages() != null) {
                // Save the image (you may need to implement this logic depending on where you want to store the image)
                String imagePath = saveCategoryImage(categoryDTO.getImages()); // Implement this method to return the image path
                category.setCategoryImagePath(imagePath);
            }
    
            // Save the updated category
            categoryRepository.save(category);
    
            // Return a success response
            return ResponseEntity.ok().body("Category updated successfully");
        }
    
        // Return a 404 if the category is not found
        return ResponseEntity.notFound().build();
    }
    
    public Optional<Category> getCategory(String CategoryName) {
        Optional<Category> category = categoryRepository.findById(CategoryName);

        if (category.isPresent())
            return category;

        return Optional.empty();
    }

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
