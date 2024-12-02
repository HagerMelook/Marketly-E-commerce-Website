package com.market.backend.signup.basicSignUp.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.signup.basicSignUp.Model.BasicClient;
import com.market.backend.signup.basicSignUp.Repository.BasicClientRepo;

@SpringBootTest
public class ClientServiceTest {
    @Autowired
    @InjectMocks
    private ClientService clientService;

    @Mock
    private BasicClientRepo basicClientRepo;

    @Test
    void testInsertBasicClientCaseExist1() {
        BasicClient basicClient = new BasicClient();
        basicClient.setUsername("hager");
        basicClient.setPassword("123");

        when(basicClientRepo.existsByUsername("hager")).thenReturn(true);
        String result = clientService.insertBasicClient(basicClient);
        assertEquals(result, "The username is already exist");
    }

    @Test
    void testInsertBasicClientCaseNotExist() {
        BasicClient basicClient = new BasicClient();
        basicClient.setUsername("Ali");
        basicClient.setPassword("123");

        when(basicClientRepo.existsByUsername("Ali")).thenReturn(false);
        String result = clientService.insertBasicClient(basicClient);
        assertEquals("Successfully registered",result);
    }
}
