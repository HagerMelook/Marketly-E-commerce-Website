package com.market.backend.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.market.backend.dtos.OrderDTO;
import com.market.backend.dtos.OrderProductDTO;
import com.market.backend.models.Account;
import com.market.backend.models.Order;
import com.market.backend.models.OrderProducts;
import com.market.backend.models.OrderProductsId;
import com.market.backend.models.Product;
import com.market.backend.models.ShippingInfo;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.OrderProductsRepository;
import com.market.backend.repositories.OrderRepository;
import com.market.backend.repositories.ProductRepository;
import com.market.backend.repositories.ShippingInfoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CheckoutService {
    private ShippingInfoRepository shippingInfoRepo;
    private AccountRepository accountRepository;
    private OrderRepository orderRepository;
    private OrderProductsRepository orderProductsRepository;
    private ProductRepository productRepository;

    public String insertShippingInfo(ShippingInfo shippingInfo) {
        // in case of any endpoint failure
        if (!accountRepository.existsById(shippingInfo.getId())) {
            return "Something Went Wrong, Please Try Again";
        }

        if (shippingInfoRepo.existsById(shippingInfo.getId())) {
            ShippingInfo oldInfo = shippingInfoRepo.findById(shippingInfo.getId())
                    .orElseThrow(() -> new EntityNotFoundException("ShippingInfo not found"));

            oldInfo.setAddress(shippingInfo.getAddress());
            oldInfo.setPhoneNumber(shippingInfo.getPhoneNumber());
            oldInfo.setPostalCode(shippingInfo.getPostalCode());
            shippingInfoRepo.save(shippingInfo);
            return "Shipping Info Updated Successfully";
        }
        shippingInfoRepo.save(shippingInfo);
        return "Shipping Info Inserted Successfully";
    }

    public String insertOrderDetails(OrderDTO orderDTO) {
        // in case of any endpoint failure
        if (!accountRepository.existsById(orderDTO.getAccount_id())) {
            return "Something Went Wrong, Please Try Again";
        }
        Account account = accountRepository.findById(orderDTO.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        Order order = Order.builder()
                .account(account)
                .checkoutPrice(orderDTO.getCheckoutPrice())
                .status("processing")
                .date(LocalDateTime.now())
                .build();

        order = orderRepository.save(order);
        for (OrderProductDTO order_product : orderDTO.getOrder_products()) {
            if (!productRepository.existsById(order_product.getId())) {
                return "Something Went Wrong, Please Try Again";
            }
            Product product = productRepository.findById(order_product.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            OrderProductsId orderProductsId = new OrderProductsId(order.getId(), product.getId());
            OrderProducts orderProducts = new OrderProducts();
            orderProducts.setOrderProductsId(orderProductsId);
            orderProducts.setOrder(order);
            orderProducts.setProduct(product);
            orderProducts.setQuantity(order_product.getQuantity());

            saveOrderProduct(orderProducts);
        }
        return "Order is Registered Successfully," + order.getId() ;
    }

    private OrderProducts saveOrderProduct(OrderProducts orderProducts) {
        return orderProductsRepository.save(orderProducts);
    }

    public ShippingInfo getShippingInfoIfExist(Long account_id) {
        // in case of any endpoint failure
        ShippingInfo shippingInfo =  new ShippingInfo();
        if (accountRepository.existsById(account_id)) {
            if (shippingInfoRepo.existsById(account_id)) {
                shippingInfo = shippingInfoRepo.findById(account_id)
                        .orElseThrow(() -> new EntityNotFoundException("ShippingInfo not found"));
        }
    }
    return shippingInfo;
}
}
