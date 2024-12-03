package com.market.backend.signup.basicSignUp.Repository;

import com.market.backend.signup.basicSignUp.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAccount_Id(Long accountId);
}
