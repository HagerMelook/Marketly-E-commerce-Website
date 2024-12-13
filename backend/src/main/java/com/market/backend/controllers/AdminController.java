package com.market.backend.controllers;

import com.market.backend.models.Account;
import com.market.backend.services.AdminService;
import com.market.backend.models.Feedback;
import com.market.backend.models.VendorRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/info/{username}")
    public ResponseEntity<Account> getUserInfoByUserName(@PathVariable String username) {
        try {
            Account account = adminService.getAccountInfoByUserName(username);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateUserAccount(@PathVariable Long id) {
        try {
            adminService.changeAccountStatus(true, id);
            return ResponseEntity.ok("Account status changed successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateUserAccount(@PathVariable Long id) {
        try {
            adminService.changeAccountStatus(false, id);
            return ResponseEntity.ok("Account status changed successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        try {
            adminService.deleteAccount(id);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/promote/{id}")
    public ResponseEntity<String> promoteAccount(@PathVariable Long id) {
        try {
            adminService.promoteAccount(id);
            return ResponseEntity.ok("Account promoted successfully");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/demote/{id}")
    public ResponseEntity<String> demoteAccount(@PathVariable Long id) {
        try {
            adminService.demoteAccount(id);
            return ResponseEntity.ok("Account demoted successfully");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getFeedback() {
        return ResponseEntity.ok(adminService.getFeedbacks());
    }

    @DeleteMapping("/feedback/{feedbackId}")
    public ResponseEntity<String> deleteFeedback(@PathVariable long feedbackId) {
        try{
            adminService.deleteFeedback(feedbackId);
            return ResponseEntity.ok("{\"message\": \"Feedback deleted successfully\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/request")
    public ResponseEntity<List<VendorRequest>> getVendorRequests() {
        return ResponseEntity.ok(adminService.getVendorRequests());
    }

//    @PostMapping("/request/accept/{requestId}")
//    public ResponseEntity<String> acceptVendorRequest(@PathVariable long requestId) {
//        try{
//            adminService.addVendor(requestId);
//            return ResponseEntity.ok("{\"message\": \"Vendor has been added successfully\"}");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/request/delete/{requestId}")
//    public ResponseEntity<String> deleteVendorRequest(@PathVariable long requestId) {
//        try{
//            adminService.declineVendorRequest(requestId);
//            return ResponseEntity.ok("{\"message\": \"Vendor deleted successfully\"}");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}