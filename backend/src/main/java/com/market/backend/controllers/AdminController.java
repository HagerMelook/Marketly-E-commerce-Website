package com.market.backend.controllers;

import com.market.backend.models.Feedback;
import com.market.backend.models.VendorRequest;
import com.market.backend.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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

    @PostMapping("/request/accept/{requestId}")
    public ResponseEntity<String> acceptVendorRequest(@PathVariable long requestId) {
        try{
            adminService.addVendor(requestId);
            return ResponseEntity.ok("{\"message\": \"Vendor has been added successfully\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/request/delete/{requestId}")
    public ResponseEntity<String> deleteVendorRequest(@PathVariable long requestId) {
        try{
            adminService.declineVendorRequest(requestId);
            return ResponseEntity.ok("{\"message\": \"Vendor deleted successfully\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
