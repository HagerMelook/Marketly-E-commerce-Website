package com.market.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;

    @Column(name = "category_image_path", nullable = false)
    private String categoryImagePath;
}
