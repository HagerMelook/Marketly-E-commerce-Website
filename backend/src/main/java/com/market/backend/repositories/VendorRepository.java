package com.market.backend.repositories;

import com.market.backend.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional <Vendor> findByOrganizationName(String organizationName);
}