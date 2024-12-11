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
import java.util.Objects;

@Service
public class EditProfileService {
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final VendorRepository vendorRepository;
    private final AccountRepository accountRepository;
    private final ShippingInfoRepository shippingInfoRepository;

    private final ObjectMapper objectMapper;

    public EditProfileService(AdminRepository adminRepository, ClientRepository clientRepository, VendorRepository vendorRepository, AccountRepository accountRepository, ShippingInfoRepository shippingInfoRepository, ObjectMapper objectMapper) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.vendorRepository = vendorRepository;
        this.accountRepository = accountRepository;
        this.shippingInfoRepository = shippingInfoRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public AdminInfoDTO getAdminInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Admin admin = adminRepository.findByAccount_Id(id)
                .orElse(new Admin());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccount_Id(id)
                .orElse(new ShippingInfo());
        return new AdminInfoDTO(account, admin, shippingInfo);
    }

    @Transactional
    public ClientInfoDTO getClientInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Client client = clientRepository.findByAccount_Id(id)
                .orElse(new Client());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccount_Id(id)
                .orElse(new ShippingInfo());
        return new ClientInfoDTO(account, client, shippingInfo);
    }

    @Transactional
    public VendorInfoDTO getVendorInfo(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Vendor vendor = vendorRepository.findByAccount_Id(id)
                .orElse(new Vendor());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccount_Id(id)
                .orElse(new ShippingInfo());
        return new VendorInfoDTO(account, vendor, shippingInfo);
    }

    @Transactional
    public void updateAdminInfo(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Admin admin = adminRepository.findByAccount_Id(id)
                .orElse(new Admin());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccount_Id(id)
                .orElse(new ShippingInfo());

        AdminInfoDTO adminInfoDTO = new AdminInfoDTO(account, admin, shippingInfo);
        JsonNode patched = patch.apply(objectMapper.convertValue(adminInfoDTO, JsonNode.class));
        adminInfoDTO = objectMapper.treeToValue(patched, AdminInfoDTO.class);

        if(!adminInfoDTO.getEmail().equals(account.getEmail())
                || !adminInfoDTO.getType().equals(account.getType())
                || !adminInfoDTO.getUsername().equals(account.getUsername())
                || adminInfoDTO.isActive()!=(account.isActive())) {

            throw new JsonPatchException("Restricted attribute");
        }

        account.setId(adminInfoDTO.getAccountId());
        account.setPassword(adminInfoDTO.getPassword());

        admin.setLastName(adminInfoDTO.getLastName());
        admin.setFirstName(adminInfoDTO.getFirstName());

        shippingInfo.setAddress(adminInfoDTO.getAddress());
        shippingInfo.setPhone(adminInfoDTO.getPhone());
    }

    @Transactional
    public void updateVendorInfo(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Vendor vendor = vendorRepository.findByAccount_Id(id)
                .orElse(new Vendor());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccount_Id(id)
                .orElse(new ShippingInfo());

        VendorInfoDTO vendorInfoDTO = new VendorInfoDTO(account, vendor, shippingInfo);
        JsonNode patched = patch.apply(objectMapper.convertValue(vendorInfoDTO, JsonNode.class));
        vendorInfoDTO = objectMapper.treeToValue(patched, VendorInfoDTO.class);

        if(!vendorInfoDTO.getEmail().equals(account.getEmail())
                || !vendorInfoDTO.getType().equals(account.getType())
                || !vendorInfoDTO.getUsername().equals(account.getUsername())
                || vendorInfoDTO.isActive()!=(account.isActive())) {

            throw new JsonPatchException("Restricted attribute");
        }

        account.setId(vendorInfoDTO.getAccountId());
        account.setPassword(vendorInfoDTO.getPassword());

        vendor.setOrganisationName(vendorInfoDTO.getOrganisationName());
        vendor.setTaxNumber(vendorInfoDTO.getTaxNumber());

        shippingInfo.setAddress(vendorInfoDTO.getAddress());
        shippingInfo.setPhone(vendorInfoDTO.getPhone());
    }

    @Transactional
    public void updateClientInfo(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Client client = clientRepository.findByAccount_Id(id)
                .orElse(new Client());
        ShippingInfo shippingInfo = shippingInfoRepository.findByAccount_Id(id)
                .orElse(new ShippingInfo());

        ClientInfoDTO clientInfoDTO = new ClientInfoDTO(account, client, shippingInfo);
        JsonNode patched = patch.apply(objectMapper.convertValue(clientInfoDTO, JsonNode.class));
        clientInfoDTO = objectMapper.treeToValue(patched, ClientInfoDTO.class);

        if(!clientInfoDTO.getEmail().equals(account.getEmail())
                || !clientInfoDTO.getType().equals(account.getType())
                || !clientInfoDTO.getUsername().equals(account.getUsername())
                || clientInfoDTO.isActive()!=(account.isActive())) {

            throw new JsonPatchException("Restricted attribute");
        }

        account.setId(clientInfoDTO.getAccountId());
        account.setPassword(clientInfoDTO.getPassword());

        client.setLastName(clientInfoDTO.getLastName());
        client.setFirstName(clientInfoDTO.getFirstName());

        shippingInfo.setAddress(clientInfoDTO.getAddress());
        shippingInfo.setPhone(clientInfoDTO.getPhone());
    }
}