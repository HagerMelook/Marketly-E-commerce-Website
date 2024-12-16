package com.market.backend.repositories;

import com.market.backend.models.VendorRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRequestRepository extends JpaRepository<VendorRequest, Long> {
    boolean existsByOrganizationName(String organizationName);
    boolean existsBytaxNumber(Long taxNumber);
    boolean existsByUsername(String username);
}

