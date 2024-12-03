package com.market.backend.controllers;

import com.market.backend.models.Account;
import com.market.backend.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
            // Directly return the IUser object as the response body
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Account> getUserInfoByEmail(@RequestParam String email) {
        try {
            Account account = adminService.getAccountInfoByEmail(email);
            // Directly return the IUser object as the response body
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/activate/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> activateUserAccount(@PathVariable Long id) {
        try {
            adminService.changeAccountStatus("activate", id);
            return ResponseEntity.ok("Account status changed successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @PutMapping("/deactivate/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deactivateUserAccount(@PathVariable Long id) {
        try {
            adminService.changeAccountStatus("deactivate", id);
            return ResponseEntity.ok("Account status changed successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
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
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
