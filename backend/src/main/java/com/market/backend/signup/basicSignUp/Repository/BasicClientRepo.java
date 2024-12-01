package com.market.backend.signup.basicSignUp.Repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.market.backend.signup.basicSignUp.Model.BasicClient;

@Repository
public interface BasicClientRepo extends JpaRepository<BasicClient, BigInteger> {
    boolean existsByUsername(String username);
}