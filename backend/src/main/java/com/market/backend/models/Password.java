package com.market.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Password {
    @Id
    private Long accountId;

    @Column(name = "account_password", nullable = false)
    private String accountPassword;

    @OneToOne
    @MapsId
//    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;
}
