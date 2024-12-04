package com.market.backend.services;

import com.market.backend.models.Account;
import com.market.backend.models.Admin;
import com.market.backend.models.Client;
import com.market.backend.models.Feedback;
import com.market.backend.models.Vendor;
import com.market.backend.models.VendorRequest;
import com.market.backend.repositories.FeedbackRepository;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.AdminRepository;
import com.market.backend.repositories.ClientRepository;
import com.market.backend.repositories.VendorRepository;
import com.market.backend.repositories.VendorRequestRepository;
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
    private final FeedbackRepository feedbackRepo;
    private final VendorRequestRepository requestRepo;

    public AdminService(AdminRepository adminRepository, ClientRepository clientRepository, VendorRepository vendorRepository, AccountRepository accountRepository, FeedbackRepository feedbackRepo, VendorRequestRepository requestRepo) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.vendorRepository = vendorRepository;
        this.accountRepository = accountRepository;
        this.feedbackRepo = feedbackRepo;
        this.requestRepo = requestRepo;
    }

    @Transactional
    public void changeAccountStatus(String action, Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        switch (action) {
            case "activate":
                account.setActive(true);
                break;
            case "deactivate":
                account.setActive(false);
                break;
            default:
                throw new IllegalArgumentException("Invalid action");
        }
    }

    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        switch (account.getType()) {
            case "admin":
                adminRepository.deleteByAccount_Id(account.getId());
                break;
            case "client":
                clientRepository.deleteByAccount_Id(account.getId());
                break;
            case "vendor":
                vendorRepository.deleteByAccount_Id(account.getId());
                break;
            default:
                throw new IllegalArgumentException("Invalid action");
        }

        accountRepository.delete(account);
    }

    @Transactional
    public Account getAccountInfoByUserName(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public Account getAccountInfoByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public void promoteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (account.getType().equals("vendor")) {
            throw new IllegalArgumentException("Cannot promote a vendor");
        }

        if (account.getType().equals("client")) {
            account.setType("admin");
        } else {
            return;
        }

        Client client = clientRepository.findByAccount_Id(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        clientRepository.delete(client);

        Admin admin = new Admin(client.getFirstName(), client.getLastName(), account);
        accountRepository.delete(account);
        adminRepository.save(admin);
    }

    @Transactional
    public void demoteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (account.getType().equals("hardcoded admin")) {
            throw new IllegalArgumentException("Cannot demote the hardcoded admin");
        }

        if (account.getType().equals("admin")) {
            account.setType("client");
        } else {
            return;
        }

        Admin admin = adminRepository.findByAccount_Id(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        adminRepository.delete(admin);

        Client client = new Client(admin.getFirstName(), admin.getLastName(), account);
        accountRepository.delete(account);
        clientRepository.save(client);
    }

    public List<Feedback> getFeedbacks() {
        return feedbackRepo.findAll();
    }

    public void deleteFeedback(long feedbackId) {
        feedbackRepo.deleteById(feedbackId);
    }

    public List<VendorRequest> getVendorRequests() {
        return requestRepo.findAll();
    }

    public void addVendor(long requestId) {
        Optional<VendorRequest> optionalPendingVendor = requestRepo.findById(requestId);

        if (optionalPendingVendor.isPresent()) {
            VendorRequest pendingVendor = optionalPendingVendor.get();

            Account account = new Account();
            Vendor vendor = new Vendor();

            account.setUsername(pendingVendor.getUsername());
            account.setPassword(pendingVendor.getPassword());
            account.setEmail(pendingVendor.getEmail());
            account.setActive(true);
            account.setType("vendor");
            vendor.setAccount(account);
            vendor.setOrganisationName(pendingVendor.getOrganisationName());
            vendor.setTaxNumber(pendingVendor.getTaxNumber());

            vendorRepository.save(vendor);
            accountRepository.save(account);
            requestRepo.delete(pendingVendor);

        } else {
            throw new RuntimeException("VendorRequest not found with ID: " + requestId);
        }
    }

    public void declineVendorRequest(long requestId) {
        requestRepo.deleteById(requestId);
    }
}
