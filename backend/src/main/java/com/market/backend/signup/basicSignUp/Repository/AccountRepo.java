package com.market.backend.signup.basicSignUp.Repository;

import com.market.backend.signup.basicSignUp.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
}
