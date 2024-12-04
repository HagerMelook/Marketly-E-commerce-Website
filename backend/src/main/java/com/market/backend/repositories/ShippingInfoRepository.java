package com.market.backend.repositories;


import com.market.backend.models.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Long> {
    Optional<ShippingInfo> findByAccount_Id(Long accountId);
    boolean existsByAddress(String address);
    boolean existsByPhone(String phone);
}
