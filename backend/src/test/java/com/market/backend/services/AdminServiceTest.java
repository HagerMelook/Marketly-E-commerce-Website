package com.market.backend.services;

import com.market.backend.models.Account;
import com.market.backend.models.Admin;
import com.market.backend.models.Client;
import com.market.backend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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
        when(accountRepository.findByUsername("dslfsdo")).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () ->
                adminService.getAccountInfoByUserName("dslfsdo"));
        assertEquals("User not found", exception.getMessage());

        verify(accountRepository).findByUsername("dslfsdo");
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
}
