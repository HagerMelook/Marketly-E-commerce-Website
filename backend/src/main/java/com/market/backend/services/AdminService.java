package com.market.backend.services;
import com.market.backend.models.Account;
import com.market.backend.models.Feedback;
import com.market.backend.models.Vendor;
import com.market.backend.models.VendorRequest;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.FeedbackRepository;
import com.market.backend.repositories.VendorRepository;
import com.market.backend.repositories.VendorRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final FeedbackRepository feedbackRepo;
    private final VendorRequestRepository requestRepo;
    private final VendorRepository vendorRepo;
    private final AccountRepository accountRepo;

    public AdminService(FeedbackRepository feedbackRepo, VendorRequestRepository requestRepo, VendorRepository vendorRepo, AccountRepository accountRepo) {
        this.feedbackRepo = feedbackRepo;
        this.requestRepo = requestRepo;
        this.vendorRepo = vendorRepo;
        this.accountRepo = accountRepo;
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
            account.setVendor(vendor);

            vendorRepo.save(vendor);
            accountRepo.save(account);
            requestRepo.delete(pendingVendor);

        } else {
            throw new RuntimeException("VendorRequest not found with ID: " + requestId);
        }
    }

    public void declineVendorRequest(long requestId) {
        requestRepo.deleteById(requestId);
    }
}
