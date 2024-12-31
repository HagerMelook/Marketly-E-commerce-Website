package com.market.backend.services;

import com.market.backend.dtos.OrderDTO;
import com.market.backend.models.Order;
import com.market.backend.models.OrderProducts;
import com.market.backend.models.Product;
import com.market.backend.repositories.OrderProductRepository;
import com.market.backend.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    public List<OrderDTO> listAllOrders(int id) {
        List<Order> userOrders = orderRepository.findOrderByAccountId((long) id);
        List<OrderDTO> ordersIncludingProductsInfo = new ArrayList<>();
        for (Order order : userOrders) {
            OrderDTO orderDTO = OrderDTO.builder()
                    .id(order.getId())
                    .checkoutPrice(order.getCheckoutPrice())
                    .date(order.getDate())
                    .status(order.getStatus())
                    .products(orderProductRepository.findProductByOrderId(order.getId()))
                    .account(order.getAccount())
                    .build();
            ordersIncludingProductsInfo.add(orderDTO);
        }
        return ordersIncludingProductsInfo;
    }

    @Transactional
    public void cancelOrder(int id) {
        orderRepository.deleteById(id);
    }
}
