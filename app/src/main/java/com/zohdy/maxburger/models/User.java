package com.zohdy.maxburger.models;

import java.util.ArrayList;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class User {
    private String name;
    private String password;
    private String phoneNumber;
    private String email;

    public User(String name, String password, String phoneNumber, String email) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
