package com.market.backend.repositories;

import com.market.backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    boolean existsByUsername(String username);
    @Query("SELECT a.id FROM Account a WHERE a.username = :username")
    Long findIdByUsername(String username);
    @Query("SELECT a.type FROM Account a WHERE a.username = :username")
    String getTypeByUserName(String username);
}
