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
        Account account = Account.builder()
                .isActive(true)
                .username("client2")
                .type("client")
                .authType("OAuth")
                .build();

        Client client = Client.builder()
                .firstName("john")
                .lastName("dann")
                .account(account)
                .build();

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
    void testDeleteAccount() {
        Account account = Account.builder()
                .isActive(true)
                .type("client")
                .authType("OAuth")
                .username("client3")
                .build();

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
        Account account = Account.builder()
                .isActive(false)
                .type("client")
                .username("client4")
                .authType("OAuth")
                .build();

        Account savedAccount = accountRepository.save(account);
        adminService.changeAccountStatus(true, savedAccount.getId());
        assertTrue(accountRepository.findById(savedAccount.getId()).get().isActive());
    }

    @Test
    void testDeactivateUser() {
        Account account = Account.builder()
                .isActive(true)
                .type("client")
                .authType("OAuth")
                .username("client5")
                .build();
        Account savedAccount = accountRepository.save(account);
        adminService.changeAccountStatus(false, savedAccount.getId());
        assertFalse(accountRepository.findById(savedAccount.getId()).get().isActive());
    }

    @Test
    void testPromoteUser() {
        Account account = Account.builder()
                .isActive(true)
                .type("client")
                .authType("OAuth")
                .username("client6")
                .build();
        Client client = new Client("john", "dann", account);
        Client savedClient = clientRepository.save(client);
        adminService.promoteAccount(savedClient.getAccount().getId());
        assertTrue(adminRepository.findById(savedClient.getAccount().getId()).isPresent());
    }

    @Test
    void testDemoteUser() {
        Account account = Account.builder()
                .isActive(true)
                .type("admin")
                .authType("Basic")
                .username("admin8")
                .build();

        Admin admin = Admin.builder()
                .account(account)
                .firstName("ahmed")
                .lastName("a")
                .build();

        Admin savedAdmin = adminRepository.save(admin);
        adminService.demoteAccount(savedAdmin.getAccount().getId());
        assertTrue(clientRepository.findByAccount_Id(savedAdmin.getAccount().getId()).isPresent());
    }
}
