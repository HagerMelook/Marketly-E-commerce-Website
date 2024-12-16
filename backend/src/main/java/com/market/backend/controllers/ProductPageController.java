package com.market.backend.controllers;

import com.market.backend.dtos.CommentDTO;
import com.market.backend.dtos.RateDTO;
import com.market.backend.dtos.ImageDTO;
import com.market.backend.dtos.ProductInfoDTO;
import com.market.backend.repositories.CommentRepository;
import com.market.backend.repositories.RateRepository;
import com.market.backend.services.ProductPageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/productpage")
public class ProductPageController {
    private final CommentRepository commentRepository;
    private final RateRepository rateRepository;
    private final ProductPageService productPageService;

    @GetMapping("/{id}")
    ResponseEntity<ProductInfoDTO> getProductInfo(@PathVariable Long id) {
        try {
            ProductInfoDTO productInfoDTO = productPageService.getProductInfoDTO(id);
            return new ResponseEntity<>(productInfoDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/comments/{id}")
    ResponseEntity<Slice<CommentDTO>> getComments(Pageable pageable, @PathVariable Long id) {
        try {
            Slice<CommentDTO> slice = productPageService.getComments(pageable, id);
            return new ResponseEntity<>(slice, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/images/{id}")
    ResponseEntity<List<ImageDTO>> getImages(@PathVariable Long id) {
        try {
            List<ImageDTO> imageDTOList = productPageService.getImages(id);
            return new ResponseEntity<>(imageDTOList, HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
