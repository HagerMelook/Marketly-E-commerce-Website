package com.market.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.backend.configurations.JWTFilter;
import com.market.backend.models.Account;
import com.market.backend.models.VendorRequest;
import com.market.backend.services.JWTService;
import com.market.backend.services.SignUpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignUpController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({JWTService.class, JWTFilter.class})
class SignUpControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SignUpService signUpService;

    @MockitoBean
    private OAuth2User oAuth2User;

    @Test
    void testClientBasicAuth_ReturnsOk200() throws Exception {
        Account account = Account.builder()
                .id(1L)
                .username("AhmedAshraf")
                .isActive(true)
                .type("client")
                .authType("basic")
                .build();
        String password = "147852369";

        String body = new ObjectMapper().writeValueAsString(account);
        when(signUpService.insertBasicClient(account, password)).thenReturn("Successfully registered");

        mockMvc.perform(post("/SignUp/ClientBasicSignUp/{password}", password)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully registered"));
    }

    @Test
    void testVendorBasicAuth_ReturnsOk200() throws Exception {
        VendorRequest vendor = VendorRequest.builder()
                .username("AhmedAshraf")
                .authType("basic")
                .build();

        String body = new ObjectMapper().writeValueAsString(vendor);
        when(signUpService.insertBasicVendor(vendor)).thenReturn("The tax number can't be empty");

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("The tax number can't be empty"));
    }
}

