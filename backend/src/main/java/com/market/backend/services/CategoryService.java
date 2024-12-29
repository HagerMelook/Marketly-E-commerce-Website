package com.market.backend.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public ResponseEntity<String> updateCategory(String categoryName, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(categoryName);
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setCategoryName(categoryDTO.getCategoryName());
            category.setCategoryImagePath(categoryDTO.getCategoryImagePath());
            categoryRepository.save(category);
            if (categoryDTO.getImages() != null)
                try {
                    saveCategoryImage(categoryDTO.getImages());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            return ResponseEntity.ok().body("Category updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    public Optional<Category> getCategory(String CategoryName) {
        Optional<Category> category = categoryRepository.findById(CategoryName);

        if (category.isPresent())
            return category;

        return Optional.empty();
    }

    public ResponseEntity<Object> addCategory(String CategoryName, MultipartFile CategoryImage) {
        // Ensure category name is unique
        if (categoryRepository.existsById(CategoryName))
            return ResponseEntity.badRequest().build(); // Category with this name already exists

        Category category = new Category();

        try {
            category.setCategoryImagePath(saveCategoryImage(CategoryImage));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving category image");
        }

        category.setCategoryName(CategoryName);
        categoryRepository.save(category);

        return ResponseEntity.ok().body("category saved successfully");
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public String saveCategoryImage(MultipartFile image) throws IOException {
        Path path = Paths.get("src/main/resources/static/images/");
        Path absolutePath = path.toAbsolutePath();

        File directory = new File(absolutePath.toString());

        if (!directory.exists())
            directory.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path imagePath = absolutePath.resolve(fileName);
        image.transferTo(imagePath.toFile());

        return imagePath.toString();
    }

    public ResponseEntity<Void> deleteCategory(String categoryName) {
        if (!categoryRepository.existsById(categoryName)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(categoryName);
        return ResponseEntity.noContent().build();
    }
}
