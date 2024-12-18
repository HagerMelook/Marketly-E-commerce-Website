package com.market.backend.services;

import com.market.backend.models.Product;
import com.market.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> searchWithKey(String key){
        String[] words = key.split(" ");
        List<Product> result = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        products.forEach(product -> {
            for (String word : words) {
                if (product.getName().toLowerCase().contains(word.toLowerCase())
                || product.getDescription().toLowerCase().contains(word.toLowerCase())){
                    result.add(product);
                    break;
                }
            }
        });
        return result;
    }

}
