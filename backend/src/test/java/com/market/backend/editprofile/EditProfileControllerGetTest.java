package com.market.backend.editprofile;


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
import org.springframework.security.test.context.support.WithMockUser;
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
            new Account(1L,true, "admin", "user1", "basic"),
            new Account(2L, false, "client", "user2", "basic"),
            new Account(3L, true,  "vendor", "user3", "basic"),
            new Account(4L,true, "admin", "user4", "oauth"),
            new Account(5L,true, "client", "user5", "oauth"),
            new Account(6L,true, "vendor", "user6", "oauth")

    };

    Password[] passwords = new Password[] {
            new Password(1L, "password1", accounts[0]),
            new Password(2L, "password2", accounts[1]),
            new Password(3L, "password3", accounts[2])
    };

    Admin[] admins = new Admin[] {
            new Admin(1L, "adminfirst", "adminlast", accounts[0]),
            new Admin(4L, "adminfirst", "adminlast", accounts[3])
    };

    Client[] clients = new Client[] {
            new Client(2L, "clientfirst", "clientlast", accounts[1]),
            new Client(5L, "clientfirst", "clientlast", accounts[4])
    };

    Vendor[] vendors = new Vendor[] {
            new Vendor(3L, "vendorfirst", 11L, accounts[2]),
            new Vendor(6L, "vendorfirst", 11L, accounts[5])
    };

    ShippingInfo[] shippingInfos = new ShippingInfo[] {
            new ShippingInfo(1L,  accounts[0], "adminaddress", "adminphone", "123"),
            new ShippingInfo(2L, accounts[1], "clientaddress", "clientphone", "123"),
            new ShippingInfo(3L, accounts[2], "vendoraddress", "vendorphone", "123"),
            new ShippingInfo(4L,  accounts[3], "adminaddress", "adminphone", "123"),
            new ShippingInfo(5L, accounts[4], "clientaddress", "clientphone", "123"),
            new ShippingInfo(6L, accounts[5], "vendoraddress", "vendorphone", "123")
    };

    @Test
    @WithMockUser
    void getAdminInfoByIdValidIdStatus() throws Exception{
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], passwords[0], admins[0], shippingInfos[0]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("host", "localhost:8080"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAdminInfoByIdValidIdEmptyAdminStatus() throws Exception{
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], passwords[0], new Admin(), shippingInfos[0]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAdminInfoByIdValidIdEmptyShippingInfoStatus() throws Exception{
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], passwords[0], admins[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAdminInfoByIdValidIdOauthStatus() throws Exception{
        //arrange
        Long id = 1L;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[3], null, admins[1], shippingInfos[3]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAdminInfoByIdValidIdJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 1,
                    "password": "password1",
                    "type": "admin",
                    "username": "user1",
                    "firstName": "adminfirst",
                    "lastName": "adminlast",
                    "active": true,
                    "address": "adminaddress",
                    "phone": "adminphone",
                    "authType": "basic"
                }
                """;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], passwords[0], admins[0], shippingInfos[0]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getAdminInfoByIdValidIdEmptyAdminJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 1,
                    "password": "password1",
                    "type": "admin",
                    "username": "user1",
                    "firstName": null,
                    "lastName": null,
                    "active": true,
                    "address": "adminaddress",
                    "phone": "adminphone",
                    "authType": "basic"
                }
                """;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], passwords[0], new Admin(), shippingInfos[0]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getAdminInfoByIdValidIdEmptyShippingInfoJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 1,
                    "password": "password1",
                    "type": "admin",
                    "username": "user1",
                    "firstName": "adminfirst",
                    "lastName": "adminlast",
                    "active": true,
                    "address": null,
                    "phone": null,
                    "authType": "basic"
                }
                """;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[0], passwords[0], admins[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getAdminInfoByIdValidIdOauthJsonSerialization() throws Exception{
        //arrange
        Long id = 4L;
        String expected = """
                {
                    "accountId": 4,
                    "password": null,
                    "type": "admin",
                    "username": "user4",
                    "firstName": "adminfirst",
                    "lastName": "adminlast",
                    "active": true,
                    "address": "adminaddress",
                    "phone": "adminphone",
                    "authType": "oauth"
                }
                """;

        Mockito.when(editProfileService.getAdminInfo(id)).thenReturn(new AdminInfoDTO(accounts[3], null, admins[1], shippingInfos[3]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/admininfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void getClientInfoByIdValidStatus() throws Exception {
        //arrange
        Long id = 2L;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], passwords[1], clients[0], shippingInfos[1]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getClientInfoByIdValidEmptyClientStatus() throws Exception {
        //arrange
        Long id = 2L;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], passwords[1], new Client(), shippingInfos[1]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getClientInfoByIdValidEmptyShippingInfoStatus() throws Exception {
        //arrange
        Long id = 2L;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], passwords[1], clients[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getClientInfoByIdValidOauthStatus() throws Exception {
        //arrange
        Long id = 5L;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[4], null, clients[1], shippingInfos[4]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getClientInfoByIdValidIdJsonSerialization() throws Exception{
        //arrange
        Long id = 2L;
        String expected = """
                {
                    "accountId": 2,
                    "password": "password2",
                    "type": "client",
                    "username": "user2",
                    "firstName": "clientfirst",
                    "lastName": "clientlast",
                    "active": false,
                    "address": "clientaddress",
                    "phone": "clientphone",
                    "authType": "basic"
                }
                """;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], passwords[1], clients[0], shippingInfos[1]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getClientInfoByIdValidIdEmptyClientJsonSerialization() throws Exception{
        //arrange
        Long id = 2L;
        String expected = """
                {
                    "accountId": 2,
                    "password": "password2",
                    "type": "client",
                    "username": "user2",
                    "firstName": null,
                    "lastName": null,
                    "active": false,
                    "address": "clientaddress",
                    "phone": "clientphone",
                    "authType": "basic"
                }
                """;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], passwords[1], new Client(), shippingInfos[1]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getClientInfoByIdValidIdEmptyShippingInfoJsonSerialization() throws Exception{
        //arrange
        Long id = 2L;
        String expected = """
                {
                    "accountId": 2,
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

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[1], passwords[1], clients[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getClientInfoByIdValidIdOauthJsonSerialization() throws Exception{
        //arrange
        Long id = 5L;
        String expected = """
                {
                    "accountId": 5,
                    "password": null,
                    "type": "client",
                    "username": "user5",
                    "firstName": "clientfirst",
                    "lastName": "clientlast",
                    "active": true,
                    "address": "clientaddress",
                    "phone": "clientphone",
                    "authType": "oauth"
                }
                """;

        Mockito.when(editProfileService.getClientInfo(id)).thenReturn(new ClientInfoDTO(accounts[4], null, clients[1], shippingInfos[4]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/clientinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void getVendorInfoByIdValidStatus() throws Exception {
        //arrange
        Long id = 3L;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], passwords[2], vendors[0], shippingInfos[2]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getVendorInfoByIdValidEmptyVendorStatus() throws Exception {
        //arrange
        Long id = 3L;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], passwords[2], new Vendor(), shippingInfos[2]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getVendorInfoByIdValidEmptyShippingInfoStatus() throws Exception {
        //arrange
        Long id = 3L;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], passwords[2], vendors[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getVendorInfoByIdValidOauthStatus() throws Exception {
        //arrange
        Long id = 6L;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[5], null, vendors[1], shippingInfos[5]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getVendorInfoByIdValidIdJsonSerialization() throws Exception{
        //arrange
        Long id = 3L;
        String expected = """
                {
                    "accountId": 3,
                    "password": "password3",
                    "type": "vendor",
                    "username": "user3",
                    "organizationName": "vendorfirst",
                    "taxNumber": 11,
                    "active": true,
                    "address": "vendoraddress",
                    "phone": "vendorphone",
                    "authType": "basic"
                }
                """;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], passwords[2], vendors[0], shippingInfos[2]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getVendorInfoByIdValidIdEmptyVendorJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 3,
                    "password": "password3",
                    "type": "vendor",
                    "username": "user3",
                    "organizationName": null,
                    "taxNumber": null,
                    "active": true,
                    "address": "vendoraddress",
                    "phone": "vendorphone",
                    "authType": "basic"
                }
                """;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], passwords[2], new Vendor(), shippingInfos[2]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getVendorInfoByIdValidIdEmptyShippingInfoJsonSerialization() throws Exception{
        //arrange
        Long id = 1L;
        String expected = """
                {
                    "accountId": 3,
                    "password": "password3",
                    "type": "vendor",
                    "username": "user3",
                    "organizationName": "vendorfirst",
                    "taxNumber": 11,
                    "active": true,
                    "address": null,
                    "phone": null,
                    "authType": "basic"
                }
                """;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[2], passwords[2], vendors[0], new ShippingInfo()));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
    void getVendorInfoByIdValidIdOauthJsonSerialization() throws Exception{
        //arrange
        Long id = 6L;
        String expected = """
                {
                    "accountId": 6,
                    "password": null,
                    "type": "vendor",
                    "username": "user6",
                    "organizationName": "vendorfirst",
                    "taxNumber": 11,
                    "active": true,
                    "address": "vendoraddress",
                    "phone": "vendorphone",
                    "authType": "oauth"
                }
                """;

        Mockito.when(editProfileService.getVendorInfo(id)).thenReturn(new VendorInfoDTO(accounts[5], null, vendors[0], shippingInfos[5]));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/account/vendorinfo/"+id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }

    @Test
    @WithMockUser
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
