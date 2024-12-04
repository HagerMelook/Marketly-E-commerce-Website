package com.market.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private boolean isActive;

    @Column(name = "type")
    private String type;

    @Column(name = "username")
    private String username;

    public Account(String email, String password, boolean isActive, String type, String username) {
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.type = type;
        this.username = username;
    }
}