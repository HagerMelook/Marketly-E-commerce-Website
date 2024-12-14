package com.market.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.market.backend.dtos.AdminInfoDTO;
import com.market.backend.dtos.ClientInfoDTO;
import com.market.backend.dtos.VendorInfoDTO;
import com.market.backend.services.EditProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class EditProfileController {
    private final EditProfileService editProfileService;

    public EditProfileController(EditProfileService editProfileService) {
        this.editProfileService = editProfileService;
    }

    @GetMapping("/admininfo/{id}")
    public ResponseEntity<AdminInfoDTO> getAdminInfoById(@PathVariable Long id) {
        try {
            AdminInfoDTO adminInfo = editProfileService.getAdminInfo(id);
            return new ResponseEntity<>(adminInfo, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/clientinfo/{id}")
    public ResponseEntity<ClientInfoDTO> getClientInfoById(@PathVariable Long id) {
        try {
            ClientInfoDTO clientInfo = editProfileService.getClientInfo(id);
            return new ResponseEntity<>(clientInfo, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vendorinfo/{id}")
    public ResponseEntity<VendorInfoDTO> getVendorInfoById(@PathVariable Long id) {
        try {
            VendorInfoDTO vendorInfo = editProfileService.getVendorInfo(id);
            return new ResponseEntity<>(vendorInfo, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(path = "/admininfo/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> updateAdminInfo(@PathVariable Long id, @RequestBody JsonPatch patch) {
        try {
            editProfileService.updateAdminInfo(id, patch);
            return ResponseEntity.ok().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(path = "/clientinfo/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> updateClientInfo(@PathVariable Long id, @RequestBody JsonPatch patch) {
        try {
            editProfileService.updateClientInfo(id, patch);
            return ResponseEntity.ok().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(path = "/vendorinfo/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Void> updateVendorInfo(@PathVariable Long id, @RequestBody JsonPatch patch) {
        try {
            editProfileService.updateVendorInfo(id, patch);
            return ResponseEntity.ok().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}