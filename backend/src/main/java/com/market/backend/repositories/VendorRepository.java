package com.market.backend.repositories;


import com.market.backend.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByAccount_Id(Long accountId);
    boolean existsByOrganisationName(String organizationName);
    boolean existsByTaxNumber(String taxNumber);
}
