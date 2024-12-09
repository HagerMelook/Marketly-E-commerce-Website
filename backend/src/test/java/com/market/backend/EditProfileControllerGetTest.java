package com.market.backend;


import com.market.backend.controllers.EditProfileController;
import com.market.backend.dtos.AdminInfoDTO;
import com.market.backend.dtos.ClientInfoDTO;
import com.market.backend.dtos.VendorInfoDTO;
import com.market.backend.models.*;
import com.market.backend.services.EditProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EditProfileController.class)
class EditProfileControllerGetTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private EditProfileService editProfileService;

    Account[] accounts = new Account[] {
            new Account(1L, "email1@example.com", "password1", true, "admin", "user1"),
            new Account(2L, "email2@example.com", "password2", false, "client", "user2"),
            new Account(3L, "email3@example.com", "password3", true, "vendor", "user3")
    };

    Admin[] admins = new Admin[] {
            new Admin(1L, "adminfirst", "adminlast", accounts[0])
    };

    Client[] clients = new Client[] {
            new Client(2L, "clientfirst", "clientlast", accounts[1])
    };

    Vendor[] vendors = new Vendor[] {
            new Vendor(3L, "vendorfirst", "vendorlast", accounts[2])
    };

    ShippingInfo[] shippingInfos = new ShippingInfo[] {
            new ShippingInfo(1L, "adminaddress", "adminphone", accounts[0]),
            new ShippingInfo(2L, "clientaddress", "clientphone", accounts[1]),
            new ShippingInfo(3L, "vendoraddress", "vendorphone", accounts[2])
    };

    @Test
    void getAdminInfoByIdValidIdStatus() throws Exception{
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], admins[0], shippingInfos[0]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAdminInfoByIdValidIdEmptyAdminStatus() throws Exception{
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], new Admin(), shippingInfos[0]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAdminInfoByIdValidIdEmptyShippingInfoStatus() throws Exception{
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], admins[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAdminInfoByIdValidIdJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 1,
                    "email": "email1@example.com",
                    "password": "password1",
                    "type": "admin",
                    "username": "user1",
                    "firstName": "adminfirst",
                    "lastName": "adminlast",
                    "active": true,
                    "address": "adminaddress",
                    "phone": "adminphone"
                }
                """;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], admins[0], shippingInfos[0]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getAdminInfoByIdValidIdEmptyAdminJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 1,
                    "email": "email1@example.com",
                    "password": "password1",
                    "type": "admin",
                    "username": "user1",
                    "firstName": null,
                    "lastName": null,
                    "active": true,
                    "address": "adminaddress",
                    "phone": "adminphone"
                }
                """;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], new Admin(), shippingInfos[0]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getAdminInfoByIdValidIdEmptyShippingInfoJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 1,
                    "email": "email1@example.com",
                    "password": "password1",
                    "type": "admin",
                    "username": "user1",
                    "firstName": "adminfirst",
                    "lastName": "adminlast",
                    "active": true,
                    "address": null,
                    "phone": null
                }
                """;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], admins[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getAdminInfoByIdInvalidIdStatus() throws Exception{
        //arrange
        Long id = 1000L;

        Mockito.when(editProfileService.getAdminInfo(id)).thenThrow(new NoSuchElementException("User not found"));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void getClientInfoByIdValidStatus() throws Exception {
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], clients[0], shippingInfos[1]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getClientInfoByIdValidEmptyClientStatus() throws Exception {
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], new Client(), shippingInfos[1]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getClientInfoByIdValidEmptyShippingInfoStatus() throws Exception {
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], clients[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getClientInfoByIdValidIdJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 2,
                    "email": "email2@example.com",
                    "password": "password2",
                    "type": "client",
                    "username": "user2",
                    "firstName": "clientfirst",
                    "lastName": "clientlast",
                    "active": false,
                    "address": "clientaddress",
                    "phone": "clientphone"
                }
                """;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], clients[0], shippingInfos[1]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getClientInfoByIdValidIdEmptyClientJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 2,
                    "email": "email2@example.com",
                    "password": "password2",
                    "type": "client",
                    "username": "user2",
                    "firstName": null,
                    "lastName": null,
                    "active": false,
                    "address": "clientaddress",
                    "phone": "clientphone"
                }
                """;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], new Client(), shippingInfos[1]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getClientInfoByIdValidIdEmptyShippingInfoJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 2,
                    "email": "email2@example.com",
                    "password": "password2",
                    "type": "client",
                    "username": "user2",
                    "firstName": "clientfirst",
                    "lastName": "clientlast",
                    "active": false,
                    "address": null,
                    "phone": null
                }
                """;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], clients[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getClientInfoByIdInvalidIdStatus() throws Exception{
        //arrange
        Long id = 1000L;

        Mockito.when(editProfileService.getClientInfo(id)).thenThrow(new NoSuchElementException("User not found"));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVendorInfoByIdValidStatus() throws Exception {
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], vendors[0], shippingInfos[2]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getVendorInfoByIdValidEmptyVendorStatus() throws Exception {
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], new Vendor(), shippingInfos[2]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getVendorInfoByIdValidEmptyShippingInfoStatus() throws Exception {
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], vendors[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getVendorInfoByIdValidIdJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 3,
                    "email": "email3@example.com",
                    "password": "password3",
                    "type": "vendor",
                    "username": "user3",
                    "organisationName": "vendorfirst",
                    "taxNumber": "vendorlast",
                    "active": true,
                    "address": "vendoraddress",
                    "phone": "vendorphone"
                }
                """;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], vendors[0], shippingInfos[2]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getVendorInfoByIdValidIdEmptyVendorJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 3,
                    "email": "email3@example.com",
                    "password": "password3",
                    "type": "vendor",
                    "username": "user3",
                    "organisationName": null,
                    "taxNumber": null,
                    "active": true,
                    "address": "vendoraddress",
                    "phone": "vendorphone"
                }
                """;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], new Vendor(), shippingInfos[2]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getVendorInfoByIdValidIdEmptyShippingInfoJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 3,
                    "email": "email3@example.com",
                    "password": "password3",
                    "type": "vendor",
                    "username": "user3",
                    "organisationName": "vendorfirst",
                    "taxNumber": "vendorlast",
                    "active": true,
                    "address": null,
                    "phone": null
                }
                """;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], vendors[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    void getVendorInfoByIdInvalidIdStatus() throws Exception{
        //arrange
        Long id = 1000L;

        Mockito.when(editProfileService.getVendorInfo(id)).thenThrow(new NoSuchElementException("User not found"));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
