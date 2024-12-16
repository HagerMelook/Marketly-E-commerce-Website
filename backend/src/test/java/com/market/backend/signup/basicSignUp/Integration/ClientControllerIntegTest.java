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

import com.market.backend.signup.basicSignUp.Model.BasicClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerIntegTest {

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
    void testClientBasicSignUpValidAndExist() {
        String url = "http://localhost:" + port + "/SignUp/ClientBasicSignUp";
        BasicClient client = new BasicClient();
        client.setUsername("hager");
        client.setPassword("123");
        ResponseEntity<String> response = restTemplate.postForEntity(url, client, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The username is already exist", response.getBody());
    }

    @Test
    void testClientBasicSignUpValidAndNotExist() {
        String url = "http://localhost:" + port + "/SignUp/ClientBasicSignUp";
        BasicClient client = new BasicClient();
        client.setUsername("Reem");
        client.setPassword("123");
        ResponseEntity<String> response = restTemplate.postForEntity(url, client, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully registered", response.getBody());
    }

    @Test
    void testClientBasicSignUpEmptyUsername() {
        String url = "http://localhost:" + port + "/SignUp/ClientBasicSignUp";
        BasicClient client = new BasicClient();
        client.setUsername(null);
        client.setPassword("123");
        ResponseEntity<String> response = restTemplate.postForEntity(url, client, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The username can't be empty", response.getBody());
    }

    @Test
    void testClientBasicSignUpEmptyPassword() {
        String url = "http://localhost:" + port + "/SignUp/ClientBasicSignUp";
        BasicClient client = new BasicClient();
        client.setUsername("Sami");
        client.setPassword(null);
        ResponseEntity<String> response = restTemplate.postForEntity(url, client, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The password can't be empty", response.getBody());
    }

    @Test
    void testClientBasicSignUpLongUsername() {
        String url = "http://localhost:" + port + "/SignUp/ClientBasicSignUp";
        BasicClient client = new BasicClient();
        client.setUsername(
                "Samiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        client.setPassword(null);
        ResponseEntity<String> response = restTemplate.postForEntity(url, client, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The username can't be more than 80 character", response.getBody());
    }

    @Test
    void testClientBasicSignUpLongPassword() {
        String url = "http://localhost:" + port + "/SignUp/ClientBasicSignUp";
        BasicClient client = new BasicClient();
        client.setUsername("Sami");
        client.setPassword(
                "123333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        ResponseEntity<String> response = restTemplate.postForEntity(url, client, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The password can't be more than 80 character", response.getBody());
    }
}
