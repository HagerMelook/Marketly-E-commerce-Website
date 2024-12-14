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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class EditProfileService {
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final VendorRepository vendorRepository;
    private final AccountRepository accountRepository;
    private final PasswordRepository passwordRepository;
    private final ShippingInfoRepository shippingInfoRepository;

    private final ObjectMapper objectMapper;

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

    void checkRestrictedAttrs(Account account, Long accountId, String username, String type, boolean isActive, String authType) throws JsonPatchException {
        if (!accountId.equals(account.getId())
                || !type.equals(account.getType())
                || !username.equals(account.getUsername())
                || isActive != (account.isActive())
                || !authType.equals(account.getAuthType())) {

            throw new JsonPatchException("Restricted attribute");
        }
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

        checkRestrictedAttrs(account, adminInfoDTO.getAccountId(), adminInfoDTO.getUsername()
                , adminInfoDTO.getType(), adminInfoDTO.isActive(), adminInfoDTO.getAuthType());

        if (password != null) {
            password.setAccountPassword(adminInfoDTO.getPassword());
        } else if (adminInfoDTO.getPassword() != null) {
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

        checkRestrictedAttrs(account, vendorInfoDTO.getAccountId(), vendorInfoDTO.getUsername()
                , vendorInfoDTO.getType(), vendorInfoDTO.isActive(), vendorInfoDTO.getAuthType());

        if (password != null) {
            password.setAccountPassword(vendorInfoDTO.getPassword());
        } else if (vendorInfoDTO.getPassword() != null) {
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

        checkRestrictedAttrs(account, clientInfoDTO.getAccountId(), clientInfoDTO.getUsername()
                , clientInfoDTO.getType(), clientInfoDTO.isActive(), clientInfoDTO.getAuthType());

        if (password != null) {
            password.setAccountPassword(clientInfoDTO.getPassword());
        } else if (clientInfoDTO.getPassword() != null) {
            throw new JsonPatchException("Restricted attribute");
        }

        client.setLastName(clientInfoDTO.getLastName());
        client.setFirstName(clientInfoDTO.getFirstName());

        shippingInfo.setAddress(clientInfoDTO.getAddress());
        shippingInfo.setPhoneNumber(clientInfoDTO.getPhone());
    }
}