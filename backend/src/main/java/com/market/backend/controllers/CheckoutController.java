package com.market.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market.backend.dtos.OrderDTO;
import com.market.backend.models.ShippingInfo;
import com.market.backend.services.CheckoutService;

@RestController
@CrossOrigin
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    private CheckoutService checkoutService;

    @PostMapping("/shippingInfo")
    public String checkoutShippingInfo(@RequestBody ShippingInfo shippingInfo){
        System.out.println(shippingInfo.getId()+" "+shippingInfo.getAddress()+" "+shippingInfo.getPhoneNumber()+" "+shippingInfo.getPostalCode());
        return checkoutService.insertShippingInfo(shippingInfo);
    }

    @PostMapping("/orderDetails")
    public String checkoutOrderDetails(@RequestBody OrderDTO orderDTO){
        System.out.println(orderDTO.getAccount_id()+" "+orderDTO.getCheckoutPrice());
        return checkoutService.insertOrderDetails(orderDTO);
    }

    @GetMapping("/{account_id}")
    public ShippingInfo getShippingInfoIfExist(@PathVariable Long account_id){
        return checkoutService.getShippingInfoIfExist(account_id);
    }
}
