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

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "organization_name", unique = true)
    private String organizationName;

    @Column(name = "tax_number", unique = true)
    private Long taxNumber;

    @Column(name = "auth_type", nullable = false)
    private String authType;
}
