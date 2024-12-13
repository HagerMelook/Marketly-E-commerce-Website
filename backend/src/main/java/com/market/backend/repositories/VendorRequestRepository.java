package com.market.backend.repositories;

import com.market.backend.models.VendorRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRequestRepository extends JpaRepository<VendorRequest, Long> {
}
