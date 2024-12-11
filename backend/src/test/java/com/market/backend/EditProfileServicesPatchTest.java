package com.market.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.market.backend.models.*;
import com.market.backend.repositories.*;
import com.market.backend.services.EditProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EditProfileServicesPatchTest {
    @Spy
    private  ObjectMapper objectMapper;
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private ShippingInfoRepository shippingInfoRepository;

    @InjectMocks
    private EditProfileService editProfileService;

    Account[] accounts;

    Admin[] admins;

    Client[] clients;

    Vendor[] vendors;

    ShippingInfo[] shippingInfos;

    @BeforeEach
    void setUp() {
        accounts = new Account[] {
                new Account(1L, "email1@example.com", "password1", true, "admin", "user1"),
                new Account(2L, "email2@example.com", "password2", false, "client", "user2"),
                new Account(3L, "email3@example.com", "password3", true, "vendor", "user3")
        };

        admins = new Admin[] {
                new Admin(1L, "adminfirst", "adminlast", accounts[0])
        };

        clients = new Client[] {
                new Client(2L, "clientfirst", "clientlast", accounts[1])
        };

        vendors = new Vendor[] {
                new Vendor(3L, "vendorfirst", "vendorlast", accounts[2])
        };

        shippingInfos = new ShippingInfo[] {
                new ShippingInfo(1L, "adminaddress", "adminphone", accounts[0]),
                new ShippingInfo(2L, "clientaddress", "clientphone", accounts[1]),
                new ShippingInfo(3L, "vendoraddress", "vendorname", accounts[2])
        };
    }

    @Test
    void updateAdminInfoValidAccountInfo() throws IOException {
        //arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/email","value":"newemail@example.com"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));
        Mockito.when(adminRepository.findByAccount_Id(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[0]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateAdminInfo(id, patch));
        assertEquals("newemail@example.com", accounts[0].getEmail());
    }

    @Test
    void updateAdminInfoValidAdminInfo() throws IOException {
        //arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/firstName","value":"newname"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));
        Mockito.when(adminRepository.findByAccount_Id(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[0]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateAdminInfo(id, patch));
        assertEquals("newname", admins[0].getFirstName());
    }

    @Test
    void updateAdminInfoValidShippingInfo() throws IOException {
        //arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));
        Mockito.when(adminRepository.findByAccount_Id(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[0]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateAdminInfo(id, patch));
        assertEquals("newaddress", shippingInfos[0].getAddress());
    }

    @Test
    void updateAdminInfoInvalidPatchContent() throws IOException {
        //arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));
        Mockito.when(adminRepository.findByAccount_Id(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[0]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateAdminInfo(id, patch));
    }

    @Test
    void updateAdminInfoInvalidId() throws IOException {
        //arrange
        Long id = 1000L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //act and assert
        assertThrows(NoSuchElementException.class, ()->editProfileService.updateAdminInfo(id, patch));
    }

    @Test
    void updateClientInfoValidAccountInfo() throws IOException {
        //arrange
        Long id = 2L;
        String patchContent = """
                [
                    {"op":"replace","path":"/email","value":"newemail@example.com"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[1]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateClientInfo(id, patch));
        assertEquals("newemail@example.com", accounts[1].getEmail());
    }

    @Test
    void updateClientInfoValidClientInfo() throws IOException {
        //arrange
        Long id = 2L;
        String patchContent = """
                [
                    {"op":"replace","path":"/firstName","value":"newname"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[1]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateClientInfo(id, patch));
        assertEquals("newname", clients[0].getFirstName());
    }

    @Test
    void updateClientInfoValidShippingInfo() throws IOException {
        //arrange
        Long id = 2L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[1]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateClientInfo(id, patch));
        assertEquals("newaddress", shippingInfos[1].getAddress());
    }

    @Test
    void updateClientInfoInvalidPatchContent() throws IOException {
        //arrange
        Long id = 2L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[1]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateClientInfo(id, patch));
    }

    @Test
    void updateClientInfoInvalidId() throws IOException {
        //arrange
        Long id = 1000L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //act and assert
        assertThrows(NoSuchElementException.class, ()->editProfileService.updateClientInfo(id, patch));
    }

    @Test
    void updateVendorInfoValidAccountInfo() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/email","value":"newemail@example.com"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findByAccount_Id(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateVendorInfo(id, patch));
        assertEquals("newemail@example.com", accounts[2].getEmail());
    }

    @Test
    void updateVendorInfoValidVendorInfo() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/organisationName","value":"newname"},
                    {"op":"replace","path":"/taxNumber","value":"newtaxnum"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findByAccount_Id(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateVendorInfo(id, patch));
        assertEquals("newname", vendors[0].getOrganisationName());
    }

    @Test
    void updateVendorInfoValidShippingInfo() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/address","value":"newaddress"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findByAccount_Id(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateVendorInfo(id, patch));
        assertEquals("newaddress", shippingInfos[2].getAddress());
    }

    @Test
    void updateVendorInfoInvalidPatchContent() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"newval"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findByAccount_Id(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }

    @Test
    void updateVendorInfoInvalidId() throws IOException {
        //arrange
        Long id = 1000L;
        String patchContent = """
                [
                    {"op":"replace","path":"/wrong","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //act and assert
        assertThrows(NoSuchElementException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }
}
