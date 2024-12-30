package com.market.backend.controllers;

import com.market.backend.models.Product;
import com.market.backend.services.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/{key}")
    public List<Product> search(@PathVariable("key") String key){
        return searchService.searchWithKey(key);
    }
}
