package com.market.backend.repositories;


import com.market.backend.models.ShippingInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Long> {
    Optional<ShippingInfo> findByAccountId(Long accountId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO shipping_info (account_id, address, phone_number, postal_code) " +
            "VALUES (:accountId, '', '', '')", nativeQuery = true)
    void insertShippingInfo(@Param("accountId") Long accountId);
}
