package com.example.christophergu.pg.data;

public class Account {

    private String username;
    private String phone;
    private String dob;
    private int age;

    public Account(){

    }

    public Account(String phone, String username, String dob, int age){
        this.username = username;
        this.phone = phone;
        this.dob = dob;
        this.age = age;
    }

    public Account(String phone, String username, String dob){
        this.username = username;
        this.phone = phone;
        this.dob = dob;
    }

    public Account(String phone){
        this.username = null;
        this.phone = phone;
        this.dob = null;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
