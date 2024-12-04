package com.market.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendor_id")
    private Long id;

    @Column(name = "organisation_name")
    private String organisationName;

    @Column(name = "tax_number")
    private String taxNumber;

    @OneToOne(cascade = CascadeType.ALL) // Defines the relationship
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;
}
