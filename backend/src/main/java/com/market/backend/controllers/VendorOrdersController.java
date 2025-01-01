package com.market.backend.controllers;

import com.market.backend.dtos.OrderProductAccountDTO;
import com.market.backend.services.VendorOrdersService;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendororders")
public class VendorOrdersController {

    private final VendorOrdersService vendorOrdersService;

    public VendorOrdersController(VendorOrdersService vendorOrdersService) {
        this.vendorOrdersService = vendorOrdersService;
    }

    @GetMapping
    public ResponseEntity<Slice<OrderProductAccountDTO>> getVendorOrdersByAccount(
            @RequestParam Long accountId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "dec") String sortDir) {

        Slice<OrderProductAccountDTO> orders = vendorOrdersService.getOrderProductsByVendorAccount(accountId, status, page, size, sortDir);

        return ResponseEntity.ok(orders);
    }
}

