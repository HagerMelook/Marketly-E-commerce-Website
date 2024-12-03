package com.market.backend.repositories;

import com.market.backend.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByAccount_Username(String userName);
    Optional<Client> findByAccount_Id(Long accountId);
}
