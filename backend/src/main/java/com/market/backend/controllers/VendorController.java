package com.market.backend.controllers;

import com.market.backend.dtos.ProductDTO;
import com.market.backend.dtos.VendorProductDTO;
import com.market.backend.models.Product;
import com.market.backend.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/vendor")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(vendorService.getAllCategories());
    }

    @PostMapping("/add-product")
    public ResponseEntity<String> createProduct(
            @RequestParam("vendorId") Long vendorId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") double price,
            @RequestParam("category") String category,
            @RequestParam("images") List<MultipartFile> images
    ) {
        ProductDTO productDTO = ProductDTO.builder()
                .vendorId(vendorId)
                .name(name)
                .description(description)
                .quantity(quantity)
                .price(price)
                .category(category)
                .images(images)
                .build();

        try {
            vendorService.createNewProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading images: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update-product")
    public ResponseEntity<String> updateProduct(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("quantity") int quantity,
            @RequestParam("price") double price,
            @RequestParam("category") String category
    ) {
        ProductDTO productDTO = ProductDTO.builder()
                .name(name)
                .description(description)
                .quantity(quantity)
                .price(price)
                .category(category)
                .build();

        try {
            vendorService.updateProduct(productDTO, id);
            return ResponseEntity.ok("Product updated successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/products/{vendorId}")
    public ResponseEntity<List<VendorProductDTO>> getVendorProducts(@PathVariable long vendorId){
        return ResponseEntity.ok(vendorService.getAllVendorProducts(vendorId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVendorProduct(@PathVariable long id) {
        try {
            vendorService.deleteProduct(id);
            return ResponseEntity.ok("{\"message\": \"Product deleted successfully\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

   //TODO requesting parameters
    @GetMapping("/search/{vendorId}/{name}")
    public ResponseEntity<List<VendorProductDTO>> searchForProduct(@PathVariable long vendorId, @PathVariable String name) {
        return  ResponseEntity.ok(vendorService.getProductByName(vendorId, name));
    }

}

