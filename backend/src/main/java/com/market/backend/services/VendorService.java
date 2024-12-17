package com.market.backend.services;

import com.market.backend.dtos.ProductDTO;
import com.market.backend.models.Product;
import com.market.backend.models.ProductImage;
import com.market.backend.models.Vendor;
import com.market.backend.repositories.CategoryRepository;
import com.market.backend.repositories.ProductImageRepository;
import com.market.backend.repositories.ProductRepository;
import com.market.backend.repositories.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VendorService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public VendorService(
            ProductImageRepository productImageRepository,
            CategoryRepository categoryRepository,
            VendorRepository vendorRepository,
            ProductRepository productRepository
    ) {
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void createNewProduct(ProductDTO newProduct) throws IOException {
        Vendor vendor = vendorRepository.findById(newProduct.getVendorId())
                .orElseThrow(() -> new NoSuchElementException("Vendor not found"));

        Product product = Product.builder()
                .name(newProduct.getName())
                .vendor(vendor)
                .price(newProduct.getPrice())
                .rating(0)
                .description(newProduct.getDescription())
                .quantity(newProduct.getQuantity())
                .category(newProduct.getCategory())
                .build();

        productRepository.save(product);
        try {
            saveProductImages(product, newProduct.getImages());
        } catch (IOException e) {
            throw new IOException("Failed to save product image", e);
        }
    }

    public void saveProductImages(Product product, List<MultipartFile> images) throws IOException {
        Path path = Paths.get("src/main/resources/static/images/");
        Path absolutePath = path.toAbsolutePath();

        File directory = new File(absolutePath.toString());

        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (MultipartFile image : images) {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path imagePath = absolutePath.resolve(fileName);
            image.transferTo(imagePath.toFile());

            ProductImage productImage = ProductImage.builder()
                    .product(product)
                    .imagePath(imagePath.toString())
                    .build();
            productImageRepository.save(productImage);
        }
    }

    @Transactional
    public void updateProduct(ProductDTO updatedProduct, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setCategory(updatedProduct.getCategory());
        product.setQuantity(updatedProduct.getQuantity());

        productRepository.save(product);
    }

    @Transactional
    public List<String> getAllCategories() {
        return categoryRepository.findAllCategoryNames();
    }
}
