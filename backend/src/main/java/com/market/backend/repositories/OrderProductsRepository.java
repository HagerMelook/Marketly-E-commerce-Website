package com.market.backend.repositories;

import com.market.backend.dtos.OrderProductAccountDTO;
import com.market.backend.models.OrderProducts;
import com.market.backend.models.OrderProductsId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductsRepository extends JpaRepository<OrderProducts, OrderProductsId> {

    @Query("SELECT new com.market.backend.dtos.OrderProductAccountDTO(op, o.account.id, o.account.username) " +
            "FROM OrderProducts op " +
            "JOIN op.order o " +
            "WHERE op.product.vendor.account.id = :accountId " +
            "AND (:status IS NULL OR o.status = :status)")
    Slice<OrderProductAccountDTO> findOrderProductsWithAccountByVendorAccountIdAndStatus(
            @Param("accountId") Long accountId,
            @Param("status") String status,
            Pageable pageable
    );

}
