package com.market.backend.controllers;

import com.market.backend.models.Category;
import com.market.backend.models.Product;
import com.market.backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/Home")
    List<Category> homeCategories(){
        return categoryService.listAllCategories();
    }

    @GetMapping("/Category/{name}")
    List<Product> categoryProducts(@PathVariable String name){
        return categoryService.listCategoriesProducts(name);
    }
}
