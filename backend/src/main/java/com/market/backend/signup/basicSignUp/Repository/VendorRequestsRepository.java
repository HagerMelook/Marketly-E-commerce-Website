package com.market.backend.signup.basicSignUp.Repository;

import com.market.backend.signup.basicSignUp.Model.VendorRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRequestsRepository extends JpaRepository<VendorRequests, Long> {
    boolean existsByorganizationName(String organizationName);
    boolean existsBytaxNumber(Long taxNumber);
    boolean existsByEmail(String email);
}
