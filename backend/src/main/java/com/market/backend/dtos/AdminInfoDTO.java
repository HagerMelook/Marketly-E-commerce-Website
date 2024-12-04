package com.market.backend.dtos;

import com.market.backend.models.Account;
import com.market.backend.models.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminInfoDTO {
    // Fields from Account
    private Long accountId;
    private String email;
    private String password;
    private boolean isActive;
    private String type;
    private String username;

    // Fields from Admin
    private String firstName;
    private String lastName;

    // Constructor to map fields from both Account and Admin
    public AdminInfoDTO(Account account, Admin admin) {
        // Map fields from Account
        if (account != null) {
            this.accountId = account.getId();
            this.email = account.getEmail();
            this.password = account.getPassword();
            this.isActive = account.isActive();
            this.type = account.getType();
            this.username = account.getUsername();
        }

        // Map fields from Admin
        if (admin != null) {
            this.firstName = admin.getFirstName();
            this.lastName = admin.getLastName();
        }
    }
}
