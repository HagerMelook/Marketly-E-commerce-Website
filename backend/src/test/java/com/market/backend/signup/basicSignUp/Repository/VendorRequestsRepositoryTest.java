package com.market.backend.signup.basicSignUp.Repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.signup.basicSignUp.Model.VendorRequests;

@SpringBootTest
public class VendorRequestsRepositoryTest {
    @Autowired
    private VendorRequestsRepository venReqRepo;

    @Test
    void testExistsByEmailExist() {
        VendorRequests vendorRequests = new VendorRequests("ali123@gmail.com","ali","sala",123456987);;
        venReqRepo.save(vendorRequests);
        boolean result = venReqRepo.existsByEmail("ali123@gmail.com");
        assertTrue(result);
    }

    @Test
    void testExistsByEmailNOtExist() {
        boolean result = venReqRepo.existsByEmail("ali111@gmail.com");
        assertFalse(result);
    }

    @Test
    void testExistsByorganizationNameExist() {
        VendorRequests vendorRequests = new VendorRequests("ali123@gmail.com","ali","suq",123456987);;
        venReqRepo.save(vendorRequests);
        boolean result = venReqRepo.existsByorganizationName("suq");
        assertTrue(result);
    }

    @Test
    void testExistsByorganizationNameNotExist() {
        boolean result = venReqRepo.existsByorganizationName("deep");
        assertFalse(result);
    }

    @Test
    void testExistsBytaxNumberExist() {
        VendorRequests vendorRequests = new VendorRequests("ali123@gmail.com","ali","jumia",123457887);
        venReqRepo.save(vendorRequests);
        boolean result = venReqRepo.existsBytaxNumber(123457887L);
        assertTrue(result);
    }

    @Test
    void testExistsBytaxNumberNotExist() {
        boolean result = venReqRepo.existsBytaxNumber(123457007L);
        assertFalse(result);
    }
}
