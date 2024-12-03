package com.market.backend.signup.basicSignUp.Repository;

import com.market.backend.signup.basicSignUp.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByAccount_Username(String userName);
    Optional<Client> findByAccount_Id(Long accountId);

}
