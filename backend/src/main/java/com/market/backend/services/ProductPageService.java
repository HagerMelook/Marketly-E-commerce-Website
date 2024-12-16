package com.market.backend.services;

import com.market.backend.dtos.CommentDTO;
import com.market.backend.dtos.RateDTO;
import com.market.backend.dtos.ImageDTO;
import com.market.backend.dtos.ProductInfoDTO;
import com.market.backend.models.Comment;
import com.market.backend.models.Product;
import com.market.backend.models.ProductImage;
import com.market.backend.models.ShippingInfo;
import com.market.backend.repositories.CommentRepository;
import com.market.backend.repositories.ProductImageRepository;
import com.market.backend.repositories.ProductRepository;
import com.market.backend.repositories.ShippingInfoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductPageService {

    private final ProductRepository productRepository;
    private final ShippingInfoRepository shippingInfoRepository;
    private final CommentRepository commentRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public ProductInfoDTO getProductInfoDTO(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        ShippingInfo shippingInfo = shippingInfoRepository.findById(product.getVendor().getId())
                .orElse(null);

        return new ProductInfoDTO(product, shippingInfo);
    }

    @Transactional
    public Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    @Transactional
    public Slice<CommentDTO> getComments(Pageable pageable, Long id) {
        Slice<Comment> slice = commentRepository.findAllByProductId(id ,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
                ));
        return slice.map(CommentDTO::new);
    }

    @Transactional
    public List<ImageDTO> getImages(Long id) {
        List<ProductImage> images = productImageRepository.findAllByProductId(id);
        return images.stream().map(productImage -> {
            try {
                return new ImageDTO(productImage);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                throw new NoSuchElementException("Image not found");
            }
        }).collect(Collectors.toList());
    }

}
