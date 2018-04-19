package com.example.christophergu.pg.data;

public class Account {

    private String username;
    private String phone;
    private String dob;

    public Account(String phone, String username, String dob){
        this.username = username;
        this.phone = phone;
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
