package com.market.backend.services;

import com.market.backend.dtos.OrderDTO;
import com.market.backend.models.Account;
import com.market.backend.models.Order;
import com.market.backend.models.Product;
import com.market.backend.repositories.OrderProductRepository;
import com.market.backend.repositories.OrderRepository;
import io.jsonwebtoken.lang.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderProductRepository orderProductRepository;

    @InjectMocks
    OrderService orderService;

    List<OrderDTO> resultingOrders;
    List<Order> basicOrders;
    List<Product> products;

    @BeforeEach
    void setUp(){
        products = new ArrayList<>();
        products.add(Product.builder()
                .name("Smart Screen")
                .price(500)
                .build());
        products.add(Product.builder()
                .name("Samsung S24 Ultra")
                .price(560)
                .build());
        resultingOrders = Arrays.asList(
                new OrderDTO[]{
                        OrderDTO.builder()
                            .checkoutPrice(1060)
                            .id(1l)
                            .date(new Date(2026, 6, 24))
                            .status("confirmed")
                            .products(products)
                            .build(),
                        OrderDTO.builder()
                            .checkoutPrice(2400)
                            .id(2l)
                            .date(new Date(2026, 6, 25))
                            .status("shipping")
                            .products(products)
                            .build()
                }
        );
        basicOrders = Arrays.asList(
                new Order[]{
                        Order.builder()
                            .checkoutPrice(1060)
                            .id(1l)
                            .date(new Date(2026, 6, 24))
                            .status("confirmed")
                            .account(Account.builder().id(1l).build())
                            .build(),
                        Order.builder()
                            .checkoutPrice(2400)
                            .id(2l)
                            .date(new Date(2026, 6, 25))
                            .status("shipping")
                            .account(Account.builder().id(1l).build())
                            .build()
                }
        );
    }

    @Test
    public void retrieveAllOrders_ReturnsAllOrders() {

        when(orderRepository.findAll()).thenReturn(basicOrders);
        when(orderProductRepository.findProductByOrderId(1l)).thenReturn(products);
        when(orderProductRepository.findProductByOrderId(2l)).thenReturn(products);

        List<OrderDTO> resultingOrders = orderService.listAllOrders(1);
        assertEquals(2, resultingOrders.size());
        resultingOrders.forEach(order -> {
            assertTrue(order.getAccount().getId() == 1);
        });
    }

    @Test
    public void retrieveAllOrders_NoOrders_RrturnsEmptyList(){

        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        List<OrderDTO> resultingOrders = orderService.listAllOrders(2);
        assertTrue(resultingOrders.isEmpty());
    }

}
