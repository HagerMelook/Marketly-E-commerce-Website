package com.market.backend.signup.basicSignUp.Model;

import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendorbasicauth")
public class BasicVendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "businessname", nullable = false, unique = true)
    private String businessname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "taxnumber", nullable = false, unique = true)
    private int taxnumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBusinessname(String username) {
        this.businessname = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusinessname() {
        return businessname;
    }

    public String getPassword() {
        return password;
    }

    public int getTaxnumber() {
        return taxnumber;
    }

    public void setTaxnumber(int taxnumber) {
        this.taxnumber = taxnumber;
    }

}
