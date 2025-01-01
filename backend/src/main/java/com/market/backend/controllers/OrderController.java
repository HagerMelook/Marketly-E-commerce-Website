package com.market.backend.controllers;

import com.market.backend.dtos.OrderDTO;
import com.market.backend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController{

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    List<OrderDTO> retrieveHistory(@PathVariable int id){
        return orderService.listAllOrders(id);
    }

    @DeleteMapping("cancel/{id}")
    void orderCancellation(@PathVariable int id){
        orderService.cancelOrder(id);
    }
    
}
