package com.market.backend.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.market.backend.models.Order;
import com.market.backend.repositories.OrderRepositories;

@Service
public class ShippingCompanySimulationService {
    @Autowired
    private final OrderRepositories orderRepositories;
   

    public ShippingCompanySimulationService(OrderRepositories orderRepositories) {
        this.orderRepositories = orderRepositories;
    }

    public ResponseEntity<?> getOrderStatus(Long id) {
        Optional<Order> existingOrder = orderRepositories.findById(id);

        if (!existingOrder.isPresent())
            return ResponseEntity.notFound().build();

        Order odrer = existingOrder.get();
        Order updatedOrder = updateOrderStatus(odrer);

        return ResponseEntity.ok(updatedOrder.getStatus());
    }

    public ResponseEntity<Slice<Order>> getListOrderStatus(Slice<Order> listOrders) {

        List<Order> updatedOrders = new ArrayList<>();
        for (Order order : listOrders.getContent()) {
            Order updatedOrder = updateOrderStatus(order);
            updatedOrders.add(updatedOrder);
        }
    
        Pageable pageable = listOrders.getPageable();
        boolean hasNext = updatedOrders.size() == pageable.getPageSize();
        Slice<Order> updatedOrderSlice = new SliceImpl<>(updatedOrders, pageable, hasNext);
    
        return ResponseEntity.ok(updatedOrderSlice);
    }
    

    public Order updateOrderStatus(Order order) {
        LocalDateTime pastDateTime = order.getDateTime();
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Calculate the duration between the order's past time and the current time
        Duration duration = Duration.between(pastDateTime, currentDateTime);

        // Convert duration to minutes
        long minutes = duration.toMinutes();

        // Update order status based on the elapsed time in minutes
        if (minutes < 30)
            order.setStatus("confirmed");
        else if (minutes < 60)
            order.setStatus("processing");
        else if (minutes < 120)
            order.setStatus("packaged");
        else if (minutes < 1440)
            order.setStatus("shipped");
        else
            order.setStatus("delivered");

        return orderRepositories.save(order);
    }
}
