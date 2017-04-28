package com.example.bookstorelocaldemo;

import org.litepal.crud.DataSupport;

public class Users extends DataSupport{

    private String username="";
    private String password="";

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
