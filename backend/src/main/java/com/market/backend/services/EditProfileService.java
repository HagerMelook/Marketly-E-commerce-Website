package com.market.backend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.market.backend.dtos.AdminInfoDTO;
import com.market.backend.dtos.ClientInfoDTO;
import com.market.backend.dtos.VendorInfoDTO;
import com.market.backend.models.*;
import com.market.backend.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class EditProfileService {
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final VendorRepository vendorRepository;
    private final AccountRepository accountRepository;
    private final PasswordRepository passwordRepository;
    private final ShippingInfoRepository shippingInfoRepository;

    private final ObjectMapper objectMapper;

    public EditProfileService(AdminRepository adminRepository, ClientRepository clientRepository, VendorRepository vendorRepository, AccountRepository accountRepository, PasswordRepository passwordRepository, ShippingInfoRepository shippingInfoRepository, ObjectMapper objectMapper) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.vendorRepository = vendorRepository;
        this.accountRepository = accountRepository;
        this.passwordRepository = passwordRepository;
        this.shippingInfoRepository = shippingInfoRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public AdminInfoDTO getAdminInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!account.getType().contains("admin")) {
            throw new NoSuchElementException("User not found");
        }

        Password password = account.getAuthType().equalsIgnoreCase("basic") ?
                passwordRepository.findByAccountId(id) : null;
        Admin admin = adminRepository.findById(id)
                .orElse(new Admin());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccountId(id)
                .orElse(new ShippingInfo());
        return new AdminInfoDTO(account, password, admin, shippingInfo);
    }

    @Transactional
    public ClientInfoDTO getClientInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!account.getType().contains("client")) {
            throw new NoSuchElementException("User not found");
        }

        Password password = account.getAuthType().equalsIgnoreCase("basic") ?
                passwordRepository.findByAccountId(id) : null;
        Client client = clientRepository.findByAccount_Id(id)
                .orElse(new Client());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccountId(id)
                .orElse(new ShippingInfo());
        return new ClientInfoDTO(account, password, client, shippingInfo);
    }

    @Transactional
    public VendorInfoDTO getVendorInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!account.getType().equals("vendor")) {
            throw new NoSuchElementException("User not found");
        }

        Password password = account.getAuthType().equalsIgnoreCase("basic") ?
                passwordRepository.findByAccountId(id) : null;
        Vendor vendor = vendorRepository.findById(id)
                .orElse(new Vendor());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccountId(id)
                .orElse(new ShippingInfo());
        return new VendorInfoDTO(account, password, vendor, shippingInfo);
    }

    @Transactional
    public void updateAdminInfo(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!account.getType().contains("admin")) {
            throw new NoSuchElementException("User not found");
        }

        Password password = account.getAuthType().equalsIgnoreCase("basic") ?
                passwordRepository.findByAccountId(id) : null;
        Admin admin = adminRepository.findById(id)
                .orElse(new Admin());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccountId(id)
                .orElse(new ShippingInfo());

        AdminInfoDTO adminInfoDTO = new AdminInfoDTO(account, password, admin, shippingInfo);
        JsonNode patched = patch.apply(objectMapper.convertValue(adminInfoDTO, JsonNode.class));
        adminInfoDTO = objectMapper.treeToValue(patched, AdminInfoDTO.class);

        if(!adminInfoDTO.getAccountId().equals(account.getId())
                || !adminInfoDTO.getType().equals(account.getType())
                || !adminInfoDTO.getUsername().equals(account.getUsername())
                || adminInfoDTO.isActive()!=(account.isActive())) {

            throw new JsonPatchException("Restricted attribute");
        }

        if (password!=null) {
            password.setAccountPassword(adminInfoDTO.getPassword());
        }
        else if (adminInfoDTO.getPassword()!=null) {
            throw new JsonPatchException("Restricted attribute");
        }

        admin.setLastName(adminInfoDTO.getLastName());
        admin.setFirstName(adminInfoDTO.getFirstName());
        adminRepository.save(admin);

        shippingInfo.setAddress(adminInfoDTO.getAddress());
        shippingInfo.setPhoneNumber(adminInfoDTO.getPhone());
        shippingInfoRepository.save(shippingInfo);
    }

    @Transactional
    public void updateVendorInfo(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!account.getType().equals("vendor")) {
            throw new NoSuchElementException("User not found");
        }

        Password password = account.getAuthType().equalsIgnoreCase("basic") ?
                passwordRepository.findByAccountId(id) : null;
        Vendor vendor = vendorRepository.findById(id)
                .orElse(new Vendor());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccountId(id)
                .orElse(new ShippingInfo());

        VendorInfoDTO vendorInfoDTO = new VendorInfoDTO(account, password, vendor, shippingInfo);
        JsonNode patched = patch.apply(objectMapper.convertValue(vendorInfoDTO, JsonNode.class));
        vendorInfoDTO = objectMapper.treeToValue(patched, VendorInfoDTO.class);

        if(!vendorInfoDTO.getAccountId().equals(account.getId())
                || !vendorInfoDTO.getType().equals(account.getType())
                || !vendorInfoDTO.getUsername().equals(account.getUsername())
                || vendorInfoDTO.isActive()!=(account.isActive())) {

            throw new JsonPatchException("Restricted attribute");
        }

        if (password!=null) {
            password.setAccountPassword(vendorInfoDTO.getPassword());
        }
        else if (vendorInfoDTO.getPassword()!=null) {
            throw new JsonPatchException("Restricted attribute");
        }

        vendor.setOrganizationName(vendorInfoDTO.getOrganizationName());
        vendor.setTaxNumber(vendorInfoDTO.getTaxNumber());

        shippingInfo.setAddress(vendorInfoDTO.getAddress());
        shippingInfo.setPhoneNumber(vendorInfoDTO.getPhone());
    }

    @Transactional
    public void updateClientInfo(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!account.getType().equals("client")) {
            throw new NoSuchElementException("User not found");
        }

        Password password = account.getAuthType().equalsIgnoreCase("basic") ?
                passwordRepository.findByAccountId(id) : null;
        Client client = clientRepository.findByAccount_Id(id)
                .orElse(new Client());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccountId(id)
                .orElse(new ShippingInfo());

        ClientInfoDTO clientInfoDTO = new ClientInfoDTO(account, password, client, shippingInfo);
        JsonNode patched = patch.apply(objectMapper.convertValue(clientInfoDTO, JsonNode.class));
        clientInfoDTO = objectMapper.treeToValue(patched, ClientInfoDTO.class);

        if(!clientInfoDTO.getAccountId().equals(account.getId())
                || !clientInfoDTO.getType().equals(account.getType())
                || !clientInfoDTO.getUsername().equals(account.getUsername())
                || clientInfoDTO.isActive()!=(account.isActive())) {

            throw new JsonPatchException("Restricted attribute");
        }

        if (password!=null) {
            password.setAccountPassword(clientInfoDTO.getPassword());
        }
        else if (clientInfoDTO.getPassword()!=null) {
            throw new JsonPatchException("Restricted attribute");
        }

        client.setLastName(clientInfoDTO.getLastName());
        client.setFirstName(clientInfoDTO.getFirstName());

        shippingInfo.setAddress(clientInfoDTO.getAddress());
        shippingInfo.setPhoneNumber(clientInfoDTO.getPhone());
    }
}