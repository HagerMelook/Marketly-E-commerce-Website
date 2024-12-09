package com.market.backend;

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
public class EditProfileServicesTest {

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

    Account[] accounts = new Account[] {
            new Account(1L, "email1@example.com", "password1", true, "admin", "user1"),
            new Account(2L, "email2@example.com", "password2", false, "client", "user2"),
            new Account(3L, "email3@example.com", "password3", true, "vendor", "user3")
    };

    Admin[] admins = new Admin[] {
            new Admin(1L, "adminfirst", "adminlast", accounts[0])
    };

    Client[] clients = new Client[] {
            new Client(2L, "clientfirst", "clientlast", accounts[1])
    };

    Vendor[] vendors = new Vendor[] {
            new Vendor(3L, "vendorfirst", "vendorlast", accounts[2])
    };

    ShippingInfo[] shippingInfos = new ShippingInfo[] {
            new ShippingInfo(2L, "clientaddress", "clientphone", accounts[1]),
            new ShippingInfo(3L, "vendoraddress", "vendorname", accounts[2])
    };

    @Test
    void getAdminInfoValidId() {
        //arrange
        Long id = 1L;
        AdminInfoDTO expected = new AdminInfoDTO(accounts[0], admins[0]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.ofNullable(accounts[0]));
        Mockito.when(adminRepository.findByAccount_Id(id)).thenReturn(Optional.ofNullable(admins[0]));

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
        assertThrows(NoSuchElementException.class, ()->{
            editProfileService.getAdminInfo(id);
        });
    }

    @Test
    void getClientInfoValidId() {
        //arrange
        Long id = 2L;
        ClientInfoDTO expected = new ClientInfoDTO(accounts[1], clients[0], shippingInfos[0]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.ofNullable(accounts[1]));
        Mockito.when(clientRepository.findByAccount_Id(id)).thenReturn(Optional.ofNullable(clients[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.ofNullable(shippingInfos[0]));

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
        assertThrows(NoSuchElementException.class, ()->{
            editProfileService.getClientInfo(id);
        });
    }

    @Test
    void getVendorInfoValidId() {
        //arrange
        Long id = 3L;
        VendorInfoDTO expected = new VendorInfoDTO(accounts[2], vendors[0], shippingInfos[1]);

        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.ofNullable(accounts[2]));
        Mockito.when(vendorRepository.findByAccount_Id(id)).thenReturn(Optional.ofNullable(vendors[0]));
        Mockito.when(shippingInfoRepository.findByAccount_Id(id)).thenReturn(Optional.ofNullable(shippingInfos[1]));

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
        assertThrows(NoSuchElementException.class, ()->{
            editProfileService.getAdminInfo(id);
        });
    }
}
