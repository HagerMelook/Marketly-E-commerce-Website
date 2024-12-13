package com.market.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class VendorRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "tax_number")
    private Long taxNumber;

    public VendorRequest(String username, String organizationName, Long taxNumber) {
        this.username = username;
        this.organizationName = organizationName;
        this.taxNumber = taxNumber;
    }
}
