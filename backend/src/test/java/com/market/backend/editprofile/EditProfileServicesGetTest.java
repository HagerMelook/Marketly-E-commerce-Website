package com.market.backend.editprofile;

import com.market.backend.dtos.AdminInfoDTO;
import com.market.backend.dtos.ClientInfoDTO;
import com.market.backend.dtos.VendorInfoDTO;
import com.market.backend.models.*;
import com.market.backend.repositories.*;
import com.market.backend.services.EditProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EditProfileServicesGetTest {

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
            new ShippingInfo(3L, accounts[2], "vendoraddress", "vendorname", "123"),
            new ShippingInfo(4L,  accounts[3], "adminaddress", "adminphone", "123"),
            new ShippingInfo(5L, accounts[4], "clientaddress", "clientphone", "123"),
            new ShippingInfo(6L, accounts[5], "vendoraddress", "vendorname", "123")
    };

    @Test
    void getAdminInfoValidId() {
        //arrange
        Long id = 1L;
        AdminInfoDTO expected = new AdminInfoDTO(accounts[0], passwords[0], admins[0], shippingInfos[0]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[0]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[0]));

        //act
        AdminInfoDTO adminInfoDTO = editProfileService.getAdminInfo(id);

        //assert
        assertEquals(expected, adminInfoDTO);
    }

    @Test
    void getAdminInfoValidIdEmptyAdmin() {
        //arrange
        Long id = 1L;
        AdminInfoDTO expected = new AdminInfoDTO(accounts[0], passwords[0], new Admin(), shippingInfos[0]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(new Admin()));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[0]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[0]));

        //act
        AdminInfoDTO adminInfoDTO = editProfileService.getAdminInfo(id);

        //assert
        assertEquals(expected, adminInfoDTO);
    }

    @Test
    void getAdminInfoValidIdEmptyShippingInfo() {
        //arrange
        Long id = 1L;
        AdminInfoDTO expected = new AdminInfoDTO(accounts[0], passwords[0], admins[0], new ShippingInfo());

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[0]));
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admins[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[0]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(new ShippingInfo()));

        //act
        AdminInfoDTO adminInfoDTO = editProfileService.getAdminInfo(id);

        //assert
        assertEquals(expected, adminInfoDTO);
    }

    @Test
    void getAdminInfoValidIdOauth() {
        //arrange
        Long id = 4L;
        AdminInfoDTO expected = new AdminInfoDTO(accounts[3], null, admins[1], shippingInfos[3]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[3]));
        Mockito.when(adminRepository.findById(id)).thenReturn(Optional.of(admins[1]));
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[3]));

        //act
        AdminInfoDTO adminInfoDTO = editProfileService.getAdminInfo(id);

        //assert
        assertEquals(expected, adminInfoDTO);
    }

    @Test
    void getAdminInfoInvalidId() {
        //arrange
        Long id = 1000L;

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //act and assert
        assertThrows(NoSuchElementException.class, ()-> editProfileService.getAdminInfo(id));
    }

    @Test
    void getClientInfoValidId() {
        //arrange
        Long id = 2L;
        ClientInfoDTO expected = new ClientInfoDTO(accounts[1], passwords[1], clients[0], shippingInfos[1]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[1]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[1]));

        //act
        ClientInfoDTO clientInfoDTO = editProfileService.getClientInfo(id);

        //assert
        assertEquals(expected, clientInfoDTO);
    }

    @Test
    void getClientInfoValidIdEmptyClient() {
        //arrange
        Long id = 2L;
        ClientInfoDTO expected = new ClientInfoDTO(accounts[1], passwords[1], new Client(), shippingInfos[1]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(new Client()));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[1]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[1]));

        //act
        ClientInfoDTO clientInfoDTO = editProfileService.getClientInfo(id);

        //assert
        assertEquals(expected, clientInfoDTO);
    }

    @Test
    void getClientInfoValidIdEmpytShippingInfo() {
        //arrange
        Long id = 2L;
        ClientInfoDTO expected = new ClientInfoDTO(accounts[1], passwords[1], clients[0], new ShippingInfo());

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[1]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(new ShippingInfo()));

        //act
        ClientInfoDTO clientInfoDTO = editProfileService.getClientInfo(id);

        //assert
        assertEquals(expected, clientInfoDTO);
    }

    @Test
    void getClientInfoValidIdOauth() {
        //arrange
        Long id = 5L;
        ClientInfoDTO expected = new ClientInfoDTO(accounts[4], null, clients[1], shippingInfos[4]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[4]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.of(clients[1]));
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[4]));

        //act
        ClientInfoDTO clientInfoDTO = editProfileService.getClientInfo(id);

        //assert
        assertEquals(expected, clientInfoDTO);
    }

    @Test
    void getClientInfoInvalidId() {
        //arrange
        Long id = 1000L;

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //act and assert
        assertThrows(NoSuchElementException.class, ()-> editProfileService.getClientInfo(id));
    }

    @Test
    void getVendorInfoValidId() {
        //arrange
        Long id = 3L;
        VendorInfoDTO expected = new VendorInfoDTO(accounts[2], passwords[2], vendors[0], shippingInfos[2]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act
        VendorInfoDTO vendorInfoDTO = editProfileService.getVendorInfo(id);

        //assert
        assertEquals(expected, vendorInfoDTO);
    }

    @Test
    void getVendorInfoValidIdEmptyVendor() {
        //arrange
        Long id = 3L;
        VendorInfoDTO expected = new VendorInfoDTO(accounts[2], passwords[2], new Vendor(), shippingInfos[2]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(new Vendor()));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[2]));

        //act
        VendorInfoDTO vendorInfoDTO = editProfileService.getVendorInfo(id);

        //assert
        assertEquals(expected, vendorInfoDTO);
    }

    @Test
    void getVendorInfoValidIdEmptyShippingInfo() {
        //arrange
        Long id = 3L;
        VendorInfoDTO expected = new VendorInfoDTO(accounts[2], passwords[2], vendors[0], new ShippingInfo());

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[2]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[0]));
        Mockito.when(passwordRepository.findByAccountId(id)).thenReturn(passwords[2]);
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(new ShippingInfo()));

        //act
        VendorInfoDTO vendorInfoDTO = editProfileService.getVendorInfo(id);

        //assert
        assertEquals(expected, vendorInfoDTO);
    }

    @Test
    void getVendorInfoValidIdOauth() {
        //arrange
        Long id = 6L;
        VendorInfoDTO expected = new VendorInfoDTO(accounts[5], null, vendors[1], shippingInfos[5]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.of(accounts[5]));
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendors[1]));
        Mockito.when(shippingInfoRepository.findByAccountId(id)).thenReturn(Optional.of(shippingInfos[5]));

        //act
        VendorInfoDTO vendorInfoDTO = editProfileService.getVendorInfo(id);

        //assert
        assertEquals(expected, vendorInfoDTO);
    }


    @Test
    void getVendorInfoInvalidId() {
        //arrange
        Long id = 1000L;

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());

        //act and assert
        assertThrows(NoSuchElementException.class, ()-> editProfileService.getAdminInfo(id));
    }
}
