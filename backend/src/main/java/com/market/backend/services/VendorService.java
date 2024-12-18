package com.market.backend.services;

import com.market.backend.dtos.ProductDTO;
import com.market.backend.dtos.VendorProductDTO;
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
import java.util.ArrayList;
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

    @Transactional
    public List<VendorProductDTO> getAllVendorProducts(long vendorId) {
        List<Product> products = productRepository.findByVendorId(vendorId);
        return getProductDTOS(products);
    }

    @Transactional
    public void deleteProduct(Long id){
        productImageRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    @Transactional
    public List<VendorProductDTO> getProductByName(long vendorId, String name){
        List<Product> products = productRepository.findByVendorIdAndNameContaining(vendorId, name);
        return getProductDTOS(products);
    }

    private List<VendorProductDTO> getProductDTOS(List<Product> products) {
        List<VendorProductDTO> productsDTO = new ArrayList<>();

        for (Product product : products) {
            List<String> images = productImageRepository.findByProductId(product.getId());
            //TODO using builder
            VendorProductDTO productDTO = VendorProductDTO.builder()
                    .id(product.getId())
                    .vendorId(product.getVendor().getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .rating(product.getRating())
                    .quantity(product.getQuantity())
                    .category(product.getCategory())
                    .images(images)
                    .build();

            productsDTO.add(productDTO);
        }

        return productsDTO;
    }

}