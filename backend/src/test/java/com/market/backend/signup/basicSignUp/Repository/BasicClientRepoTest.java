package com.market.backend.signup.basicSignUp.Repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.signup.basicSignUp.Model.BasicClient;

@SpringBootTest
public class BasicClientRepoTest {
    @Autowired
    private BasicClientRepo basicClientRepo;

    @Test
    void testExistsByUsernameCaseExist() {
        BasicClient basicClient = new BasicClient();
        basicClient.setUsername("Ahmed Mohammed");
        basicClient.setPassword("hee*klon111");
        basicClientRepo.save(basicClient);

        boolean result = basicClientRepo.existsByUsername("Ahmed Mohammed");
        assertTrue(result, "The user should exist");
    }

    @Test
    void testExistsByUsernameCaseNotExist() {
        boolean result = basicClientRepo.existsByUsername("Amal");
        assertFalse(result, "The user should not exist");
    }
}
