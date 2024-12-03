package com.market.backend.repositories;

import com.market.backend.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByAccount_Id(Long accountId);
    void deleteByAccount_Id(Long accountId);
}
