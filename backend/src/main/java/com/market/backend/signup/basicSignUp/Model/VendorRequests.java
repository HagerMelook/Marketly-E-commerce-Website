package com.market.backend.signup.basicSignUp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

@Entity
@Component
public class VendorRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aid;
    private String email;
    private String password;
    private String username;
    private String organizationName;
    private long taxNumber;

    public VendorRequests() {}

    public VendorRequests(String email, String username) {
        this.email = email;
        this.username = username;
    }

//    For Google Auth
    public VendorRequests(String email, String username, String organizationNumber, long taxNumber) {
        this(email, username);
        this.organizationName = organizationNumber;
        this.taxNumber = taxNumber;
    }

//    For Basic Auth
    public VendorRequests(String email, String password, String username, String organizationNumber, long taxNumber) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.organizationName = organizationNumber;
        this.taxNumber = taxNumber;
    }

    public void setTaxNumber(long taxNumber) {
        this.taxNumber = taxNumber;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public String getOrganizationName() {
        return organizationName;
    }

    public long getTaxNumber() {
        return taxNumber;
    }

    @Override
    public String toString() {
        return "VendorRequests{" +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
//                ", organizationName=" + organizationName +
//                ", taxNumber=" + taxNumber +
                '}';
    }

}
