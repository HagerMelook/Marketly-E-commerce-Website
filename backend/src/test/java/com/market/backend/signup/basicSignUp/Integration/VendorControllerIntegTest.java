package com.market.backend.signup.basicSignUp.Integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.market.backend.signup.basicSignUp.Model.BasicVendor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VendorControllerIntegTest {
    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @TestConfiguration
    static class RestTemplateConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    @Test
    void testVendorBasicSignUpValidAndExistBusName() {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname("marketly");
        vendor.setPassword("123");
        vendor.setTaxnumber(888888888);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The business name is already exist", response.getBody());
    }

    @Test
    void testVendorBasicSignUpValidAndExistTaxNum() {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname("market");
        vendor.setPassword("123");
        vendor.setTaxnumber(888888888);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The tax number is already exist", response.getBody());
    }


    @Test
    void testVendorBasicSignUpValidAndNotExist() {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname("suq");
        vendor.setPassword("123");
        vendor.setTaxnumber(999999999);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully registered", response.getBody());
    }

    @Test
    void testVendorBasicSignUpEmptyBusName() {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname(null);
        vendor.setPassword("123");
        vendor.setTaxnumber(999999999);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The business name can't be empty", response.getBody());
    }

    @Test
    void testVendorBasicSignUpEmptyPassword() {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname("suq");
        vendor.setPassword(null);
        vendor.setTaxnumber(999999999);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The password can't be empty", response.getBody());
    }

    @Test
    void testVendorBasicSignUpEmptyTaxNum() {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname("suq");
        vendor.setPassword("123");
        vendor.setTaxnumber(-1);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The tax number can't be empty", response.getBody());
    }

    @Test
    void testVendorBasicSignUpTaxNumNot9() {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname("suq");
        vendor.setPassword("123");
        vendor.setTaxnumber(99999999);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The tax number must be of 9 numbers only", response.getBody());
    }

    @Test
    void testVendorBasicSignUpLongBusName() {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname(
                "suqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        vendor.setPassword("123");
        vendor.setTaxnumber(999999999);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The business name can't be more than 80 character", response.getBody());
    }

    @Test
    void testVendorBasicSignUpLongPassword() throws Exception {
        String url = "http://localhost:" + port + "/SignUp/VendorBasicSignUp";
        BasicVendor vendor = new BasicVendor();
        vendor.setBusinessname("suq");
        vendor.setPassword(
                "1233333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        vendor.setTaxnumber(999999999);
        ResponseEntity<String> response = restTemplate.postForEntity(url, vendor, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The password can't be more than 80 character", response.getBody());
    }

}