package com.market.backend.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class ProductDTO {
    private Long id;
    private Long vendorId;
    private String name;
    private String description;
    private double price;
    private double rating;
    private int quantity;
    private String category;
    List<MultipartFile> images;
}