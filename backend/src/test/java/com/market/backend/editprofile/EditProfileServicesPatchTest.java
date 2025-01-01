package com.market.backend.editprofile;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EditProfileServicesPatchTest {
    @Spy
    private ObjectMapper objectMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
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

    @Mock
    private PasswordRepository passwordRepository;

    @InjectMocks
    private EditProfileService editProfileService;


    Account[] accounts;

    Admin[] admins;

    Client[] clients;

    Vendor[] vendors;

    Password[] passwords;

    ShippingInfo[] shippingInfos;

    @BeforeEach
    void setUp() {
        accounts = new Account[]{
                new Account(1L, true, "ROLE_ADMIN", "user1", "basic"),
                new Account(2L, false, "ROLE_CLIENT", "user2", "basic"),
                new Account(3L, true, "ROLE_VENDOR", "user3", "basic"),
                new Account(4L, true, "ROLE_ADMIN", "user4", "oauth"),
                new Account(5L, true, "ROLE_CLIENT", "user5", "oauth"),
                new Account(6L, true, "ROLE_VENDOR", "user6", "oauth")

        };

        passwords = new Password[]{
                new Password(1L, "password1", accounts[0]),
                new Password(2L, "password2", accounts[1]),
                new Password(3L, "password3", accounts[2])
        };

        admins = new Admin[]{
                new Admin(1L, "adminfirst", "adminlast", accounts[0]),
                new Admin(4L, "adminfirst", "adminlast", accounts[3])
        };

        clients = new Client[]{
                new Client(2L, "clientfirst", "clientlast", accounts[1]),
                new Client(5L, "clientfirst", "clientlast", accounts[4])
        };

        vendors = new Vendor[]{
                new Vendor(3L, "vendorfirst", 11L, accounts[2]),
                new Vendor(6L, "vendorfirst", 11L, accounts[5])
        };

        shippingInfos = new ShippingInfo[]{
                new ShippingInfo(1L, accounts[0], "adminaddress", "adminphone", "123"),
                new ShippingInfo(2L, accounts[1], "clientaddress", "clientphone", "123"),
                new ShippingInfo(3L, accounts[2], "vendoraddress", "vendorname", "123"),
                new ShippingInfo(4L, accounts[3], "adminaddress", "adminphone", "123"),
                new ShippingInfo(5L, accounts[4], "clientaddress", "clientphone", "123"),
                new ShippingInfo(6L, accounts[5], "vendoraddress", "vendorname", "123")
        };
    }

    @Test
    void updateAdminInfoValidPasswordInfo() throws IOException {
        //arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/password","value":"newpassword"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[0]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[0]));
        Mockito.when(passwordEncoder.encode("newpassword")).thenReturn("encoded");

        //act and assert
        assertDoesNotThrow(() -> editProfileService.updateAdminInfo(id, patch));
        assertEquals("encoded", passwords[0].getAccountPassword());
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
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[0]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[0]));

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
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[0]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[0]));

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
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[0]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[0]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateAdminInfo(id, patch));
    }

    @Test
    void updateAdminInfoPasswordForOauth() throws IOException {
        //arrange
        Long id = 4L;
        String patchContent = """
                [
                    {"op":"replace","path":"/password","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[3]));
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admins[1]));
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[3]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateAdminInfo(id, patch));
    }

    @Test
    void updateAdminInfoWrongType() throws IOException {
        //arrange
        Long id = 2L;
        String patchContent = """
                [
                    {"op":"replace","path":"/firstName","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));

        //act and assert
        assertThrows(NoSuchElementException.class, ()->editProfileService.updateAdminInfo(id, patch));
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
    void updateClientInfoValidPasswordInfo() throws IOException {
        //arrange
        Long id = 2L;
        String patchContent = """
                [
                    {"op":"replace","path":"/password","value":"newpassword"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[1]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[1]));
        Mockito.when(passwordEncoder.encode("newpassword")).thenReturn("encoded");

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateClientInfo(id, patch));
        assertEquals("encoded", passwords[1].getAccountPassword());
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
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[1]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[1]));

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
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[1]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[1]));

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
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[1]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[1]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateClientInfo(id, patch));
    }

    @Test
    void updateClientInfoPasswordForOauth() throws IOException {
        //arrange
        Long id = 5L;
        String patchContent = """
                [
                    {"op":"replace","path":"/password","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[4]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[1]));
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[4]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateClientInfo(id, patch));
    }

    @Test
    void updateClientInfoWrongType() throws IOException {
        //arrange
        Long id = 1L;
        String patchContent = """
                [
                    {"op":"replace","path":"/firstName","value":"new"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));

        //act and assert
        assertThrows(NoSuchElementException.class, ()->editProfileService.updateClientInfo(id, patch));
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
    void updateVendorInfoValidPasswordInfo() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/password","value":"newpassword"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));
        Mockito.when(passwordEncoder.encode("newpassword")).thenReturn("encoded");

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateVendorInfo(id, patch));
        assertEquals("encoded", passwords[2].getAccountPassword());
    }

    @Test
    void updateVendorInfoValidVendorInfo() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/organizationName","value":"newname"},
                    {"op":"replace","path":"/taxNumber","value":11}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertDoesNotThrow(()->editProfileService.updateVendorInfo(id, patch));
        assertEquals("newname", vendors[0].getOrganizationName());
        assertEquals(11L, vendors[0].getTaxNumber());
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
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

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
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }

    @Test
    void updateVendorInfoPasswordForOauth() throws IOException {
        //arrange
        Long id = 6L;
        String patchContent = """
                [
                    {"op":"replace","path":"/password","value":"newval"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[5]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[1]));
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[5]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }

    @Test
    void updateVendorInfoWrongType() throws IOException {
        //arrange
        Long id = 2L;
        String patchContent = """
                [
                    {"op":"replace","path":"/firstName","value":"newval"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));

        //act and assert
        assertThrows(NoSuchElementException.class, ()->editProfileService.updateVendorInfo(id, patch));
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




    @Test
    void updateInfoRestrictedAttrType() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/type","value":"newval"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }

    @Test
    void updateInfoRestrictedAttrAuthType() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/authType","value":"newval"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }

    @Test
    void updateInfoRestrictedAttrAccountId() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/accountId","value":12}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }

    @Test
    void updateInfoRestrictedAttrUsername() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/username","value":"newval"}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }

    @Test
    void updateInfoRestrictedAttrIsActive() throws IOException {
        //arrange
        Long id = 3L;
        String patchContent = """
                [
                    {"op":"replace","path":"/active","value":false}
                ]
                """;
        JsonPatch patch = JsonPatch.fromJson(new ObjectMapper().readTree(patchContent));

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act and assert
        assertThrows(JsonPatchException.class, ()->editProfileService.updateVendorInfo(id, patch));
    }
}
