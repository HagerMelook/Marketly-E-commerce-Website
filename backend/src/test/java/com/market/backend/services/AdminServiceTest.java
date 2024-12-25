package com.market.backend.services;

import com.market.backend.models.*;
import com.market.backend.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordRepository passwordRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private VendorRequestRepository requestRepository;

    @InjectMocks
    private AdminService adminService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = Account.builder()
                .id(1L)
                .isActive(true)
                .username("client2")
                .type("client")
                .authType("basic")
                .build();
    }

    @Test
    void getAccountInfoByUserName() {
        when(accountRepository.findByUsername("client2")).thenReturn(Optional.of(testAccount));

        Account result = adminService.getAccountInfoByUserName("client2");

        assertEquals(testAccount, result);
        verify(accountRepository).findByUsername("client2");
    }

    @Test
    void getAccountInfoByInvalidUserName() {
        when(accountRepository.findByUsername("invalid username")).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                adminService.getAccountInfoByUserName("invalid username"));
        assertEquals("User not found", exception.getMessage());

        verify(accountRepository).findByUsername("invalid username");
    }


    @Test
    void testDeleteAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        adminService.deleteAccount(1L);

        verify(accountRepository).findById(1L);
        verify(clientRepository).deleteById(1L);
        verify(passwordRepository).deleteById(1L);
        verify(accountRepository).delete(testAccount);

        verifyNoInteractions(adminRepository, vendorRepository);
    }

    @Test
    void testDeleteAccount_UserNotFound() {
        when(accountRepository.findById(testAccount.getId()))
                .thenReturn(java.util.Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                adminService.deleteAccount(testAccount.getId()));

        assertEquals("User not found", exception.getMessage());

        verify(accountRepository).findById(testAccount.getId());
        verifyNoMoreInteractions(accountRepository, adminRepository, clientRepository, vendorRepository, passwordRepository);
    }

    @Test
    void testChangeAccountStatus_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        adminService.changeAccountStatus(true, 1L);

        assertTrue(testAccount.isActive());
        verify(accountRepository).findById(1L);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void testChangeAccountStatus_UserNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                adminService.changeAccountStatus(true, 1L));
        assertEquals("User not found", exception.getMessage());

        verify(accountRepository).findById(1L);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void testPromoteUser() {
        Client client = Client.builder()
                    .firstName("john")
                    .lastName("dan")
                    .account(testAccount)
                    .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(clientRepository.existsById(1L)).thenReturn(true);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        adminService.promoteAccount(1L);

        assertEquals("admin", testAccount.getType());
        verify(accountRepository).findById(1L);
        verify(clientRepository).existsById(1L);
        verify(clientRepository).findById(1L);
        verify(clientRepository).delete(any(Client.class));
        verify(adminRepository).save(any(Admin.class));
        verifyNoMoreInteractions(accountRepository, clientRepository);
    }

    @Test
    void testDemoteUser() {
        testAccount.setType("admin");

        Admin admin = Admin.builder()
                .account(testAccount)
                .firstName("ahmed")
                .lastName("ahmed")
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(adminRepository.existsById(1L)).thenReturn(true);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        adminService.demoteAccount(1L);

        assertEquals("client", testAccount.getType());

        verify(accountRepository).findById(1L);
        verify(adminRepository).existsById(1L);
        verify(adminRepository).findById(1L);
        verify(adminRepository).deleteById(1L);
        verify(clientRepository).save(any(Client.class));
        verifyNoMoreInteractions(accountRepository, clientRepository);
    }

    @Test
     void TestGetFeedbacks(){
        Feedback feedback1 = new Feedback(1L, "Great product!");
        Feedback feedback2 = new Feedback(2L, "Needs improvement.");
        List<Feedback> feedbacks = List.of(feedback1, feedback2);

        when(feedbackRepository.findAll()).thenReturn(feedbacks);

        List<Feedback> result = adminService.getFeedbacks();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Great product!", result.get(0).getBody());
        Assertions.assertEquals("Needs improvement.", result.get(1).getBody());
    }

    @Test
    void TestDeleteFeedback(){
        long feedbackId = 1L;
        adminService.deleteFeedback(feedbackId);
        verify(feedbackRepository, times(1)).deleteById(feedbackId);
    }

//    @Test
//    void TestDeleteFeedback2(){
//        Feedback feedback = new Feedback(1L, "Great product!");
//        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
//        adminService.deleteFeedback(1L);
//
//    }

    @Test
    void TestGetVendorRequests(){
        VendorRequest vendorRequest1 = VendorRequest.builder()
                .id(1L)
                .password("password")
                .username("vendor1")
                .organizationName("Vendor One")
                .taxNumber(123L)
                .authType("basic")
                .build();

        VendorRequest vendorRequest2 = VendorRequest.builder()
                .id(2L)
                .password("password2")
                .username("vendor2")
                .organizationName("Vendor Two")
                .taxNumber(456L)
                .authType("basic")
                .build();

        List<VendorRequest> requests = List.of(vendorRequest1, vendorRequest2);

        when(requestRepository.findAll()).thenReturn(requests);

        List<VendorRequest> result = adminService.getVendorRequests();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("vendor1", result.get(0).getUsername());
        Assertions.assertEquals("vendor2", result.get(1).getUsername());
    }

    @Test
    void TestAddVendor() {
        VendorRequest vendorRequest = VendorRequest.builder()
                .id(1L)
                .password("password")
                .username("vendor1")
                .organizationName("Vendor One")
                .taxNumber(123L)
                .authType("basic")
                .build();

        Account account = Account.builder()
                .isActive(true)
                .type("vendor")
                .username("vendor1")
                .authType("basic")
                .build();

        Vendor vendor = Vendor.builder()
                .organizationName("Vendor One")
                .taxNumber(123L)
                .account(account)
                .build();

        when(requestRepository.findById(1L)).thenReturn(Optional.of(vendorRequest));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        adminService.addVendor(1L);

        verify(requestRepository, times(1)).delete(vendorRequest);
        verify(accountRepository, times(1)).save(account);
        verify(vendorRepository, times(1)).save(argThat(savedVendor ->
                savedVendor.getId() == null && // ID is null before saving
                        savedVendor.getOrganizationName().equals("Vendor One") &&
                        savedVendor.getTaxNumber().equals(123L) &&
                        savedVendor.getAccount().equals(account)
        ));
    }


    @Test
    void TestDeclineVendorRequest(){
        long requestId = 1L;
        adminService.declineVendorRequest(requestId);
        verify(requestRepository, times(1)).deleteById(requestId);
    }
}
