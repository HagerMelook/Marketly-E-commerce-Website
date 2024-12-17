package com.market.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.models.Account;
import com.market.backend.models.Password;
import com.market.backend.models.VendorRequest;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.PasswordRepository;
import com.market.backend.repositories.VendorRequestRepository;

@SpringBootTest
public class SignUpServiceTest {
    @InjectMocks
    private SignUpService signUpService;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private VendorRequestRepository venReqRepo;

    @Mock
    private PasswordRepository passwordRepository;

    @Test
    void testCheckUsernameAvailabilityAccount() {
        when(accountRepo.existsByUsername("hager")).thenReturn(true);

        boolean result = signUpService.checkUsernameAvailability("hager");
        assertFalse(result);
    }

    @Test
    void testCheckUsernameAvailabilityVendor() {
        when(venReqRepo.existsByUsername("hager")).thenReturn(true);

        boolean result = signUpService.checkUsernameAvailability("hager");
        assertFalse(result);
    }

    @Test
    void testCheckUsernameAvailabilityNotExist() {
        when(accountRepo.existsByUsername("hager")).thenReturn(false);
        when(venReqRepo.existsByUsername("hager")).thenReturn(false);

        boolean result = signUpService.checkUsernameAvailability("hager");
        assertTrue(result);
    }
    

    @Test
    void testInsertClientByGoogleAccountNotExist() {
        when(accountRepo.existsByUsername("hagerashraf")).thenReturn(false);
        when(venReqRepo.existsByUsername("hagerashraf")).thenReturn(false);
        String result = signUpService.insertClientByGoogleAccount("hagerashraf@gmail.com");
        assertEquals("Client Registered Successfully", result);
    }

    @Test
    void testInsertClientByGoogleAccountExist() {
        when(accountRepo.existsByUsername("hager129")).thenReturn(true);
        String result = signUpService.insertClientByGoogleAccount("hager129@gmail.com");
        assertEquals("Google Account is Already Registered", result);
    }

    @Test
    void testInsertBasicVendorNullUsername() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername(null);
        venReq.setTaxNumber(111111111L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The username can't be empty",result);
    }

    @Test
    void testInsertBasicVendorNullOrgName() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName(null);
        venReq.setUsername("noon");
        venReq.setTaxNumber(111111111L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The business name can't be empty",result);
    }

    @Test
    void testInsertBasicVendorNullPassword() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noon");
        venReq.setTaxNumber(111111111L);
        venReq.setPassword(null);
        venReq.setAuthType("basic");

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The password can't be empty",result);
    }

    @Test
    void testInsertBasicVendorNullTaxNum() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noon");
        venReq.setTaxNumber(-1L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The tax number can't be empty",result);
    }

    @Test
    void testInsertBasicVendorLongUsername() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noonnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
        venReq.setTaxNumber(111111111L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The username can't be more than 80 character",result);
    }

    @Test
    void testInsertBasicVendorLongOrgName() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noonnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
        venReq.setUsername("noon");
        venReq.setTaxNumber(111111111L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The business name can't be more than 80 character",result);
    }

    @Test
    void testInsertBasicVendorLongPassword() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noon");
        venReq.setTaxNumber(111111111L);
        venReq.setPassword("1233333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        venReq.setAuthType("basic");

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The password can't be more than 80 character",result);
    }

    @Test
    void testInsertBasicVendorTaxNumNot9() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noon");
        venReq.setTaxNumber(11234543L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The tax number must be of 9 numbers only",result);
    }

    @Test
    void testInsertBasicVendorNotExist() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noon");
        venReq.setTaxNumber(111111111L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");
        
        when(venReqRepo.existsByUsername("noon")).thenReturn(false);
        when(venReqRepo.existsByOrganizationName("Noon")).thenReturn(false);
        when(venReqRepo.existsBytaxNumber(456987123L)).thenReturn(false);

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("Successfully registered",result);
    }

    @Test
    void testInsertBasicVendorExistOrgName() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noon");
        venReq.setTaxNumber(111111111L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");
        when(venReqRepo.existsByOrganizationName("Noon")).thenReturn(true);
        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The business name is already exist",result);
    }

    @Test
    void testInsertBasicVendorExistUsername() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noon");
        venReq.setTaxNumber(111111111L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");

        when(venReqRepo.existsByUsername("noon")).thenReturn(true);

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The username is already exist",result);
    }

    @Test
    void testInsertBasicVendorExistTaxNum() {
        VendorRequest venReq = new VendorRequest();
        venReq.setOrganizationName("Noon");
        venReq.setUsername("noon");
        venReq.setTaxNumber(112345432L);
        venReq.setPassword("123");
        venReq.setAuthType("basic");
        
        when(venReqRepo.existsBytaxNumber(112345432L)).thenReturn(true);

        String result = signUpService.insertBasicVendor(venReq);
        assertEquals("The tax number is already exist",result);
    }

    @Test
    void testRegisterVendorRequestExistByUsername() {
        when(venReqRepo.existsByUsername("noon")).thenReturn(true);
        String result = signUpService.registerVendorRequest("noon@gmail.com", "Noon", 456987123);
        assertEquals("Google Account is Already Registered", result);
    }

    @Test
    void testRegisterVendorRequestExistByOrgName() {
        when(accountRepo.existsByUsername("noon")).thenReturn(false);
        when(venReqRepo.existsByUsername("noon")).thenReturn(false);
        when(venReqRepo.existsByOrganizationName("Noon")).thenReturn(true);
        String result = signUpService.registerVendorRequest("noon@gmail.com", "Noon", 456987123);
        assertEquals("Google Account is Already Registered", result);
    }

    @Test
    void testRegisterVendorRequestExistByTaxNum() {
        when(accountRepo.existsByUsername("noon")).thenReturn(false);
        when(venReqRepo.existsByUsername("noon")).thenReturn(false);
        when(venReqRepo.existsByOrganizationName("Noon")).thenReturn(false);
        when(venReqRepo.existsBytaxNumber(456987123L)).thenReturn(true);
        String result = signUpService.registerVendorRequest("noon@gmail.com", "Noon", 456987123);
        assertEquals("Google Account is Already Registered", result);
    }

    @Test
    void testRegisterVendorRequestNotExist() {
        when(accountRepo.existsByUsername("noon")).thenReturn(false);
        when(venReqRepo.existsByUsername("noon")).thenReturn(false);
        when(venReqRepo.existsByOrganizationName("Noon")).thenReturn(false);
        when(venReqRepo.existsBytaxNumber(456987123L)).thenReturn(false);
        String result = signUpService.registerVendorRequest("noon@gmail.com", "Noon", 456987123);
        assertEquals("Request Registered Successfully", result);
    }

    @Test
    void testInsertBasicClientCaseExist() {
        Account client = Account.builder()
        .isActive(true)
        .username("ahmed")
        .authType("basic")
        .type("client")
        .build();
        when(accountRepo.existsByUsername("ahmed")).thenReturn(true);
        String result = signUpService.insertBasicClient(client,"123");
        assertEquals("The username is already exist",result);
    }

    @Test
    void testInsertBasicClientCaseNotExist() {
        Account client = Account.builder()
        .isActive(true)
        .username("ahmed")
        .authType("basic")
        .type("client")
        .build();
        Password password = Password.builder()
        .account(client)
        .accountPassword("123")
        .build();

        when(accountRepo.existsByUsername("ahmed")).thenReturn(false);
        when(passwordRepository.save(password)).thenReturn(password);
        String result = signUpService.insertBasicClient(client,"123");
        assertEquals("Successfully registered",result);
    }

    @Test
    void testInsertBasicClientCaseNullUsername() {
        Account client = Account.builder()
        .isActive(true)
        .username(null)
        .authType("basic")
        .type("client")
        .build();

        String result = signUpService.insertBasicClient(client,"123");
        assertEquals("The username can't be empty",result);
    }

    @Test
    void testInsertBasicClientCaseNullPassword() {
        Account client = Account.builder()
        .isActive(true)
        .username("ahmed")
        .authType("basic")
        .type("client")
        .build();

        String result = signUpService.insertBasicClient(client,null);
        assertEquals("The password can't be empty",result);
    }
    @Test
    void testInsertBasicClientCaseLongPassword() {
        Account client = Account.builder()
        .isActive(true)
        .username("ahmed")
        .authType("basic")
        .type("client")
        .build();

        String result = signUpService.insertBasicClient(client,"1233333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        assertEquals("The password can't be more than 80 character",result);
    }
    
    @Test
    void testInsertBasicClientCaseLongUsername() {
        Account client = Account.builder()
        .isActive(true)
        .username("ahmedddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
        .authType("basic")
        .type("client")
        .build();
        String result = signUpService.insertBasicClient(client,"123");
        assertEquals("The username can't be more than 80 character",result);
    }
}
