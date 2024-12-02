package com.market.backend.signup.basicSignUp.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.market.backend.signup.basicSignUp.Service.VendorService;

@WebMvcTest(VendorController.class)
public class VendorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    private VendorService vendorService;

    @Test
    void testVendorBasicSignUpValidAndExist() throws Exception {
        String vendor = """
                    {
                        "businessname": "marketly",
                        "password": "123",
                        "taxnumber": "888888888"
                    }
                """;

        Mockito.when(vendorService.insertBasicVendor(Mockito.any()))
                .thenReturn("The business name is already exist");

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendor))
                .andExpect(status().isOk())
                .andExpect(content().string("The business name is already exist"));
    }

    @Test
    void testVendorBasicSignUpValidAndNotExist() throws Exception {
        String vendor = """
                    {
                        "businessname": "suq",
                        "password": "123",
                        "taxnumber": "999999999"
                    }
                """;

        Mockito.when(vendorService.insertBasicVendor(Mockito.any()))
                .thenReturn("Successfully registered");

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendor))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully registered"));
    }

    @Test
    void testVendorBasicSignUpEmptyBusName() throws Exception {
        String vendor = """
                    {
                        "businessname": null,
                        "password": "123",
                        "taxnumber": "999999999"
                    }
                """;

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendor))
                .andExpect(status().isOk())
                .andExpect(content().string("The business name can't be empty"));
    }

    @Test
    void testVendorBasicSignUpEmptyPassword() throws Exception {
        String vendor = """
                    {
                        "businessname": "suq",
                        "password": null,
                        "taxnumber": "999999999"
                    }
                """;

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendor))
                .andExpect(status().isOk())
                .andExpect(content().string("The password can't be empty"));
    }

    @Test
    void testVendorBasicSignUpEmptyTaxNum() throws Exception {
        String vendor = """
                    {
                        "businessname": "suq",
                        "password": "123",
                        "taxnumber": -1
                    }
                """;

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendor))
                .andExpect(status().isOk())
                .andExpect(content().string("The tax number can't be empty"));
    }

    @Test
    void testVendorBasicSignUpTaxNumNot9() throws Exception {
        String vendor = """
                    {
                        "businessname": "suq",
                        "password": "123",
                        "taxnumber": 99999999
                    }
                """;

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendor))
                .andExpect(status().isOk())
                .andExpect(content().string("The tax number must be of 9 numbers only"));
    }

    @Test
    void testVendorBasicSignUpLongBusName() throws Exception {
        String vendor = """
                    {
                        "businessname": "suqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                        "password": "123",
                        "taxnumber": 999999999
                    }
                """;

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendor))
                .andExpect(status().isOk())
                .andExpect(content().string("The business name can't be more than 80 character"));
    }

    @Test
    void testVendorBasicSignUpLongPassword() throws Exception {
        String vendor = """
                    {
                        "businessname": "suq",
                        "password": "12333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333",
                        "taxnumber": 999999999
                    }
                """;

        mockMvc.perform(post("/SignUp/VendorBasicSignUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(vendor))
                .andExpect(status().isOk())
                .andExpect(content().string("The password can't be more than 80 character"));
    }
}
