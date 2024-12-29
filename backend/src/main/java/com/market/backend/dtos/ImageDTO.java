package com.market.backend.dtos;

import com.market.backend.models.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private byte[] imageBytes;

    public ImageDTO(ProductImage productImage) throws IOException {
        String path = productImage.getImagePath();
        Path imagePath = Path.of(path);
        imageBytes = Files.readAllBytes(imagePath);
    }
}
