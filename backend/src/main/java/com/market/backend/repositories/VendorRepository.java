package com.market.backend.repositories;

import com.market.backend.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByAccount_Id(Long accountId);
}
