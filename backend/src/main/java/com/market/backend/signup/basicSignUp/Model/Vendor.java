package com.market.backend.signup.basicSignUp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Vendor {

    @Id
//    @GeneratedValue
    private int aid;
    private long organizationNumber;
    private long taxNumber;

    public Vendor(int aid, long organizationNumber, long taxNumber) {
        this.aid = aid;
        this.organizationNumber = organizationNumber;
        this.taxNumber = taxNumber;
    }

    public Vendor() {}

    public int getAid() {
        return aid;
    }

    public long getOrganizationNumber() {
        return organizationNumber;
    }

    public long getTaxNumber() {
        return taxNumber;
    }
}
