package com.market.backend.signup.basicSignUp.Repository;
import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.market.backend.signup.basicSignUp.Model.BasicVendor;

@Repository
public interface BasicVendorRepo extends JpaRepository <BasicVendor, BigInteger>{
    boolean existsByBusinessname(String businessname);
    boolean existsByTaxnumber(int taxnumber);
}