package com.market.backend.services;

import com.market.backend.models.*;
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

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final VendorRepository vendorRepository;
    private final AccountRepository accountRepository;
    private final FeedbackRepository feedbackRepository;
    private final VendorRequestRepository requestRepository;

    public AdminService(AdminRepository adminRepository, ClientRepository clientRepository, VendorRepository vendorRepository, AccountRepository accountRepository, FeedbackRepository feedbackRepository, VendorRequestRepository requestRepository) {
        this.adminRepository = adminRepository;
        this.clientRepository = clientRepository;
        this.vendorRepository = vendorRepository;
        this.accountRepository = accountRepository;
        this.feedbackRepository = feedbackRepository;
        this.requestRepository = requestRepository;
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

            Client client = clientRepository.findByAccount_Id(id)
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

            Admin admin = adminRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Admin not found"));

            Client client = new Client(admin.getFirstName(), admin.getLastName(), account);
            adminRepository.deleteById(account.getId());
            clientRepository.save(client);
        }
    }

    public List<Feedback> getFeedbacks() {
        return feedbackRepository.findAll();
    }

    public void deleteFeedback(long feedbackId) {
        feedbackRepository.deleteById(feedbackId);
    }

    public List<VendorRequest> getVendorRequests() {
        return requestRepository.findAll();
    }

//    public void addVendor(long requestId) {
//        Optional<VendorRequest> optionalPendingVendor = requestRepo.findById(requestId);
//
//        if (optionalPendingVendor.isPresent()) {
//            VendorRequest pendingVendor = optionalPendingVendor.get();
//
//            Account account = new Account();
//            Vendor vendor = new Vendor();
//
//            account.setUsername(pendingVendor.getUsername());
//            account.setPassword(pendingVendor.getPassword());
//            account.setEmail(pendingVendor.getEmail());
//            account.setActive(true);
//            account.setType("vendor");
//            vendor.setAccount(account);
//            vendor.setOrganisationName(pendingVendor.getOrganisationName());
//            vendor.setTaxNumber(pendingVendor.getTaxNumber());
//
//            vendorRepository.save(vendor);
//            accountRepository.save(account);
//            requestRepo.delete(pendingVendor);
//
//        } else {
//            throw new RuntimeException("VendorRequest not found with ID: " + requestId);
//        }
//    }
//
//    public void declineVendorRequest(long requestId) {
//        requestRepo.deleteById(requestId);
//    }
}
