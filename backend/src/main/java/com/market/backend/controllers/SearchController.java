package com.market.backend.controllers;

import com.market.backend.models.Product;
import com.market.backend.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Search")
public class SearchController {

    @Autowired
    SearchService serachService;

    @GetMapping("/{key}")
    public List<Product> search(@PathVariable("key") String key){
        return serachService.searchWithKey(key);
    }

}
