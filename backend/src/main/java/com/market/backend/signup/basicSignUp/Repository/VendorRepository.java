package com.market.backend.signup.basicSignUp.Repository;

import com.market.backend.signup.basicSignUp.Model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByAccount_Id(Long accountId);
    boolean existsByorganisationName(String organisationName);
    boolean existsBytaxNumber(Long taxNumber);
}
