package com.market.backend.services;

import com.market.backend.dtos.ProductDTO;
import com.market.backend.dtos.ShoppingCartDTO;
import com.market.backend.dtos.ShoppingCartProductDTO;
import com.market.backend.models.Account;
import com.market.backend.models.Product;
import com.market.backend.models.ShoppingCart;
import com.market.backend.models.ShoppingCartProduct;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.ProductRepository;
import com.market.backend.repositories.ShoppingCartProductRepository;
import com.market.backend.repositories.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final ShoppingCartProductRepository shoppingCartProductRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               ProductRepository productRepository,
                               AccountRepository accountRepository,
                               ShoppingCartProductRepository shoppingCartProductRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.shoppingCartProductRepository = shoppingCartProductRepository;
    }

    @Transactional
    public void addProductToCart(Long productId, Long userId, int quantity) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        if (quantity > product.get().getQuantity()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserAccountId(userId)
                .orElseGet(() -> createShoppingCart(userId));

        Optional<ShoppingCartProduct> existingCartProduct = shoppingCartProductRepository
                .findByShoppingCartAndProduct(shoppingCart, product.get());

        ShoppingCartProduct cartProduct;
        if (existingCartProduct.isPresent()) {
            cartProduct = existingCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
            double totalPrice = cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
            cartProduct.setPrice(totalPrice);
        } else {
            cartProduct = new ShoppingCartProduct();
            cartProduct.setShoppingCart(shoppingCart);
            cartProduct.setProduct(product.get());
            cartProduct.setQuantity(quantity);
            double totalPrice = cartProduct.getProduct().getPrice() * quantity;
            cartProduct.setPrice(totalPrice);
        }
        shoppingCartProductRepository.save(cartProduct);
    }

    private ShoppingCart createShoppingCart(Long userId) {
        Optional<Account> account = accountRepository.findById(userId);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }

        ShoppingCart newCart = new ShoppingCart();
        newCart.setUserAccount(account.get());
        newCart.setCartProducts(new ArrayList<>());
        shoppingCartRepository.save(newCart);
        return newCart;
    }

    @Transactional
    public void removeProductFromCart(Long productId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserAccountId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Shopping cart not found"));

        shoppingCartProductRepository.deleteByShoppingCartAndProductId(shoppingCart, productId);
    }

    public ShoppingCartDTO getCartProducts(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserAccountId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Shopping cart not found"));

        List<ShoppingCartProduct> products = shoppingCartProductRepository.findByShoppingCart(shoppingCart);

        double totalPrice = calculateTotalPrice(products);
        return new ShoppingCartDTO(shoppingCartProductToDTO(products), totalPrice);
    }

    private List<ShoppingCartProductDTO> shoppingCartProductToDTO(List<ShoppingCartProduct> shoppingCartProducts) {
        List<ShoppingCartProductDTO> shoppingCartProductDTOS = new ArrayList<>();
        for (ShoppingCartProduct shoppingCartProduct : shoppingCartProducts) {
            ShoppingCartProductDTO shoppingCartProductDTO = ShoppingCartProductDTO.builder()
                    .id(shoppingCartProduct.getId())
                    .product(productToDTO(shoppingCartProduct.getProduct()))
                    .quantity(shoppingCartProduct.getQuantity())
                    .price(shoppingCartProduct.getPrice())
                    .build();
            shoppingCartProductDTOS.add(shoppingCartProductDTO);
        }
        return shoppingCartProductDTOS;
    }

    private ProductDTO productToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .build();
    }

    @Transactional
    public void updateProductQuantityInCart(Long productId, Long userId, int newQuantity) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserAccountId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (shoppingCart.isEmpty()) {
            throw new IllegalArgumentException("Shopping cart not found");
        }

        Optional<ShoppingCartProduct> cartProduct = shoppingCartProductRepository
                .findByShoppingCartAndProduct(shoppingCart.get(), product);

        if (cartProduct.isEmpty()) {
            throw new IllegalArgumentException("Product not found in the cart");
        }

        ShoppingCartProduct productInCart = cartProduct.get();
        productInCart.setQuantity(newQuantity);
        shoppingCartProductRepository.save(productInCart);
    }

    private double calculateTotalPrice(List<ShoppingCartProduct> cartProducts) {
        double totalPrice = 0;
        for (ShoppingCartProduct cartProduct : cartProducts) {
            totalPrice += cartProduct.getProduct().getPrice() * cartProduct.getQuantity();
        }
        return totalPrice;
    }
}
