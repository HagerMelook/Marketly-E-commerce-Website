package com.market.backend.signup.basicSignUp.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

@Entity
@Component
public class Client {

    @Id
    private int aid;
    private String fname;
    private String lname;

    public Client() {}

    public Client(int aid, String fname, String lname) {
        this.aid = aid;
        this.fname = fname;
        this.lname = lname;
    }

    public int getAid() {
        return aid;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }
}
