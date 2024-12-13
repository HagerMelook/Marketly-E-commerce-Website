package com.market.backend.signup.basicSignUp.Repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.signup.basicSignUp.Model.BasicVendor;

@SpringBootTest
public class BasicVendorRepoTest {
    @Autowired
    private BasicVendorRepo basicVendorRepo;

    @Test
    void testExistsByBusinessnameCaseExist() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("jumia");
        basicVendor.setPassword("sale*for456");
        basicVendor.setTaxnumber(234567897);

        basicVendorRepo.save(basicVendor);
        boolean result = basicVendorRepo.existsByBusinessname("jumia");
        assertTrue(result, "The business should exist");
    }

    @Test
    void testExistsByBusinessnameCaseNotExist() {
        boolean result = basicVendorRepo.existsByBusinessname("noon");
        assertFalse(result, "The business should not exist");
    }

    @Test
    void testExistsByTaxnumberCaseExist() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("amazon");
        basicVendor.setPassword("sale*for456");
        basicVendor.setTaxnumber(234567896);

        basicVendorRepo.save(basicVendor);
        boolean result = basicVendorRepo.existsByTaxnumber(234567896);
        assertTrue(result, "The tax number should exist");
    }

    @Test
    void testExistsByTaxnumberCaseNotExist() {
        boolean result = basicVendorRepo.existsByTaxnumber(000000000);
        assertFalse(result, "The tax number should not exist");
    }
}
