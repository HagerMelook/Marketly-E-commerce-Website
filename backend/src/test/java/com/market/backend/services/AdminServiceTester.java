package com.market.backend.services;

import com.market.backend.models.Account;
import com.market.backend.models.Feedback;
import com.market.backend.models.Vendor;
import com.market.backend.models.VendorRequest;
import com.market.backend.repositories.AccountRepository;
import com.market.backend.repositories.FeedbackRepository;
import com.market.backend.repositories.VendorRepository;
import com.market.backend.repositories.VendorRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private FeedbackRepository feedbackRepo;
    @Mock
    private VendorRequestRepository requestRepo;
    @Mock
    private VendorRepository vendorRepo;
    @Mock
    private AccountRepository accountRepo;
    @InjectMocks
    private AdminService adminService;

    @Test
    public void TestGetFeedbacks(){
        Feedback feedback1 = new Feedback(1L, "Great product!");
        Feedback feedback2 = new Feedback(2L, "Needs improvement.");
        List<Feedback> feedbacks = List.of(feedback1, feedback2);

        when(feedbackRepo.findAll()).thenReturn(feedbacks);

        List<Feedback> result = adminService.getFeedbacks();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Great product!", result.get(0).getBody());
        Assertions.assertEquals("Needs improvement.", result.get(1).getBody());
    }

    @Test
    public void TestDeleteFeedback(){
        long feedbackId = 1L;
        adminService.deleteFeedback(feedbackId);
        verify(feedbackRepo, times(1)).deleteById(feedbackId);
    }

//    @Test
//    public void TestDeleteFeedback2(){
//        Feedback feedback = new Feedback(1L, "Great product!");
//        when(feedbackRepo.findById(1L)).thenReturn(Optional.of(feedback));
//        adminService.deleteFeedback(1L);
//
//    }

    @Test
    public void TestGetVendorRequests(){
        VendorRequest vendorRequest1 = new VendorRequest(1L, "email@example.com", "password", "vendor1", "Vendor One", "TAX123");
        VendorRequest vendorRequest2 = new VendorRequest(2L, "email2@example.com", "password2", "vendor2", "Vendor Two", "TAX456");
        List<VendorRequest> requests = List.of(vendorRequest1, vendorRequest2);

        when(requestRepo.findAll()).thenReturn(requests);

        List<VendorRequest> result = adminService.getVendorRequests();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("vendor1", result.get(0).getUsername());
        Assertions.assertEquals("vendor2", result.get(1).getUsername());
    }

    @Test
    public void TestAddVendor(){
        VendorRequest vendorRequest = new VendorRequest(1L, "email@example.com", "password", "vendor1", "Vendor One", "TAX123");
        Account account = new Account("email@example.com", "password", true, "vendor", "vendor1");
        Vendor vendor = new Vendor("Vendor One", "TAX123", account);

        when(requestRepo.findById(1L)).thenReturn(Optional.of(vendorRequest));
        when(accountRepo.save(any(Account.class))).thenReturn(account);
        when(vendorRepo.save(any(Vendor.class))).thenReturn(vendor);

        adminService.addVendor(1L);

        verify(requestRepo, times(1)).delete(vendorRequest);
        verify(accountRepo, times(1)).save(account);
        verify(vendorRepo, times(1)).save(vendor);
    }

    @Test
    public void TestDeclineVendorRequest(){
        long requestId = 1L;
        adminService.declineVendorRequest(requestId);
        verify(requestRepo, times(1)).deleteById(requestId);
    }

}