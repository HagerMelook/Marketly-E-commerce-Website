package com.market.backend.services;

import com.market.backend.models.*;
import com.market.backend.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final VendorRepository vendorRepository;
    private final AccountRepository accountRepository;
    private final FeedbackRepository feedbackRepository;
    private final VendorRequestRepository requestRepository;
    private final PasswordRepository passwordRepository;

    public AdminService(AdminRepository adminRepository, ClientRepository clientRepository, VendorRepository vendorRepository,
                        AccountRepository accountRepository, FeedbackRepository feedbackRepository, VendorRequestRepository requestRepository,
                        PasswordRepository passwordRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.vendorRepository = vendorRepository;
        this.accountRepository = accountRepository;
        this.feedbackRepository = feedbackRepository;
        this.requestRepository = requestRepository;
        this.passwordRepository = passwordRepository;

    }

    @Transactional
    public void changeAccountStatus(boolean isActive, Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        account.setActive(isActive);
    }

    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        switch (account.getType().toLowerCase()) {
            case "admin":
                adminRepository.deleteById(account.getId());
                break;
            case "client":
                clientRepository.deleteById(account.getId());
                break;
            case "vendor":
                vendorRepository.deleteById(account.getId());
                break;
            default:
                throw new IllegalArgumentException("Invalid action");
        }

        passwordRepository.deleteById(account.getId());
        accountRepository.delete(account);
    }

    @Transactional
    public Account getAccountInfoByUserName(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public void promoteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (account.getType().equalsIgnoreCase("vendor")) {
            throw new IllegalArgumentException("Cannot promote a vendor");
        }

        if (account.getType().equalsIgnoreCase("client")) {
            account.setType("admin");

            if (!clientRepository.existsById(id)) {
                return;
            }

            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));

            Admin admin = new Admin(client.getFirstName(), client.getLastName(), account);
            clientRepository.delete(client);
            adminRepository.save(admin);
        }
    }

    @Transactional
    public void demoteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (account.getType().equalsIgnoreCase("hardcoded admin")) {
            throw new IllegalArgumentException("Cannot demote the hardcoded admin");
        }

        if (account.getType().equalsIgnoreCase("admin")) {
            account.setType("client");

            if (!adminRepository.existsById(id)) {
                return;
            }

            Admin admin = adminRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Admin not found"));

            Client client = new Client(admin.getFirstName(), admin.getLastName(), account);
            adminRepository.deleteById(account.getId());
            clientRepository.save(client);
        }
    }

    @Transactional
    public List<Feedback> getFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Transactional
    public void deleteFeedback(long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }

    @Transactional
    public List<VendorRequest> getVendorRequests() {
        return requestRepository.findAll();
    }

    @Transactional
    public void declineVendorRequest(long requestId) {
        requestRepository.deleteById(requestId);
    }

    @Transactional
    public void addVendor(long requestId) {
        Optional<VendorRequest> optionalPendingVendor = requestRepository.findById(requestId);

        if (optionalPendingVendor.isPresent()) {
            VendorRequest pendingVendor = optionalPendingVendor.get();

            Account account = new Account();
            Vendor vendor = new Vendor();
            account.setUsername(pendingVendor.getUsername());
            account.setActive(true);
            account.setType("vendor");
            account.setAuthType(pendingVendor.getAuthType());
            accountRepository.save(account);

            vendor.setOrganizationName(pendingVendor.getOrganizationName());
            vendor.setTaxNumber(pendingVendor.getTaxNumber());
            vendor.setAccount(account);
            vendorRepository.save(vendor);

            if(pendingVendor.getAuthType().equals("oauth")){
                Password password = new Password();
                password.setAccountPassword((pendingVendor.getPassword()));
                password.setAccount(account);
                passwordRepository.save(password);
            }

            requestRepository.delete(pendingVendor);

        } else {
            throw new RuntimeException("VendorRequest not found with ID: " + requestId);
        }
    }
}
