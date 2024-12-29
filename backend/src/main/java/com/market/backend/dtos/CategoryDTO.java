package com.market.backend.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class CategoryDTO {
    private String categoryName;
    private String categoryImagePath;
    private MultipartFile images;
}
