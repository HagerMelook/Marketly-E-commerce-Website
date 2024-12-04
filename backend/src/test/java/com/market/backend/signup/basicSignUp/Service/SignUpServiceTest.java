package com.market.backend.signup.basicSignUp.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.market.backend.signup.basicSignUp.Model.Account;
import com.market.backend.signup.basicSignUp.Model.VendorRequests;
import com.market.backend.signup.basicSignUp.Repository.AccountRepository;
import com.market.backend.signup.basicSignUp.Repository.VendorRequestsRepository;

@SpringBootTest
public class SignUpServiceTest {
    
    @InjectMocks
    private SignUpService signUpService;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private VendorRequestsRepository venReqRepo;

    @Test
    void testCheckGoogleAccountExistanceInAccount() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("email", "hagermelook123@gmail.com");
        attributes.put("name", "Hager Melook");

        when(accountRepo.existsByEmail(("hagermelook123@gmail.com").toString())).thenReturn(true);

        boolean result = signUpService.checkGoogleAccountExistance(attributes, false);
        assertFalse(result);

    }

    @Test
    void testCheckGoogleAccountExistanceInVendorReq() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("email", "hagermelook123@gmail.com");
        attributes.put("name", "Hager Melook");

        when(accountRepo.existsByEmail(("hagermelook123@gmail.com").toString())).thenReturn(false);
        when(venReqRepo.existsByEmail(("hagermelook123@gmail.com").toString())).thenReturn(true);
        boolean result = signUpService.checkGoogleAccountExistance(attributes, true);
        assertFalse(result);
    }

    @Test
    void testCheckGoogleAccountNotExistance() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("email", "hagermelook123@gmail.com");
        attributes.put("name", "Hager Melook");

        when(accountRepo.existsByEmail(("hagermelook123@gmail.com").toString())).thenReturn(false);
        when(venReqRepo.existsByEmail(("hagermelook123@gmail.com").toString())).thenReturn(false);
        boolean result = signUpService.checkGoogleAccountExistance(attributes,false);
        assertTrue(result);
    }


    @Test
    void testInsertBasicClientCaseExist1() {
        Account basicClient = new Account(null,"Hager Melook","client",true);
        basicClient.setPassword("123");
        when(accountRepo.existsByUsername("Hager Melook")).thenReturn(true);
        String result = signUpService.insertBasicClient(basicClient);
        assertEquals(result, "The username is already exist");
    }

    @Test
    void testInsertBasicClientCaseNotExist() {
        Account basicClient = new Account(null,"Hager Mohammed","client",true);
        basicClient.setPassword("123");
        when(accountRepo.existsByUsername("Hager Melook")).thenReturn(true);
        String result = signUpService.insertBasicClient(basicClient);
        assertEquals("Successfully registered",result);
    }

    @Test
    void testInsertBasicClientCaseNullUsername() {
        Account basicClient = new Account(null,null,"client",true);
        basicClient.setPassword("123");
        String result = signUpService.insertBasicClient(basicClient);
        assertEquals("The username can't be empty",result);
    }

    @Test
    void testInsertBasicClientCaseNullPassword() {
        Account basicClient = new Account(null,"ala","client",true);
        basicClient.setPassword(null);
        String result = signUpService.insertBasicClient(basicClient);
        assertEquals("The password can't be empty",result);
    }

    @Test
    void testInsertBasicClientCaseLongUsername() {
        Account basicClient = new Account(null,"alaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","client",true);
        basicClient.setPassword("123");
        String result = signUpService.insertBasicClient(basicClient);
        assertEquals("The username can't be more than 80 character",result);
    }
    @Test
    void testInsertBasicClientCaseLongPassword() {
        Account basicClient = new Account(null,"ala","client",true);
        basicClient.setPassword("123333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        String result = signUpService.insertBasicClient(basicClient);
        assertEquals("The password can't be more than 80 character",result);
    }

    @Test
    void testInsertBasicVendorCaseExist1() {
        VendorRequests basicVendor = new VendorRequests(null, "ahmed", "sala", 712345123);
        basicVendor.setPassword("123");
        when(venReqRepo.existsByorganizationName("sala")).thenReturn(true);
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("The business name is already exist",result);
    }

    @Test
    void testInsertBasicVendorCaseExist2() {
        VendorRequests basicVendor = new VendorRequests(null, "ahmed", "sala", 812345123);
        basicVendor.setPassword("123");
        when(venReqRepo.existsByorganizationName("sala")).thenReturn(false);
        when(venReqRepo.existsBytaxNumber(812345123L)).thenReturn(true);
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("The tax number is already exist",result);
    }

    @Test
    void testInsertBasicVendorCaseNotExist() {
        VendorRequests basicVendor = new VendorRequests(null, "ahmed", "sala", 712345123);
        basicVendor.setPassword("123");
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("Successfully registered",result);
    }

    @Test
    void testInsertBasicVendorCaseNullOrganizationName() {
        VendorRequests basicVendor = new VendorRequests(null, "ahmed", null, 012345456);
        basicVendor.setPassword("123");
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("The business name can't be empty",result);
    }

    @Test
    void testInsertBasicVendorCaseNullTaxNumber() {
        VendorRequests basicVendor = new VendorRequests(null, "ahmed", "sala", -1);
        basicVendor.setPassword("123");
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("The tax number can't be empty",result);
    }

    @Test
    void testInsertBasicVendorCaseNullPassword() {
        VendorRequests basicVendor = new VendorRequests(null, "ahmed", "sala", 110111111);
        basicVendor.setPassword(null);
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("The password can't be empty",result);
    }

    @Test
    void testInsertBasicVendorCaseLongOrganizationName() {
        VendorRequests basicVendor = new VendorRequests(null,"ahmed","salaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",123456789);
        basicVendor.setPassword("123");
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("The business name can't be more than 80 character",result);
    }
    @Test
    void testInsertBasicVendorCaseTaxnumbernot9() {
        VendorRequests basicVendor = new VendorRequests(null, "ahmed", "sala", 012345);
        basicVendor.setPassword("123");
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("The tax number must be of 9 numbers only",result);
    }

    @Test
    void testInsertBasicVendorCaseLongPassword() {
        VendorRequests basicVendor = new VendorRequests(null, "ahmed", "sala", 012345444);
        basicVendor.setPassword("1233333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        String result = signUpService.insertBasicVendor(basicVendor);
        assertEquals("The password can't be more than 80 character",result);
    }

    @Test
    void testInsertClientByGoogleAccountExist() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("email", "hagermelook123@gmail.com");
        attributes.put("name", "Hager Melook");

        String result = signUpService.insertClientByGoogleAccount(attributes);
        assertEquals("Google Account is Already Registered", result);
    }

    @Test
    void testInsertClientByGoogleAccountNotExist() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("email", "hagerme123@gmail.com");
        attributes.put("name", "Hager Melook");

        String result = signUpService.insertClientByGoogleAccount(attributes);
        assertEquals("Client Registered Successfully", result);
    }

    @Test
    void testRegisterVendorRequestExist() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("email", "hagermelook123@gmail.com");
        attributes.put("name", "Hager Melook");

        String result = signUpService.registerVendorRequest(attributes,"new_mark",222333444);
        assertEquals("Already Used Gmail Account", result);
    }

    @Test
    void testRegisterVendorRequestNotEXist() {
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("email", "hagermelook123@gmail.com");
        attributes.put("name", "Hager Melook");

        String result = signUpService.registerVendorRequest(attributes,"new_mark",222333444);
        assertEquals("Request Registered Successfully", result);
    }
}
