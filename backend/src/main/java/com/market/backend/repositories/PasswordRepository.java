package com.market.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.market.backend.models.Password;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {
    String getAccountPasswordByAccountId(long accountId);
}
