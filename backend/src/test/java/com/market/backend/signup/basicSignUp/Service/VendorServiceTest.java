package com.market.backend.signup.basicSignUp.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.signup.basicSignUp.Model.BasicVendor;
import com.market.backend.signup.basicSignUp.Repository.BasicVendorRepo;

@SpringBootTest
public class VendorServiceTest {
    @Autowired
    @InjectMocks
    private VendorService vendorService;

    @Mock
    private BasicVendorRepo basicVendorRepo;

    @Test
    void testInsertBasicVendorCaseBusNameExist() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("marketly");
        basicVendor.setPassword("123");
        basicVendor.setTaxnumber(888888888);

        when(basicVendorRepo.existsByBusinessname("marketly")).thenReturn(true);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("The business name is already exist", result);
    }

    @Test
    void testInsertBasicVendorCaseTaxNumExist() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("didy");
        basicVendor.setPassword("123");
        basicVendor.setTaxnumber(888888888);

        when(basicVendorRepo.existsByTaxnumber(888888888)).thenReturn(true);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("The tax number is already exist", result);
    }

    @Test
    void testInsertBasicVendorCaseNotExist() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("sala");
        basicVendor.setPassword("123");
        basicVendor.setTaxnumber(654781007);
        when(basicVendorRepo.existsByBusinessname("sala")).thenReturn(false);
        when(basicVendorRepo.existsByTaxnumber(654781007)).thenReturn(false);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("Successfully registered", result);
    }

    @Test
    void testInsertBasicVendorCaseNullBusName() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname(null);
        basicVendor.setPassword("123");
        basicVendor.setTaxnumber(654781007);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("The business name can't be empty", result);
    }

    @Test
    void testInsertBasicVendorCaseNullPassword() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("sala");
        basicVendor.setPassword(null);
        basicVendor.setTaxnumber(654781007);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("The password can't be empty", result);
    }

    @Test
    void testInsertBasicVendorCaseNullTaxNum() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("sala");
        basicVendor.setPassword("123");
        basicVendor.setTaxnumber(-1);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("The tax number can't be empty", result);
    }

    @Test
    void testInsertBasicVendorCaseLongBusName() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("salaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        basicVendor.setPassword("123");
        basicVendor.setTaxnumber(654781007);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("The business name can't be more than 80 character", result);
    }

    @Test
    void testInsertBasicVendorCaseLongPassword() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("sala");
        basicVendor.setPassword("1233333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        basicVendor.setTaxnumber(654781007);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("The password can't be more than 80 character", result);
    }

    @Test
    void testInsertBasicVendorCaseTaxNumNot9() {
        BasicVendor basicVendor = new BasicVendor();
        basicVendor.setBusinessname("sala");
        basicVendor.setPassword("123");
        basicVendor.setTaxnumber(65478107);
        String result = vendorService.insertBasicVendor(basicVendor);
        assertEquals("The tax number must be of 9 numbers only", result);
    }
}
