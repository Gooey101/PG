package com.example.christophergu.pg.data;

public class joinTeam {
    private String phone;
    private int tid;

    public joinTeam(String phone, int tid) {
        this.phone = phone;
        this.tid = tid;
    }


    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
