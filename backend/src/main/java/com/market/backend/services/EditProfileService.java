package com.market.backend.services;

import com.market.backend.dtos.AdminInfoDTO;
import com.market.backend.dtos.ClientInfoDTO;
import com.market.backend.dtos.VendorInfoDTO;
import com.market.backend.models.*;
import com.market.backend.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EditProfileService {
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final VendorRepository vendorRepository;
    private final AccountRepository accountRepository;

    private final ShippingInfoRepository shippingInfoRepository;

    public EditProfileService(AdminRepository adminRepository, ClientRepository clientRepository, VendorRepository vendorRepository, AccountRepository accountRepository, ShippingInfoRepository shippingInfoRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.vendorRepository = vendorRepository;
        this.accountRepository = accountRepository;
        this.shippingInfoRepository = shippingInfoRepository;
    }

    @Transactional
    public AdminInfoDTO getAdminInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Optional<Admin> adminOptional = adminRepository.findByAccount_Id(id);
        if (!adminOptional.isPresent()) throw new NoSuchElementException("User not found");
        return new AdminInfoDTO(account, adminOptional.get());
    }

    @Transactional
    public ClientInfoDTO getClientInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Optional<Client> clientOptional = clientRepository.findByAccount_Id(id);
        if (!clientOptional.isPresent()) throw new NoSuchElementException("User not found");
        Optional<ShippingInfo> shippingInfoOptional = shippingInfoRepository.findByAccount_Id(id);
        if (!shippingInfoOptional.isPresent()) throw new NoSuchElementException("User not found");
        return new ClientInfoDTO(account, clientOptional.get(), shippingInfoOptional.get());
    }

    @Transactional
    public VendorInfoDTO getVendorInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Optional<Vendor> vendorOptional = vendorRepository.findByAccount_Id(id);
        if (!vendorOptional.isPresent()) throw new NoSuchElementException("User not found");
        Optional<ShippingInfo> shippingInfoOptional = shippingInfoRepository.findByAccount_Id(id);
        if (!shippingInfoOptional.isPresent()) throw new NoSuchElementException("User not found");
        return new VendorInfoDTO(account, vendorOptional.get(), shippingInfoOptional.get());
    }

    @Transactional
    public boolean updateAccount(Long id, Account newAccount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (account!=null && !account.getType().equals("admin")) {
            accountRepository.save(newAccount);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateAdminFirstName(Long id, String newFirstName) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (admin!=null) {
            admin.setFirstName(newFirstName);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateAdminLastName(Long id, String newLastName) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (admin!=null) {
            admin.setLastName(newLastName);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateClientFirstName(Long id, String newFirstName) {
        Optional<Client> clientOptional = clientRepository.findByAccount_Id(id);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setFirstName(newFirstName);
            clientRepository.save(client);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateClientLastName(Long id, String newLastName) {
        Optional<Client> clientOptional = clientRepository.findByAccount_Id(id);

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setLastName(newLastName);
            clientRepository.save(client);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateVendorOrgName(Long id, String newOrgName) {
        Optional<Vendor> vendorOptional = vendorRepository.findByAccount_Id(id);

        if (vendorOptional.isPresent()) {
            Vendor vendor = vendorOptional.get();
            vendor.setOrganisationName(newOrgName);
            vendorRepository.save(vendor);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateVendorTaxNumber(Long id, String newTaxNumber) {
        Optional<Vendor> vendorOptional = vendorRepository.findByAccount_Id(id);

        if (vendorOptional.isPresent()) {
            Vendor vendor = vendorOptional.get();
            vendor.setTaxNumber(newTaxNumber);
            vendorRepository.save(vendor);
            return true;
        }
        return false;
    }
}