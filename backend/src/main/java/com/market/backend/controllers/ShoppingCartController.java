package com.market.backend.controllers;

import com.market.backend.dtos.ShoppingCartDTO;
import com.market.backend.services.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/ShoppingCart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/Add/{accountId}")
    public ResponseEntity<String> addProductToCart(@PathVariable long accountId,
                                                   @RequestParam Long productId,
                                                   @RequestParam int quantity){
        try {
            shoppingCartService.addProductToCart(productId, accountId, quantity);
            return ResponseEntity.ok("Product added to cart successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ShoppingCartDTO> getCartProducts(@PathVariable long accountId){
        ShoppingCartDTO products = shoppingCartService.getCartProducts(accountId);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/Delete/{productId}/{accountId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable long productId, @PathVariable long accountId){
        try {
            shoppingCartService.removeProductFromCart(productId, accountId);
            return ResponseEntity.ok("Product removed from cart.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/Update/{productId}/{accountId}")
    public ResponseEntity<String> updateProductQuantityInCart(@PathVariable long productId,
                                                              @PathVariable long accountId,
                                                              @RequestBody int newQuantity){
        try {
            shoppingCartService.updateProductQuantityInCart(productId, accountId, newQuantity);
            return ResponseEntity.ok("Product quantity updated.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}
