package com.market.backend.services;

import com.market.backend.models.Account;
import com.market.backend.models.Admin;
import com.market.backend.models.Client;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.AdminRepository;
import com.market.backend.repositories.ClientRepository;
import com.market.backend.repositories.VendorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {
    @Autowired
    private AdminService adminService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void getAccountInfoByUserName() {
        Account account = new Account("client2@x.com", "password", true, "client", "client2");
        Client client = new Client("john", "dann", account);
        clientRepository.save(client);

        assertEquals(adminService.getAccountInfoByUserName("client2"), account);
    }

    @Test
    void getAccountInfoByInvalidUserName() {
        assertThrows(NoSuchElementException.class, () -> {
            adminService.getAccountInfoByUserName("dslfsdo");
        });
    }

    @Test
    void getAccountInfoByEmail() {
        Account account = new Account("client3@x.com", "password", true, "client", "client3");
        Client client = new Client("john", "dann", account);
        clientRepository.save(client);

        assertEquals(adminService.getAccountInfoByEmail("client3@x.com"), account);
    }

    @Test
    void testGetAccountInfoByInvalidEmail() {
        assertThrows(NoSuchElementException.class, () -> {
            adminService.getAccountInfoByEmail("dslfsdo@a.com");
        });
    }

    @Test
    void testDeleteAccount() {
        Account account = new Account("client3@x.com", "password", true, "client", "client3");

        Account savedAccount = accountRepository.save(account);
        adminService.deleteAccount(savedAccount.getId());

        assertFalse(accountRepository.findById(savedAccount.getId()).isPresent());
    }

    @Test
    void testDeleteInvalidAccount() {
        assertThrows(NoSuchElementException.class, () -> {
            adminService.deleteAccount(25654L);
        });
    }



    @Test
    void testActivateUser() {
        Account account = new Account("client4@x.com", "password", false, "client", "client4");
        Account savedAccount = accountRepository.save(account);
        adminService.changeAccountStatus("activate", savedAccount.getId());
        assertTrue(accountRepository.findById(savedAccount.getId()).get().isActive());
    }

    @Test
    void testDeactivateUser() {
        Account account = new Account("client5@x.com", "password", true, "client", "client5");
        Account savedAccount = accountRepository.save(account);
        adminService.changeAccountStatus("deactivate", savedAccount.getId());
        assertFalse(accountRepository.findById(savedAccount.getId()).get().isActive());
    }

    @Test
    void testPromoteUser() {
        Account account = new Account("client6@x.com", "password", true, "client", "client6");
        Client client = new Client("john", "dann", account);
        Client savedClient = clientRepository.save(client);
        adminService.promoteAccount(savedClient.getAccount().getId());
        assertTrue(adminRepository.findByAccount_Id(savedClient.getAccount().getId()).isPresent());
    }

    @Test
    void testDemoteUser() {
        Account account = new Account("admin8@x.com", "password", true, "admin", "admin8");
        Admin admin = new Admin("ahmed", "a", account);
        Admin savedAdmin = adminRepository.save(admin);
        adminService.demoteAccount(savedAdmin.getAccount().getId());
        assertTrue(clientRepository.findByAccount_Id(savedAdmin.getAccount().getId()).isPresent());

    }
}
