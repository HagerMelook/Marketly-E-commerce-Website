package com.market.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Vendor {
    @Id
    private Long id;

    @Column(name = "organization_name", unique = true, nullable = false)
    private String organizationName;

    @Column(name = "tax_number", unique = true)
    private Long taxNumber;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;
}