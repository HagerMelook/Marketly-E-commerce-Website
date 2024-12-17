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
    @Column(unique=true, name = "account_id")
    private Long id;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "tax_number")
    private Long taxNumber;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;
}