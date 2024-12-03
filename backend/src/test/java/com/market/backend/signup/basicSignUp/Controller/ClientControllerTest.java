package com.market.backend.signup.basicSignUp.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.market.backend.signup.basicSignUp.Service.SignUpService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(SginUpController.class)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private SignUpService clientService;

    @Test
    void testClientBasicSignUpValidAndNotExist() throws Exception {
        String client = """
                    {
                        "username": "Reem",
                        "password": "123"
                    }
                """;

        Mockito.when(clientService.insertBasicClient(Mockito.any()))
                .thenReturn("Successfully registered");

        mockMvc.perform(post("/SignUp/ClientBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(client))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully registered"));
    }

    @Test
    void testClientBasicSignUpValidAndExist() throws Exception {
        String client = """
                    {
                        "username": "hager",
                        "password": "123"
                    }
                """;

        Mockito.when(clientService.insertBasicClient(Mockito.any()))
                .thenReturn("The username is already exist");

        mockMvc.perform(post("/SignUp/ClientBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(client))
                .andExpect(status().isOk())
                .andExpect(content().string("The username is already exist"));
    }

    @Test
    void testClientBasicSignUpEmptyUsername() throws Exception {
        String client = """
                    {
                        "username": null,
                        "password": "123"
                    }
                """;
        Mockito.when(clientService.insertBasicClient(Mockito.any()))
                .thenReturn("The username can't be empty");

        mockMvc.perform(post("/SignUp/ClientBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(client))
                .andExpect(status().isOk())
                .andExpect(content().string("The username can't be empty"));
    }

    @Test
    void testClientBasicSignUpEmptyPassword() throws Exception {
        String client = """
                    {
                        "username": "Reem",
                        "password": null
                    }
                """;

        Mockito.when(clientService.insertBasicClient(Mockito.any()))
                .thenReturn("The password can't be empty");

        mockMvc.perform(post("/SignUp/ClientBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(client))
                .andExpect(status().isOk())
                .andExpect(content().string("The password can't be empty"));
    }

    @Test
    void testClientBasicSignUpLongUsername() throws Exception {
        String client = """
                    {
                        "username": "Reemaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                        "password": "123"
                    }
                """;
        Mockito.when(clientService.insertBasicClient(Mockito.any()))
                .thenReturn("The username can't be more than 80 character");

        mockMvc.perform(post("/SignUp/ClientBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(client))
                .andExpect(status().isOk())
                .andExpect(content().string("The username can't be more than 80 character"));
    }

    @Test
    void testClientBasicSignUpLongPassword() throws Exception {
        String client = """
                    {
                        "username": "Reem",
                        "password": "1233333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333"
                    }
                """;
        Mockito.when(clientService.insertBasicClient(Mockito.any()))
                .thenReturn("The password can't be more than 80 character");
                
        mockMvc.perform(post("/SignUp/ClientBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(client))
                .andExpect(status().isOk())
                .andExpect(content().string("The password can't be more than 80 character"));
    }
}
