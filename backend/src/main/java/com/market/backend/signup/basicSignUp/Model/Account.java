package com.market.backend.signup.basicSignUp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aid;
    private String email;
    private String password;
    private String username;
    private String type;
    private String status;

    public Account() {}

    public Account(String status, String type, String username, String password, String email) {
        this.status = status;
        this.type = type;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Account(String email, String username, String type, String status) {
        this.email = email;
        this.username = username;
        this.type = type;
        this.status = status;
    }

    public int getAid() {
        return aid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }
}
