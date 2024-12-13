package com.market.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vendor {
    @Id
    private Long id;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "tax_number")
    private Long taxNumber;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;
}