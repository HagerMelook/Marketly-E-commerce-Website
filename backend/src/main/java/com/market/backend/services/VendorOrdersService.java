package com.market.backend.services;

import com.market.backend.dtos.OrderProductAccountDTO;
import com.market.backend.repositories.OrderProductsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VendorOrdersService {

    private final OrderProductsRepository orderProductsRepository;

    public VendorOrdersService(OrderProductsRepository orderProductsRepository) {
        this.orderProductsRepository = orderProductsRepository;
    }

    public Slice<OrderProductAccountDTO> getOrderProductsByVendorAccount(
            Long accountId,
            String status,
            int page,
            int size,
            String sortDir) {

        Pageable pageable = PageRequest.of(page, size,
                sortDir.equalsIgnoreCase("asc") ? Sort.by("order.date").ascending()
                        : Sort.by("order.date").descending());

        return orderProductsRepository.findOrderProductsWithAccountByVendorAccountIdAndStatus(accountId, status, pageable);
    }
}
