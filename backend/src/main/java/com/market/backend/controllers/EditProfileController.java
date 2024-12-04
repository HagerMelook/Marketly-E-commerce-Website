package com.market.backend.controllers;

import com.market.backend.dtos.AdminInfoDTO;
import com.market.backend.dtos.ClientInfoDTO;
import com.market.backend.dtos.UpdateStringInfoDTO;
import com.market.backend.dtos.VendorInfoDTO;
import com.market.backend.models.Account;
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

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putAccount(@PathVariable Long requestedId, @RequestBody Account newAccount) {
        if (editProfileService.updateAccount(requestedId, newAccount)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("admininfo/firstname/{requestedId}")
    private ResponseEntity<Void> putAdminFirstName(@PathVariable Long requestedId, @RequestBody UpdateStringInfoDTO newDataObj) {
        if (editProfileService.updateAdminFirstName(requestedId, newDataObj.getNewData())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("admininfo/lastname/{requestedId}")
    private ResponseEntity<Void> putAdminLastName(@PathVariable Long requestedId, @RequestBody UpdateStringInfoDTO newDataObj) {
        if (editProfileService.updateAdminLastName(requestedId, newDataObj.getNewData())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("clientinfo/firstname/{requestedId}")
    private ResponseEntity<Void> putClientFirstName(@PathVariable Long requestedId, @RequestBody UpdateStringInfoDTO newDataObj) {
        if (editProfileService.updateClientFirstName(requestedId, newDataObj.getNewData())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("clientinfo/lastname/{requestedId}")
    private ResponseEntity<Void> putClientLastName(@PathVariable Long requestedId, @RequestBody UpdateStringInfoDTO newDataObj) {
        if (editProfileService.updateClientLastName(requestedId, newDataObj.getNewData())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("vendorinfo/orgname/{requestedId}")
    private ResponseEntity<Void> putVendorOrgName(@PathVariable Long requestedId, @RequestBody UpdateStringInfoDTO newDataObj) {
        if (editProfileService.updateVendorOrgName(requestedId, newDataObj.getNewData())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("vendorinfo/taxnumber/{requestedId}")
    private ResponseEntity<Void> putVendorTaxNumber(@PathVariable Long requestedId, @RequestBody UpdateStringInfoDTO newDataObj) {
        if (editProfileService.updateVendorTaxNumber(requestedId, newDataObj.getNewData())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

}