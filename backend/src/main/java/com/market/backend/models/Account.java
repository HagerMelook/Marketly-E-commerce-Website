package com.market.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "status", nullable = false)
    private boolean isActive = true;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "auth_type", nullable = false)
    private String authType;
}